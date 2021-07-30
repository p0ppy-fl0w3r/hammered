package com.example.hammered.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsPreferences(private val context: Context) {



    companion object{
        val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
            name = "settings_pref"
        )

        val CURRENT_STARTUP_SCREEN = intPreferencesKey(name = "current_startup_screen")
    }

    suspend fun changeStartupScreen(position: Int){
        context.dataStore.edit { preferences ->
            preferences[CURRENT_STARTUP_SCREEN] = position
        }
    }

    val currentStartupScreen: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[CURRENT_STARTUP_SCREEN] ?: 0
    }
}