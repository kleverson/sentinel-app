package br.com.sentinelapp.core.data

import android.content.Context
import androidx.room.Room
import br.com.sentinelapp.core.data.dao.PasswordDao
import br.com.sentinelapp.core.data.security.KeyStoreManager
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
        keyStoreManager: KeyStoreManager
    ): AppDatabase{
        val passPhrase = keyStoreManager.getOrCreatePassphrase()

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
