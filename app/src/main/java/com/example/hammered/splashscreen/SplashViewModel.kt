package com.example.hammered.splashscreen

import android.app.Application
import androidx.lifecycle.*
import com.example.hammered.database.CocktailDatabase
import com.example.hammered.datastore.SettingsPreferences
import com.example.hammered.repository.CocktailRepository
import kotlinx.coroutines.launch

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CocktailRepository(CocktailDatabase.getDatabase(application))
    private val preferences = SettingsPreferences(application)

    val currentSelectedStartupScreen = preferences.currentStartupScreen.asLiveData()

    private val _populateDatabase = MutableLiveData<Boolean?>()

    val populateDatabase : LiveData<Boolean?>
        get() = _populateDatabase

    fun insertValuesInDatabase(){
        viewModelScope.launch {
            repository.insertAll()
            _populateDatabase.value = true
        }
    }

    fun donePopulating(){
        _populateDatabase.value = null
    }
}