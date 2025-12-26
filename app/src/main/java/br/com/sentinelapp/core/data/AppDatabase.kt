package br.com.sentinelapp.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.sentinelapp.core.data.dao.PasswordDao
import br.com.sentinelapp.core.data.entities.PasswordEntity

@Database(
    entities = [PasswordEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun passwordDao(): PasswordDao
}