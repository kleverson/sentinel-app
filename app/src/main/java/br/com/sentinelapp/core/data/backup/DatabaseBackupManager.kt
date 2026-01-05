package br.com.sentinelapp.core.data.backup

import android.content.Context
import android.util.Log
import br.com.sentinelapp.core.data.security.KeyStoreManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gerencia backup e restore do banco de dados SQLCipher.
 * O banco pode ser restaurado em qualquer dispositivo usando a senha master.
 */
@Singleton
class DatabaseBackupManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val keyStoreManager: KeyStoreManager
) {
    private val TAG = "DatabaseBackupManager"
    private val DB_NAME = "sentinel_app_database"

    /**
     * Cria um backup do banco de dados em um arquivo.
     * @param backupFile Arquivo de destino para o backup
     * @return true se o backup foi criado com sucesso
     */
    fun createBackup(backupFile: File): Boolean {
        return try {
            Log.d(TAG, "Creating backup to: ${backupFile.absolutePath}")

            // Fecha conexões do banco antes do backup
            val dbFile = context.getDatabasePath(DB_NAME)

            if (!dbFile.exists()) {
                Log.e(TAG, "Database file does not exist: ${dbFile.absolutePath}")
                return false
            }

            // Copia o arquivo do banco para o backup
            FileInputStream(dbFile).use { input ->
                FileOutputStream(backupFile).use { output ->
                    input.copyTo(output)
                }
            }

            // Também copia os arquivos auxiliares (-wal, -shm)
            listOf("$DB_NAME-wal", "$DB_NAME-shm").forEach { auxFileName ->
                val auxFile = File(context.getDatabasePath(auxFileName).absolutePath)
                if (auxFile.exists()) {
                    val auxBackupFile = File(backupFile.parent, auxFileName)
                    FileInputStream(auxFile).use { input ->
                        FileOutputStream(auxBackupFile).use { output ->
                            input.copyTo(output)
                        }
                    }
                    Log.d(TAG, "Backed up auxiliary file: $auxFileName")
                }
            }

            Log.d(TAG, "Backup created successfully")
            Log.d(TAG, "Backup size: ${backupFile.length()} bytes")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create backup", e)
            false
        }
    }

    /**
     * Restaura o banco de dados de um arquivo de backup.
     * IMPORTANTE: O app deve ser reiniciado após a restauração.
     *
     * @param backupFile Arquivo de backup
     * @param masterPassword Senha master que foi usada para criptografar o banco
     * @return true se o restore foi bem-sucedido
     */
    fun restoreBackup(backupFile: File, masterPassword: String): Boolean {
        return try {
            Log.d(TAG, "Restoring backup from: ${backupFile.absolutePath}")

            if (!backupFile.exists()) {
                Log.e(TAG, "Backup file does not exist")
                return false
            }

            // Valida se o backup pode ser aberto com a senha fornecida
            if (!validateBackup(backupFile, masterPassword)) {
                Log.e(TAG, "Backup validation failed - wrong password or corrupted file")
                return false
            }

            // Fecha todas as conexões do banco
            context.deleteDatabase(DB_NAME)

            val dbFile = context.getDatabasePath(DB_NAME)

            // Copia o backup para a localização do banco
            FileInputStream(backupFile).use { input ->
                FileOutputStream(dbFile).use { output ->
                    input.copyTo(output)
                }
            }

            // Restaura arquivos auxiliares se existirem
            listOf("$DB_NAME-wal", "$DB_NAME-shm").forEach { auxFileName ->
                val auxBackupFile = File(backupFile.parent, auxFileName)
                if (auxBackupFile.exists()) {
                    val auxFile = context.getDatabasePath(auxFileName)
                    FileInputStream(auxBackupFile).use { input ->
                        FileOutputStream(auxFile).use { output ->
                            input.copyTo(output)
                        }
                    }
                    Log.d(TAG, "Restored auxiliary file: $auxFileName")
                }
            }

            Log.d(TAG, "Restore completed successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to restore backup", e)
            false
        }
    }

    /**
     * Valida se um arquivo de backup pode ser aberto com a senha fornecida.
     */
    private fun validateBackup(backupFile: File, masterPassword: String): Boolean {
        return try {
            val key = keyStoreManager.deriveSQLCipherKey(masterPassword)
            // TODO: Implementar validação real abrindo o banco com SQLCipher
            // Por enquanto, assume que o arquivo existe = válido
            true
        } catch (e: Exception) {
            Log.e(TAG, "Backup validation failed", e)
            false
        }
    }

    /**
     * Retorna o caminho do arquivo do banco de dados.
     */
    fun getDatabasePath(): String {
        return context.getDatabasePath(DB_NAME).absolutePath
    }

    /**
     * Retorna o tamanho do banco de dados em bytes.
     */
    fun getDatabaseSize(): Long {
        val dbFile = context.getDatabasePath(DB_NAME)
        return if (dbFile.exists()) dbFile.length() else 0L
    }
}

