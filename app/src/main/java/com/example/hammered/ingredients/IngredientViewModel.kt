package com.example.hammered.ingredients

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hammered.Constants
import com.example.hammered.database.CocktailDatabase
import com.example.hammered.entities.relations.IngredientWithCocktail
import com.example.hammered.repository.CocktailRepository
import kotlinx.coroutines.launch

class IngredientViewModel(application: Application) : AndroidViewModel(application) {

    private val _ingredientData = MutableLiveData<List<IngredientWithCocktail>?>()

    val ingredientData: LiveData<List<IngredientWithCocktail>?>
        get() = _ingredientData

    private val database = CocktailDatabase.getDatabase(application)
    private val repository = CocktailRepository(database)

    init {
        viewModelScope.launch {
            repository.insertAll()
        }
    }

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