package com.example.hammered.settings

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.hammered.Constants
import com.example.hammered.database.CocktailDatabase
import com.example.hammered.datastore.SettingsPreferences
import com.example.hammered.entities.Cocktail
import com.example.hammered.entities.Ingredient
import com.example.hammered.entities.relations.IngredientCocktailRef
import com.example.hammered.repository.CocktailRepository
import com.example.hammered.utils.JsonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

// FIXME you can open the nav drawer from settings fragment
// FIXME Ingredient fragment opens default to all ingredient
// FIXME use file with glide in export image

// TODO change the export name of cocktail and/or ingredient
class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = SettingsPreferences(application)
    private val repository = CocktailRepository(CocktailDatabase.getDatabase(application))

    private val mApplication = application

    val currentStartupScreen = preferences.currentStartupScreen.asLiveData()

    private val _startJsonSave = MutableLiveData<Boolean?>()
    val startJsonSave: LiveData<Boolean?>
        get() = _startJsonSave

    // See Constants.kt for status codes.
    private val _jsonSaveStatus = MutableLiveData<Int?>()
    val jsonSaveStatus: LiveData<Int?>
        get() = _jsonSaveStatus

    fun changeStartupScreen(position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            preferences.changeStartupScreen(position)
        }
    }

    fun saveToJson() {

        _startJsonSave.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val allIngredients = repository.getAllIngredient()
            val allCocktail = repository.getALlCocktail()
            val allRef = repository.getAllIngredientCocktailRef()

            val ingredientImageBitmaps = mutableListOf<List<Any>>()
            val cocktailImageBitmap = mutableListOf<List<Any>>()

            val allIngredientAfterImage = mutableListOf<Ingredient>()
            val allCocktailAfterImage = mutableListOf<Cocktail>()

            for (i in allIngredients) {
                if (i.ingredient_image.isNotBlank()) {

                    val newIngredientImageName = "${i.ingredient_name}-ing"

                    Glide.with(mApplication).asBitmap().load(i.ingredient_image).into(
                        object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                ingredientImageBitmaps.add(listOf(newIngredientImageName, resource))
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                                Timber.i("The load was cleared.")
                            }
                        }
                    )
                    i.ingredient_image = newIngredientImageName
                    allIngredientAfterImage.add(i)
                }
            }

            for (i in allCocktail) {
                if (i.cocktail_image.isNotBlank()) {

                    val newCocktailImageName = i.cocktail_name + "-cocktail-${i.cocktail_id}"

                    Glide.with(mApplication).asBitmap().load(i.cocktail_image).into(
                        object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                cocktailImageBitmap.add(listOf(newCocktailImageName, resource))
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                                Timber.i("The cocktail image load was cleared.")
                            }
                        }
                    )
                    i.cocktail_image = newCocktailImageName
                    allCocktailAfterImage.add(i)
                }
            }


            val ingredientJson = JsonUtils.getJsonFromClass(allIngredientAfterImage)
            val cocktailJson = JsonUtils.getJsonFromClass(allCocktailAfterImage)
            val refJson = JsonUtils.getJsonFromClass(allRef)

            // Deprecated but still works for android 11
            val dir = Environment
                .getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                )?.absolutePath
            if (dir != null) {
                if (dir.isNotBlank()) {
                    try {
                        val exportDir =
                            File(dir, "hammered_export${System.currentTimeMillis()}")

                        if (exportDir.mkdirs()) {
                            Timber.i("Directory creation success.")
                        } else {
                            _jsonSaveStatus.postValue(Constants.DIRECTORY_CREATE_FAILED)
                            Timber.e("Directory not created.")
                        }

                        val ingredientFile = File(exportDir, Constants.INGREDIENT_JSON_FILE)
                        val cocktailFile = File(exportDir, Constants.COCKTAIL_JSON_FILE)
                        val refFile = File(exportDir, Constants.BRIDGING_JSON_FILE)

                        ingredientFile.writeText(ingredientJson)
                        cocktailFile.writeText(cocktailJson)
                        refFile.writeText(refJson)

                        for (imageBitmap in ingredientImageBitmaps) {

                            val imgDir = File(exportDir, "${imageBitmap[0]}.png")

                            val output = FileOutputStream(imgDir)
                            (imageBitmap[1] as Bitmap).compress(
                                Bitmap.CompressFormat.PNG,
                                100,
                                output
                            )
                            output.flush()
                            output.close()
                        }

                        for (cocktailBitmap in cocktailImageBitmap) {

                            val imgDir = File(exportDir, "${cocktailBitmap[0]}.png")

                            val output = FileOutputStream(imgDir)
                            (cocktailBitmap[1] as Bitmap).compress(
                                Bitmap.CompressFormat.PNG,
                                100,
                                output
                            )
                            output.flush()
                            output.close()
                        }

                        Timber.i("Data saved successfully to $exportDir.")
                        _jsonSaveStatus.postValue(Constants.DATA_SAVE_SUCCESS)
                    } catch (e: Exception) {
                        Timber.e("File creation failed ${e.message}")
                        _jsonSaveStatus.postValue(Constants.FILE_CREATION_FAILED)
                    }
                } else {
                    Timber.e("The directory was blank.")
                    _jsonSaveStatus.postValue(Constants.DIRECTORY_INVALID)
                }
            } else {
                Timber.e("The directory was null.")
                _jsonSaveStatus.postValue(Constants.GET_DIR_FAILED)
            }
        }
    }

    fun getFromJson(dataDir: Uri) {
        // TRIAL
        viewModelScope.launch(Dispatchers.IO) {
            val exportTree = DocumentFile.fromTreeUri(getApplication(), dataDir) ?: return@launch
            val fileList = exportTree.listFiles().toMutableList()

            var jCocktailFile: DocumentFile? = null
            var jRefFile: DocumentFile? = null
            var jIngredientFile: DocumentFile? = null

            val imageList = mutableListOf<DocumentFile>()

            for (i in fileList) {
                when (i.name) {
                    Constants.INGREDIENT_JSON_FILE -> jIngredientFile = i
                    Constants.COCKTAIL_JSON_FILE -> jCocktailFile = i
                    Constants.BRIDGING_JSON_FILE -> jRefFile = i
                    else -> imageList.add(i)
                }
            }

            if (jCocktailFile == null || jIngredientFile == null || jRefFile == null) {
                // Not a valid export folder
                // TODO show an error message
                return@launch
            }

            try {
                val ingredientJsonString = getJsonStringFromUri(jIngredientFile.uri)
                val cocktailJsonString = getJsonStringFromUri(jCocktailFile.uri)
                val refJsonString = getJsonStringFromUri(jRefFile.uri)

                val cocktailDataList = JsonUtils.getClassFromJson<Cocktail>(cocktailJsonString)
                val ingredientDataList =
                    JsonUtils.getClassFromJson<Ingredient>(ingredientJsonString)
                val refDataList = JsonUtils.getClassFromJson<IngredientCocktailRef>(refJsonString)

                for (image in imageList) {
                    mApplication.contentResolver.openInputStream(image.uri).use {
                        mApplication.openFileOutput(image.name, Context.MODE_PRIVATE)
                            .use { output ->
                                do {
                                    output.write(it?.readBytes())
                                } while (it?.read() != -1)
                            }
                    }
                }

                ingredientDataList?.forEach {
                    repository.insertIngredient(it)
                }

                cocktailDataList?.forEach {
                    repository.insertCocktail(it)
                }

                refDataList?.forEach {
                    repository.insertIngredientCocktailRef(it)
                }
                // TODO show completion message

            } catch (e: Exception) {
                // TODO show error message
                Timber.e("Export failed ${e.message}")
            }
        }
    }

    private fun getJsonStringFromUri(uri: Uri): String {
        val inputStream = mApplication.contentResolver.openInputStream(uri)
        val bufferedReader = inputStream!!.bufferedReader()
        return bufferedReader.use { it.readLine() }
    }

    fun doneSave() {
        _startJsonSave.value = null
    }

    fun doneShowingMessages() {
        _jsonSaveStatus.value = null
    }

    fun deleteEverything() {
        viewModelScope.launch {
            repository.deleteAllIngredient()
            repository.deleteAllCocktail()
            repository.deleteAllRef()
        }
    }

    fun resetApp() {
        viewModelScope.launch {
            repository.deleteAllIngredient()
            repository.deleteAllCocktail()
            repository.deleteAllRef()

            repository.insertAll()
        }
    }
}