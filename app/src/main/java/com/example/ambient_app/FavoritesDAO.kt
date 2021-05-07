package com.example.ambient_app

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDAO {
    @Query("SELECT * FROM favorites_entries ORDER BY id DESC")
    fun getAll(): Flow<List<FavoritesEntry>>

    @Query("DELETE FROM favorites_entries")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoritesEntry: FavoritesEntry)

    @Delete
    fun delete(entry: FavoritesEntry)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateEntry(entry: FavoritesEntry)

    @Query("DELETE FROM favorites_entries WHERE id = :input")
    suspend fun deleteId(input: Int)
}