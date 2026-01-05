package br.com.sentinelapp.core.data.security

import android.content.Context
import android.util.Base64
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KeyStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefsName = "sentinel_keystore_prefs"
    private val prefPasswordHashKey = "password_hash"
    private val prefSaltKey = "password_salt"

    private val ITERATIONS = 120_000
    private val KEY_LENGTH = 256

    private val TAG = "KeyStoreManager"

    /**
     * Salva a senha master do usuário (armazena apenas um hash para validação futura).
     * A senha em si será usada para derivar a chave do SQLCipher.
     */
    fun setUserPassphrase(masterPassword: String) {
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

        Log.d(TAG, "=== START setUserPassphrase ===")
        Log.d(TAG, "Master password length: ${masterPassword.length}")
        Log.d(TAG, "Prefs file: $prefsName")

        // Log current state before write
        val beforeHash = prefs.getString(prefPasswordHashKey, null)
        val beforeSalt = prefs.getString(prefSaltKey, null)
        Log.d(TAG, "BEFORE - password_hash exists: ${beforeHash != null}")
        Log.d(TAG, "BEFORE - password_salt exists: ${beforeSalt != null}")

        // Gera salt único para este dispositivo
        val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
        Log.d(TAG, "Salt generated (${salt.size} bytes)")

        // Deriva hash da senha para validação futura
        val passwordHash = deriveKeyFromPassword(masterPassword, salt)
        Log.d(TAG, "Password hash derived (${passwordHash.size} bytes)")

        val hashEncoded = Base64.encodeToString(passwordHash, Base64.NO_WRAP)
        val saltEncoded = Base64.encodeToString(salt, Base64.NO_WRAP)

        Log.d(TAG, "Encoded hash length: ${hashEncoded.length} chars")
        Log.d(TAG, "Encoded salt length: ${saltEncoded.length} chars")

        // Force synchronous commit
        val editor = prefs.edit()
        editor.putString(prefPasswordHashKey, hashEncoded)
        editor.putString(prefSaltKey, saltEncoded)

        Log.d(TAG, "Calling editor.commit()...")
        val commitResult = editor.commit()
        Log.d(TAG, "Commit result: $commitResult")

        if (!commitResult) {
            Log.e(TAG, "SharedPreferences commit() returned false!")
            throw IllegalStateException("Failed to commit SharedPreferences - commit returned false")
        }

        // Verify write immediately after commit
        val storedHash = prefs.getString(prefPasswordHashKey, null)
        val storedSalt = prefs.getString(prefSaltKey, null)

        Log.d(TAG, "AFTER - password_hash exists: ${storedHash != null}")
        Log.d(TAG, "AFTER - password_salt exists: ${storedSalt != null}")

        if (storedHash == null || storedSalt == null) {
            Log.e(TAG, "Failed to persist password data to SharedPreferences")
            throw IllegalStateException("Failed to persist password data")
        }

        Log.d(TAG, "Password hash stored (${storedHash.length} chars)")
        Log.d(TAG, "Salt stored (${storedSalt.length} chars)")
        Log.d(TAG, "=== END setUserPassphrase SUCCESS ===")
    }

    /**
     * Verifica se a senha master fornecida está correta.
     */
    fun validateMasterPassword(password: String): Boolean {
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val storedHash = prefs.getString(prefPasswordHashKey, null) ?: return false
        val storedSalt = prefs.getString(prefSaltKey, null) ?: return false

        val salt = Base64.decode(storedSalt, Base64.NO_WRAP)
        val computedHash = deriveKeyFromPassword(password, salt)
        val computedHashEncoded = Base64.encodeToString(computedHash, Base64.NO_WRAP)

        return storedHash == computedHashEncoded
    }

    /**
     * Verifica se a senha master já foi configurada.
     */
    fun isMasterPasswordSet(): Boolean {
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        return prefs.contains(prefPasswordHashKey) && prefs.contains(prefSaltKey)
    }

    /**
     * Deriva a chave do SQLCipher a partir da senha master.
     * Esta é a chave que será usada para criptografar o banco de dados.
     *
     * IMPORTANTE: Para recuperação de backup, a mesma senha master sempre
     * produzirá a mesma chave do SQLCipher (usando um salt fixo).
     */
    fun deriveSQLCipherKey(masterPassword: String): ByteArray {
        // Usa um salt fixo e conhecido para garantir que a mesma senha
        // sempre gere a mesma chave (necessário para restaurar backups)
        val fixedSalt = "SentinelApp_SQLCipher_Salt_v1".toByteArray()
        return deriveKeyFromPassword(masterPassword, fixedSalt)
    }

    /**
     * Deriva uma chave AES de 256 bits a partir de uma senha usando PBKDF2.
     */
    private fun deriveKeyFromPassword(password: String, salt: ByteArray): ByteArray {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
        return factory.generateSecret(spec).encoded
    }

    /**
     * Remove a senha master (para testes ou reset).
     */
    fun clearMasterPassword() {
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        prefs.edit().clear().commit()
        Log.d(TAG, "Master password cleared")
    }
}
