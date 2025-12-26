package br.com.sentinelapp.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.sentinelapp.core.data.entities.PasswordEntity

@Dao
interface PasswordDao {

    @Query("SELECT * FROM passwords")
    suspend fun getAllPasswords(): List<PasswordEntity>

    @Query("SELECT * FROM passwords WHERE id = :passwordId LIMIT 1")
    suspend fun getPasswordById(passwordId: Int): PasswordEntity?

    @Query("SELECT * FROM passwords WHERE provider LIKE :term or user like :term")
    suspend fun getPasswordByTerm(term: String): List<PasswordEntity>

    @Query("DELETE FROM passwords WHERE id = :id")
    suspend fun removePassword(id: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassword(passwordEntity: PasswordEntity) : Long
}