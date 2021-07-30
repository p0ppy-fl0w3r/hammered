package com.example.hammered.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.hammered.datastore.SettingsPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(application:Application) : AndroidViewModel(application) {

    private val preferences = SettingsPreferences(application)

     val currentStartupScreen = preferences.currentStartupScreen.asLiveData()

    fun changeStartupScreen(position: Int){
        viewModelScope.launch(Dispatchers.IO) {
            preferences.changeStartupScreen(position)
        }
    }

}