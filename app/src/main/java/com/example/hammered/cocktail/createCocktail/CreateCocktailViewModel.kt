package com.example.hammered.cocktail.createCocktail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hammered.database.CocktailDatabase
import com.example.hammered.entities.Cocktail
import com.example.hammered.entities.relations.IngredientCocktailRef
import com.example.hammered.repository.CocktailRepository
import com.example.hammered.wrappers.NewCocktailRef
import com.example.hammered.wrappers.StepsWrapper
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

    private val _ingredientValid = MutableLiveData<Int>()

    val ingredientValid: LiveData<Int>
        get() = _ingredientValid

    private val _saveCocktail = MutableLiveData<Boolean?>()

    val saveCocktail: LiveData<Boolean?>
        get() = _saveCocktail

    private val repository = CocktailRepository(CocktailDatabase.getDatabase(application))

    private val _cocktailId = MutableLiveData<Long>()

    init {
        val newIngredientList = mutableListOf(NewCocktailRef(ingredient_name = "Gin", quantity = "12"))
        val newStepsList = mutableListOf(StepsWrapper(0, "Add water"))

        _ingredientList.value = newIngredientList
        _stepsList.value = newStepsList
        getLastCocktailId()
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
            Timber.e("Looks like the list was empty or null $e")
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
            throw   IllegalArgumentException("No such ingredient for reference number $ref_number")
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
            Timber.e("Looks like the steps list was empty or null $e")
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
        checkIngredient()
    }

    // TODO add the check values to constants
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
            _stepsValid.value = -2
        }
        else {
            _stepsValid.value = -1
        }
    }

    private fun checkIngredient() {
        val allIngredient = _ingredientList.value
        if (!allIngredient.isNullOrEmpty()) {
            for (ingredients in allIngredient.withIndex()) {
                if (ingredients.value.ingredient_name.isBlank() || ingredients.value.quantity.isBlank()) {
                    _ingredientValid.value = ingredients.index
                    return
                }
                else {
                    val quantity = ingredients.value.quantity.toFloat()
                    if (quantity == 0f) {
                        _ingredientValid.value = ingredients.index
                        return
                    }
                }
            }
            _ingredientValid.value = -2
        }
        else {
            _ingredientValid.value = -1
        }
    }

    fun setCocktailChecked() {
        if (_ingredientValid.value == -2 && _stepsValid.value == -2) {
            _saveCocktail.value = true
        }
        else {
            _saveCocktail.value = null
        }
    }

    fun doneChecking() {
        _saveCocktail.value = null
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
                    // TODO transform steps to string
                    steps = _stepsList.value.toString()
                )
            }
            if (newCocktail != null) {
                repository.insertCocktail(newCocktail)
            }
            val listCocktailIngredientRef = mutableListOf<IngredientCocktailRef>()

            for (ingredients in _ingredientList.value!!) {
                if (newCocktail != null) {
                    listCocktailIngredientRef.add(ingredients.toIngredientCocktailRef(newCocktail.cocktail_id))
                }
            }

            for(ingredientRef in listCocktailIngredientRef){
                repository.insertIngredientCocktailRef(ingredientRef)
            }

            doneChecking()
        }

    }

    private fun getLastCocktailId(){
        viewModelScope.launch {
            val cocktailId = repository.getLastCocktailId()
            _cocktailId.value = cocktailId
            Timber.e("The cocktail id is $cocktailId")
        }
    }

}