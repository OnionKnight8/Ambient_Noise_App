package com.example.ambient_app

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class FavoritesRepository(private val favoritesDAO: FavoritesDAO) {
    // Room queries will execute on a separate thread and notify an observer of changes.
    val allEntries: Flow<List<FavoritesEntry>> = favoritesDAO.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(favoritesEntry: FavoritesEntry) {
        favoritesDAO.insert(favoritesEntry)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteEntry(favoritesEntry: FavoritesEntry) {
        favoritesDAO.delete(favoritesEntry)
    }
}