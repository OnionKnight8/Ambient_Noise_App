package com.example.ambient_app

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class Application : Application() {
    val scope = CoroutineScope(SupervisorJob())

    // Lazy instantiation creates objects when needed
    val favoritesDatabase by lazy { FavoritesDatabase.getDatabase(this, scope) }
    val favoritesRepository by lazy { FavoritesRepository(favoritesDatabase.favoritesDAO()) }
}