package com.fl0w3r.hammered.cocktail.createCocktail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fl0w3r.hammered.Constants
import com.fl0w3r.hammered.database.CocktailDatabase
import com.fl0w3r.hammered.entities.Cocktail
import com.fl0w3r.hammered.entities.Ingredient
import com.fl0w3r.hammered.entities.relations.IngredientCocktailRef
import com.fl0w3r.hammered.repository.CocktailRepository
import com.fl0w3r.hammered.wrappers.NewCocktailRef
import com.fl0w3r.hammered.wrappers.StepsWrapper
import kotlinx.coroutines.launch
import timber.log.Timber

class CreateCocktailViewModel(application: Application) : AndroidViewModel(application) {

    private val _ingredientList = MutableLiveData<MutableList<NewCocktailRef>>()

    val ingredientList: LiveData<MutableList<NewCocktailRef>>
        get() = _ingredientList

    private val _stepsList = MutableLiveData<MutableList<StepsWrapper>?>()
    val stepsList: LiveData<MutableList<StepsWrapper>?>
        get() = _stepsList

    private val _stepsValid = MutableLiveData<Int>()

    val stepsValid: LiveData<Int>
        get() = _stepsValid

    // The error code convention is errorCode, Index eg:- (-2, 0)
    private val _ingredientValid = MutableLiveData<List<Int>>()

    val ingredientValid: LiveData<List<Int>>
        get() = _ingredientValid

    private val _saveCocktail = MutableLiveData<Boolean?>()

    val saveCocktail: LiveData<Boolean?>
        get() = _saveCocktail

    private val _editIngredientList = MutableLiveData<List<IngredientCocktailRef>>()
    val editIngredientList: LiveData<List<IngredientCocktailRef>>
        get() = _editIngredientList

    private val repository = CocktailRepository(CocktailDatabase.getDatabase(application))

    private val _cocktailId = MutableLiveData<Long>()

    private val _allIngredientList = MutableLiveData<List<Ingredient>>()

    val allIngredientList: LiveData<List<Ingredient>>
        get() = _allIngredientList

    private val _finishActivity = MutableLiveData<Boolean?>()

    val finishActivity: LiveData<Boolean?>
        get() = _finishActivity

    init {
        getAllIngredientFromDatabase()

        _ingredientList.value = mutableListOf(NewCocktailRef())
        _stepsList.value = mutableListOf(StepsWrapper())

        getLastCocktailId()
    }

    fun getDataToPopulateFields(id: Long) {
        viewModelScope.launch {
            _editIngredientList.value = repository.getRefFromCocktail(id)
        }
    }

    fun setIngredientList(list: List<NewCocktailRef>) {
        _ingredientList.value = list.toMutableList()
    }

    fun setStepsFromRawString(steps: String) {
        val editStepsList = steps.split("\n").mapIndexed { index, text ->
            StepsWrapper(index, text)
        }
        _stepsList.value = editStepsList.toMutableList()
    }

    private fun getAllIngredientFromDatabase() {
        viewModelScope.launch {
            _allIngredientList.value = repository.getAllIngredient()
        }
    }

    fun addIngredient() {
        try {
            val lastId = _ingredientList.value!!.last().ref_number
            val newIngredient = NewCocktailRef(ref_number = lastId.plus(1))
            val newList = _ingredientList.value!!
            newList.add(newIngredient)
            _ingredientList.value = newList
        }
        catch (e: Exception) {
            Timber.w("Looks like the ingredient list was empty or null $e")
            _ingredientList.value = mutableListOf(
                NewCocktailRef()
            )
        }
    }

    private fun getItemByNumber(ref_number: Int): NewCocktailRef? {
        val allValues = _ingredientList.value
        if (allValues != null) {
            for (items in allValues) {
                if (items.ref_number == ref_number) {
                    return items
                }
            }
        }
        return null
    }

    fun removeIngredient(ref_number: Int) {
        val selectedItem = getItemByNumber(ref_number)
        if (selectedItem != null) {
            val allValues = _ingredientList.value
            if (!allValues.isNullOrEmpty()) {
                allValues.remove(selectedItem)
            }
            _ingredientList.value = allValues!!
        }
        else {
            throw IllegalArgumentException("No such ingredient for reference number $ref_number")
        }
    }

    fun addStep() {
        try {
            val newList = _stepsList.value!!
            val last = newList.last().ref
            val newStep = StepsWrapper(last + 1, "")
            newList.add(newStep)
            _stepsList.value = newList
        }
        catch (e: Exception) {
            Timber.w("Looks like the steps list was empty or null $e")
            _stepsList.value = mutableListOf(
                StepsWrapper(0, "")
            )
        }
    }

    fun removeStep(step: StepsWrapper) {
        val allValue = _stepsList.value
        if (!allValue.isNullOrEmpty()) {
            allValue.remove(step)
            _stepsList.value = allValue
        }
        else {
            throw   IllegalArgumentException("No such step: $step")
        }
    }

