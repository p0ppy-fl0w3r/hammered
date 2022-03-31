package com.fl0w3r.hammered.settings

import android.app.Application
import android.content.ContentValues
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.*
import com.fl0w3r.hammered.Constants
import com.fl0w3r.hammered.database.CocktailDatabase
import com.fl0w3r.hammered.datastore.SettingsPreferences
import com.fl0w3r.hammered.entities.Cocktail
import com.fl0w3r.hammered.entities.Ingredient
import com.fl0w3r.hammered.entities.relations.IngredientCocktailRef
import com.fl0w3r.hammered.repository.CocktailRepository
import com.fl0w3r.hammered.utils.JsonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.io.IOUtils
import timber.log.Timber
import java.io.File
import java.util.*
import kotlin.io.path.Path


class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = SettingsPreferences(application)
    private val repository = CocktailRepository(CocktailDatabase.getDatabase(application))

    private val mApplication = application

    val currentStartupScreen = preferences.currentStartupScreen.asLiveData()

    val currentMixerOption = preferences.currentMixerOptionSelection.asLiveData()

    val astStatus  = preferences.asrStatus.asLiveData()

    private val _startJsonSave = MutableLiveData<Boolean?>()
    val startJsonSave: LiveData<Boolean?>
        get() = _startJsonSave

    private val _startImport = MutableLiveData<Boolean?>()
    val startImport: LiveData<Boolean?>
        get() = _startImport

    // See Constants.kt for status codes.
    private val _jsonSaveStatus = MutableLiveData<Int?>()
    val jsonSaveStatus: LiveData<Int?>
        get() = _jsonSaveStatus

    private val _importStatus = MutableLiveData<Int?>()
    val importStatus: LiveData<Int?>
        get() = _importStatus

    fun changeStartupScreen(position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            preferences.changeStartupScreen(position)
        }
    }

    fun changeMixerOption(option: String) {
        viewModelScope.launch(Dispatchers.IO) {
            preferences.changeMixerOption(option)
        }
    }

    fun changeAsrStatus(status: Boolean){
        viewModelScope.launch {
            preferences.asrStatus(status)
        }
    }

    @SuppressWarnings("deprecation")
    fun saveToJson() {

        _startJsonSave.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val allIngredients = repository.getAllIngredient()
            val allCocktail = repository.getALlCocktail()
            val allRef = repository.getAllIngredientCocktailRef()

            val ingredientJson = JsonUtils.getJsonFromClass(allIngredients)
            val cocktailJson = JsonUtils.getJsonFromClass(allCocktail)
            val refJson = JsonUtils.getJsonFromClass(allRef)


            if (Build.VERSION.SDK_INT < 29) {

                val dir = Environment
                    .getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS
                    )?.absolutePath
                if (dir != null) {
                    if (dir.isNotBlank()) {
                        try {
                            val exportDir =
                                File(
                                    dir,
                                    "hammered_export ${
                                        SimpleDateFormat(
                                            "HH:mm-yyyy-mm-dd",
                                            Locale.US
                                        ).format(System.currentTimeMillis())
                                    }"
                                )

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
            } else {
                val resolver = mApplication.contentResolver
                val directoryName = "hammered_export ${
                    SimpleDateFormat(
                        "HH:mm-yyyy-mm-dd",
                        Locale.US
                    ).format(System.currentTimeMillis())
                }"


                val ingredientValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, Constants.INGREDIENT_JSON_FILE)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/json")
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH, Path(
                            Environment.DIRECTORY_DOWNLOADS, directoryName
                        ).toString()
                    )
                }

                val cocktailValue = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, Constants.COCKTAIL_JSON_FILE)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/json")
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH, Path(
                            Environment.DIRECTORY_DOWNLOADS, directoryName
                        ).toString()
                    )
                }

                val refValue = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, Constants.BRIDGING_JSON_FILE)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/json")
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH, Path(
                            Environment.DIRECTORY_DOWNLOADS, directoryName
                        ).toString()
                    )
                }

                val ingredientUri =
                    resolver.insert(MediaStore.Files.getContentUri("external"), ingredientValues)
                val cocktailUri =
                    resolver.insert(MediaStore.Files.getContentUri("external"), cocktailValue)
                val refUri =
                    resolver.insert(MediaStore.Files.getContentUri("external"), refValue)


                if (ingredientUri == null || cocktailUri == null || refUri == null) {
                    _jsonSaveStatus.postValue(Constants.FILE_CREATION_FAILED)
                    return@launch
                }

                resolver.openOutputStream(ingredientUri).use {
                    it?.write(ingredientJson.encodeToByteArray())
                    it?.close()
                }

                resolver.openOutputStream(cocktailUri).use {
                    it?.write(cocktailJson.encodeToByteArray())
                    it?.close()
                }

                resolver.openOutputStream(refUri).use {
                    it?.write(refJson.encodeToByteArray())
                    it?.close()
                }

                _jsonSaveStatus.postValue(Constants.DATA_SAVE_SUCCESS)

            }

        }

    }

    fun getFromJson(dataDir: Uri, ignoreNew: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            // Import started
            _startImport.postValue(true)

            val exportTree = DocumentFile.fromTreeUri(getApplication(), dataDir)

            if (exportTree == null) {
                _importStatus.postValue(Constants.FOLDER_INVALID)
                return@launch
            }

            val fileList = exportTree.listFiles().toMutableList()

            var jCocktailFile: DocumentFile? = null
            var jRefFile: DocumentFile? = null
            var jIngredientFile: DocumentFile? = null

            for (i in fileList) {
                when (i.name) {
                    Constants.INGREDIENT_JSON_FILE -> jIngredientFile = i
                    Constants.COCKTAIL_JSON_FILE -> jCocktailFile = i
                    Constants.BRIDGING_JSON_FILE -> jRefFile = i
                }
            }

            if (jCocktailFile == null || jIngredientFile == null || jRefFile == null) {
                // Not a valid export folder
                Timber.e("The import folder is invalid.")
                _importStatus.postValue(Constants.FOLDER_INVALID)
                return@launch
            }

            try {

                val cocktailDataList =
                    JsonUtils.getClassFromJson<Cocktail>(getJsonStringFromUri(jCocktailFile.uri))
                val ingredientDataList =
                    JsonUtils.getClassFromJson<Ingredient>(getJsonStringFromUri(jIngredientFile.uri))
                val refDataList =
                    JsonUtils.getClassFromJson<IngredientCocktailRef>(getJsonStringFromUri(jRefFile.uri))


                ingredientDataList?.forEach {
                    if (ignoreNew) {
                        repository.ignoreInsertIngredient(it)
                    } else {
                        repository.insertIngredient(it)
                    }
                }

                cocktailDataList?.forEach {
                    if (ignoreNew) {
                        repository.ignoreInsertCocktail(it)
                    } else {
                        repository.insertCocktail(it)
                    }
                }

                refDataList?.forEach {
                    if (ignoreNew) {
                        repository.ignoreInsertIngredientCocktailRef(it)
                    } else {
                        repository.insertIngredientCocktailRef(it)
                    }
                }
                _importStatus.postValue(Constants.IMPORT_SUCCESS)

            } catch (e: Exception) {
                Timber.e("Import failed ${e.message}")
                _importStatus.postValue(Constants.IMPORT_FAILED)
            }
        }
    }

    private fun getJsonStringFromUri(uri: Uri): String {
        val inputStream = mApplication.contentResolver.openInputStream(uri)

        return inputStream.use { IOUtils.toString(it) }
    }

    fun doneSave() {
        _startJsonSave.value = null
    }

    fun doneImport() {
        _startImport.value = null
    }

    fun doneShowingMessages() {
        _jsonSaveStatus.value = null
    }

    fun doneImportMessages() {
        _importStatus.value = null
    }

    fun deleteEverything() {
        viewModelScope.launch {
            repository.deleteAllIngredient()
            repository.deleteAllCocktail()
            repository.deleteAllRef()
        }
    }

    fun resetApp(
        ingredients: List<Ingredient>,
        cocktails: List<Cocktail>,
        references: List<IngredientCocktailRef>
    ) {
        viewModelScope.launch {
            deleteEverything()

            repository.insertInitialValues(ingredients, cocktails, references)
        }
    }
}