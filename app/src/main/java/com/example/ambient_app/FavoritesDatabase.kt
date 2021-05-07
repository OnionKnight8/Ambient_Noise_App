package com.example.ambient_app

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Database(entities = [FavoritesEntry::class], version = 2)
@TypeConverters(Converter::class)
abstract class FavoritesDatabase : RoomDatabase() {
    abstract fun favoritesDAO(): FavoritesDAO

    // Creates instance of database if one does not currently exist.
    companion object {
        @Volatile
        var INSTANCE: FavoritesDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): FavoritesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoritesDatabase::class.java, "favorites_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(FavoritesDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
        private class FavoritesDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

            // Create Database
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        initialPop(database.favoritesDAO())
                    }
                }
            }
        }

        suspend fun initialPop(favoritesDAO: FavoritesDAO) {
            // DELETE ALL
            favoritesDAO.deleteAll()

            // Add Samples
            var content: List<String> = mutableListOf("1","1","0","0","0","0")
            var volume: List<String> = mutableListOf("8","4","0","0","0","0")
            var favoritesEntry = FavoritesEntry(name = "Night Time Storm", content = content,
                volume = volume)
            favoritesDAO.insert(favoritesEntry)

            var content2: List<String> = mutableListOf("0","0","0","1","1","1")
            var volume2: List<String> = mutableListOf("0","0","0","6","2","2")
            favoritesEntry = FavoritesEntry(name = "City With White Noise", content = content2,
                volume = volume2)
            favoritesDAO.insert(favoritesEntry)
        }
    }
}