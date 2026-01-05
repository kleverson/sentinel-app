package br.com.sentinelapp.core.data

import android.content.Context
import androidx.room.Room
import br.com.sentinelapp.core.data.dao.PasswordDao
import br.com.sentinelapp.core.data.security.KeyStoreManager
import br.com.sentinelapp.core.data.security.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        keyStoreManager: KeyStoreManager,
        sessionManager: SessionManager
    ): AppDatabase{
        // Usa a senha master da sessão para derivar a chave do SQLCipher
        // Isso permite que o mesmo banco seja aberto em outro dispositivo com a mesma senha
        val masterPassword = sessionManager.getMasterPassword()
        val passPhrase = keyStoreManager.deriveSQLCipherKey(masterPassword)

        val factory = SupportFactory(passPhrase)

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "sentinel_app_database"
        ).openHelperFactory(factory)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDao(db: AppDatabase): PasswordDao =
        db.passwordDao()
}
