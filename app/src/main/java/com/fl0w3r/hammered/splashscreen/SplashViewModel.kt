package com.fl0w3r.hammered.splashscreen

import android.app.Application
import androidx.lifecycle.*
import com.fl0w3r.hammered.database.CocktailDatabase
import com.fl0w3r.hammered.datastore.SettingsPreferences
import com.fl0w3r.hammered.repository.CocktailRepository
import kotlinx.coroutines.launch

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CocktailRepository(CocktailDatabase.getDatabase(application))
    private val preferences = SettingsPreferences(application)

    val isDataInserted = preferences.currentDataInsertionStatus.asLiveData()
    val currentSelectedStartupScreen = preferences.currentStartupScreen.asLiveData()

    private val _populateDatabase = MutableLiveData<Boolean?>()

    val populateDatabase: LiveData<Boolean?>
        get() = _populateDatabase

    fun insertValuesInDatabase() {
        viewModelScope.launch {
            repository.insertAll()
            _populateDatabase.value = true
        }
    }

    fun donePopulating() {
        _populateDatabase.value = null
    }

    fun setDataInserted() {
        viewModelScope.launch {
            preferences.changeDataInsertStatus(true)
        }
    }
}