    fun validate() {
        checkSteps()

        // Only check the ingredient if the steps are valid
        // Otherwise the animation will play for both steps and ingredient.
        if (_stepsValid.value == Constants.VALUE_OK) {
            checkIngredient()
        }
    }

    private fun checkSteps() {
        val allSteps = _stepsList.value
        if (!allSteps.isNullOrEmpty()) {
            for (steps in allSteps.withIndex()) {
                if (steps.value.steps.isBlank()) {
                    _stepsValid.value = steps.index
                    return
                }
            }
            // The steps are valid
            _stepsValid.value = Constants.VALUE_OK
        }
        else {
            _stepsValid.value = Constants.NO_VALUES
        }
    }

    private fun checkIngredient() {
        viewModelScope.launch {
            val allIngredient = _ingredientList.value
            if (!allIngredient.isNullOrEmpty()) {
                for (ingredients in allIngredient.withIndex()) {
                    // Get the value form the quantity editText
                    var quantity = 0f
                    if (ingredients.value.quantity.isNotBlank()) {
                        quantity = ingredients.value.quantity.toFloat()
                    }

                    val ingredientFromDatabase =
                        repository.getIngredient(ingredients.value.ingredient_name)

                    // The ingredient name field was left blank
                    if (ingredients.value.ingredient_name.isBlank()) {
                        _ingredientValid.value =
                            listOf(Constants.INGREDIENT_NAME_EMPTY, ingredients.index)
                        return@launch
                    }
                    // The quantity field was empty or 0
                    if (quantity == 0f) {
                        _ingredientValid.value =
                            listOf(Constants.QUANTITY_FIELD_EMPTY, ingredients.index)
                        return@launch
                    }
                    // There was no such ingredient in the database
                    if (ingredientFromDatabase == null) {
                        _ingredientValid.value =
                            listOf(Constants.NO_INGREDIENT_IN_DATABASE, ingredients.index)
                        return@launch
                    }

                }
                _ingredientValid.value = listOf(Constants.VALUE_OK, 0)
            }
            else {
                _ingredientValid.value = listOf(Constants.NO_VALUES, 0)
            }
        }
    }

    fun setCocktailChecked() {
        if (_ingredientValid.value == listOf(Constants.VALUE_OK, 0)
            && _stepsValid.value == Constants.VALUE_OK
        ) {
            _saveCocktail.value = true
        }
        else {
            _saveCocktail.value = null
        }
    }

    private fun doneChecking() {
        _saveCocktail.value = null
        _finishActivity.value = true
    }

    fun saveCocktail(name: String, description: String, imageUrl: String) {
        viewModelScope.launch {
            val newCocktail = _cocktailId.value?.plus(1)?.let {
                Cocktail(
                    cocktail_id = it,
                    cocktail_name = name,
                    cocktail_description = description,
                    cocktail_image = imageUrl,
                    isFavorite = false,
                    steps = _stepsList.value!!.joinToString(separator = "\n") { value ->
                        value.steps
                    }
                )
            }
            if (newCocktail != null) {
                repository.insertCocktail(newCocktail)
            }
            val listCocktailIngredientRef = mutableListOf<IngredientCocktailRef>()

            for (ingredients in _ingredientList.value!!) {
                if (newCocktail != null) {
                    ingredients.ingredient_id =
                        repository.getIngredient(ingredients.ingredient_name)!!.ingredient_id
                    listCocktailIngredientRef.add(ingredients.toIngredientCocktailRef(newCocktail.cocktail_id))
                }
            }

            for (ingredientRef in listCocktailIngredientRef) {
                repository.insertIngredientCocktailRef(ingredientRef)
            }
            doneChecking()
        }

    }

    fun editCocktail(id: Long, name: String, description: String, imageUrl: String) {
        viewModelScope.launch {
            val newCocktail =
                Cocktail(
                    cocktail_id = id,
                    cocktail_name = name,
                    cocktail_description = description,
                    cocktail_image = imageUrl,
                    isFavorite = false,
                    steps = _stepsList.value!!.joinToString(separator = "\n") { value ->
                        value.steps
                    }
                )

            repository.updateCocktail(newCocktail)

            // Delete the previous IngredientCocktailRef referencing to edited cocktail.
            // This will ensure that the ingredients are removed from the database if the user removes
            // ingredient when editing.
            repository.deleteAllRefOfCocktail(newCocktail.cocktail_id)

            val listCocktailIngredientRef = mutableListOf<IngredientCocktailRef>()

            for (ingredients in _ingredientList.value!!) {
                ingredients.ingredient_id =
                    repository.getIngredient(ingredients.ingredient_name)!!.ingredient_id
                listCocktailIngredientRef.add(ingredients.toIngredientCocktailRef(newCocktail.cocktail_id))
            }

            for (ingredientRef in listCocktailIngredientRef) {
                repository.insertIngredientCocktailRef(ingredientRef)
            }
            doneChecking()
        }
    }

    private fun getLastCocktailId() {
        viewModelScope.launch {
            val cocktailId = repository.getLastCocktailId()
            _cocktailId.value = cocktailId
        }
    }

}