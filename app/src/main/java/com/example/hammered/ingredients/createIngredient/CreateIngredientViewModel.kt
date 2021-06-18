package com.example.hammered.ingredients.createIngredient

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hammered.database.CocktailDatabase
import com.example.hammered.entities.Ingredient
import com.example.hammered.repository.CocktailRepository
import kotlinx.coroutines.launch

class CreateIngredientViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CocktailRepository(CocktailDatabase.getDatabase(application))

    private val _newIngredient = MutableLiveData<Ingredient?>()

    private val _ingredientExists = MutableLiveData<Boolean?>()

    val ingredientExists: LiveData<Boolean?>
        get() = _ingredientExists

    val newIngredient: LiveData<Ingredient?>
        get() = _newIngredient

    private fun addIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            repository.insertIngredient(ingredient)
            _newIngredient.value = ingredient
        }
    }

    fun checkIngredient(ingredient: Ingredient) {
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

    fun checkedIngredient(){
        _ingredientExists.value = null
    }

    fun doneAddingIngredient() {
        _newIngredient.value = null
    }


}