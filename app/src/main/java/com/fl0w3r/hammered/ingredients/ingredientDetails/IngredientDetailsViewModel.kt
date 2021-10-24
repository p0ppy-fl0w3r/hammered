package com.fl0w3r.hammered.ingredients.ingredientDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fl0w3r.hammered.database.CocktailDatabase
import com.fl0w3r.hammered.entities.Ingredient
import com.fl0w3r.hammered.entities.relations.CocktailWithIngredient
import com.fl0w3r.hammered.repository.CocktailRepository
import kotlinx.coroutines.launch

class IngredientDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CocktailRepository(CocktailDatabase.getDatabase(application))

    private val _cocktailRefLiveData = MutableLiveData<List<CocktailWithIngredient>>()

    val cocktailRefLiveData: LiveData<List<CocktailWithIngredient>>
        get() = _cocktailRefLiveData

    private val _currentIngredient = MutableLiveData<Ingredient?>()

    val currentIngredient: LiveData<Ingredient?>
        get() = _currentIngredient

    private val _ingredientDeleted = MutableLiveData<Boolean?>()

    val ingredientDeleted : LiveData<Boolean?>
        get() = _ingredientDeleted

    fun getFromIngredient(id: Long) {
        viewModelScope.launch {
            val allIngredientRef = mutableListOf<CocktailWithIngredient>()
            val allRefFromCocktail = repository.getRefFromIngredientId(id)

            for (ref in allRefFromCocktail) {
                val cocktailWithIngredient = repository.getCocktailWithIngredient(ref.cocktail_id)
                allIngredientRef.add(cocktailWithIngredient)
            }
            _cocktailRefLiveData.value = allIngredientRef
        }
    }

    fun setIngredient(ingredient: Ingredient) {
        _currentIngredient.value = ingredient

        viewModelScope.launch {
            _currentIngredient.value = repository.getIngredient(ingredient.ingredient_id)
        }
    }

    fun changeInCart() {
        viewModelScope.launch {
            val mIngredient = _currentIngredient.value!!
            mIngredient.inCart = !mIngredient.inCart
            repository.updateIngredient(mIngredient)
            _currentIngredient.value = mIngredient
        }
    }

    fun changeInStock() {
        viewModelScope.launch {
            val mIngredient = _currentIngredient.value!!
            repository.updateIngredient(mIngredient)
            _currentIngredient.value = mIngredient
        }
    }

    fun deleteCurrentIngredient(){
        viewModelScope.launch {
            currentIngredient.value?.let {

                repository.deleteIngredient(it)
                repository.deleteAllRefOfIngredient(it.ingredient_id)

                _ingredientDeleted.value = true
            }
        }
    }

    fun doneDeleting(){
        _ingredientDeleted.value = null
    }

    fun copyAndEditIngredient(){}
}