package com.fl0w3r.hammered.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.fl0w3r.hammered.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsPreferences(private val context: Context) {



    companion object{
        val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
            name = "settings_pref"
        )

        val CURRENT_STARTUP_SCREEN = intPreferencesKey(name = "current_startup_screen")
        val DATA_ADDED_TO_APP = booleanPreferencesKey(name = "data_inserted_to_database")
        val MIXER_SELECTION = stringPreferencesKey(name = "mixer_selection")
    }

    suspend fun changeStartupScreen(position: Int){
        context.dataStore.edit { preferences ->
            preferences[CURRENT_STARTUP_SCREEN] = position
        }
    }

    val currentStartupScreen: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[CURRENT_STARTUP_SCREEN] ?: 0
    }

    suspend fun changeDataInsertStatus(dataAdded: Boolean){
        context.dataStore.edit { preferences ->
            preferences[DATA_ADDED_TO_APP] = dataAdded
        }
    }

    val currentDataInsertionStatus: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DATA_ADDED_TO_APP] ?: false
    }

    suspend fun changeMixerOption(option: String){
        context.dataStore.edit { preferences ->
            preferences[MIXER_SELECTION] = option
        }
    }

    val currentMixerOptionSelection: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[MIXER_SELECTION] ?: Constants.SELECT_ALL
    }
}