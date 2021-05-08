package com.example.ambient_app

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoritesRepository: FavoritesRepository) : ViewModel() {
    val allEntries: LiveData<List<FavoritesEntry>> = favoritesRepository.allEntries.asLiveData()

    fun insert(favoritesEntry: FavoritesEntry) = viewModelScope.launch {
        favoritesRepository.insert(favoritesEntry)
    }

    fun delete(favoritesEntry: FavoritesEntry) = viewModelScope.launch {
        favoritesRepository.deleteEntry(favoritesEntry)
    }

    fun deleteId(favoritesId: Int) = viewModelScope.launch {
        favoritesRepository.deleteId(favoritesId)
    }
}

class FavoritesViewModelFactory(private val favoritesRepository: FavoritesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(favoritesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}