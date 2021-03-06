package com.fl0w3r.hammered.ingredients.createIngredient

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fl0w3r.hammered.database.CocktailDatabase
import com.fl0w3r.hammered.entities.Ingredient
import com.fl0w3r.hammered.repository.CocktailRepository
import kotlinx.coroutines.launch

class CreateIngredientViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CocktailRepository(CocktailDatabase.getDatabase(application))

    private val _newIngredient = MutableLiveData<Ingredient?>()

    // Ingredient that is to be edited.
    private val _currentIngredient = MutableLiveData<Ingredient?>()

    private val _ingredientExists = MutableLiveData<Boolean?>()

    val ingredientExists: LiveData<Boolean?>
        get() = _ingredientExists

    val newIngredient: LiveData<Ingredient?>
        get() = _newIngredient

    val currentIngredient: LiveData<Ingredient?>
        get() = _currentIngredient


    private fun addIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            repository.insertIngredient(ingredient)
            _newIngredient.value = ingredient
        }
    }

    fun getIngredient(id: Long){
        viewModelScope.launch {
            _currentIngredient.value = repository.getIngredient(id)
        }
    }

    fun checkIngredient(ingredient: Ingredient) {
        /**
         * Check and save a new ingredient.
         * @param ingredient the new ingredient that is to be saved in the database.
         **/
        viewModelScope.launch {
            val mIngredient = repository.getIngredient(ingredient.ingredient_name)

            if (mIngredient == null) {
                addIngredient(ingredient)
            }
            else {
                _ingredientExists.value = true
            }
        }
    }

    fun checkIngredient(ingredient: Ingredient, initialName: String) {
        /**
         * Check and save ingredient if you're editing an ingredient that previously existed.
         * @param ingredient new ingredient with updated information.
         * @param initialName initial name of the ingredient.
         **/

        viewModelScope.launch {
            val mIngredient = repository.getIngredient(ingredient.ingredient_name)
            if (mIngredient != null && initialName != mIngredient.ingredient_name) {
                _ingredientExists.value = true
            }
            else {
                addIngredient(ingredient)
            }
        }
    }

    fun checkedIngredient() {
        _ingredientExists.value = null
    }

    fun doneAddingIngredient() {
        _newIngredient.value = null
    }


}