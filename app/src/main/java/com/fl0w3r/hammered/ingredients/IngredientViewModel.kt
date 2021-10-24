package com.fl0w3r.hammered.ingredients

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fl0w3r.hammered.Constants
import com.fl0w3r.hammered.database.CocktailDatabase
import com.fl0w3r.hammered.entities.relations.IngredientWithCocktail
import com.fl0w3r.hammered.repository.CocktailRepository
import kotlinx.coroutines.launch

class IngredientViewModel(application: Application) : AndroidViewModel(application) {

    private val _ingredientData = MutableLiveData<List<IngredientWithCocktail>?>()

    val ingredientData: LiveData<List<IngredientWithCocktail>?>
        get() = _ingredientData

    private val database = CocktailDatabase.getDatabase(application)
    private val repository = CocktailRepository(database)

    fun checkChanged(currVal: IngredientWithCocktail, valueFrom: Int) {
        viewModelScope.launch {
            if (valueFrom == Constants.ITEM_IN_STOCK) {
                currVal.ingredient.inStock = !currVal.ingredient.inStock
                repository.updateIngredient(currVal.ingredient)
            }
            else if (valueFrom == Constants.ITEM_IN_CART) {
                currVal.ingredient.inCart = !currVal.ingredient.inCart
                repository.updateIngredient(currVal.ingredient)
            }
        }
    }

    fun checkedData(id: Int) {
        viewModelScope.launch {
            when (id) {
                Constants.NORMAL_ITEM -> _ingredientData.value =
                    repository.getAllCocktailsFromIngredient()
                Constants.ITEM_IN_STOCK -> _ingredientData.value =
                    repository.getInStockCocktailsFromIngredient()
                Constants.ITEM_IN_CART -> _ingredientData.value =
                    repository.getInCartCocktailsFromIngredient()
            }
        }

    }

}