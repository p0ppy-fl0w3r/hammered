package com.example.hammered.splashscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hammered.database.CocktailDatabase
import com.example.hammered.repository.CocktailRepository
import kotlinx.coroutines.launch

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CocktailRepository(CocktailDatabase.getDatabase(application))

    init {
        viewModelScope.launch { repository.insertAll() }
    }
}