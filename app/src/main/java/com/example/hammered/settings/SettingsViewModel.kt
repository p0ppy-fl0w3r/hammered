package com.example.hammered.settings

import android.app.Application
import android.os.Build
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.hammered.database.CocktailDatabase
import com.example.hammered.datastore.SettingsPreferences
import com.example.hammered.entities.Ingredient
import com.example.hammered.repository.CocktailRepository
import com.example.hammered.utils.JsonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.util.*

// The app will not ask for storage permission
// TODO make sure that the app requests for external storage permission
// TODO add indicator to show that the file is being saved.

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = SettingsPreferences(application)
    private val repository = CocktailRepository(CocktailDatabase.getDatabase(application))


    val currentStartupScreen = preferences.currentStartupScreen.asLiveData()

    fun changeStartupScreen(position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            preferences.changeStartupScreen(position)
        }
    }

    fun saveToJson() {
        viewModelScope.launch {
            val allIngredients = repository.getAllIngredient()
            val allCocktail = repository.getALlCocktail()
            val allRef = repository.getAllIngredientCocktailRef()

            val ingredientJson = JsonUtils.getJsonFromClass(allIngredients)
            val cocktailJson = JsonUtils.getJsonFromClass(allCocktail)
            val refJson = JsonUtils.getJsonFromClass(allRef)

            if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)) {
                val dir = Environment
                    .getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS
                    )?.absolutePath
                if (dir != null) {
                    if (dir.isNotBlank()) {
                        try {
                            val exportDir =
                                File(dir, "hammered_export${System.currentTimeMillis()}")

                            exportDir.mkdirs()

                            val ingredientFile = File(exportDir, "ingredient.json")
                            val cocktailFile = File(exportDir, "cocktail.json")
                            val refFile = File(exportDir, "ref.json")

                            ingredientFile.writeText(ingredientJson)
                            cocktailFile.writeText(cocktailJson)
                            refFile.writeText(refJson)

                            Timber.e("File created in ${exportDir.absolutePath}")
                        }
                        catch (e: Exception) {
                            Timber.e("File creation failed ${e.message}")
                        }
                    }
                    else {
                        Timber.e("The directory was blank.")
                    }
                }
                else {
                    Timber.e("The directory was null.")

                }
            }
            else{
                TODO("Implement for android Q and above.")
            }

            Timber.e("The json for ingredient is $ingredientJson")
            Timber.e("The json for cocktail is $cocktailJson")
            Timber.e("The json for ref is $refJson")

        }
    }

}