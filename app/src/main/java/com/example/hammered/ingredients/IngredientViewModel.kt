package com.example.hammered.ingredients

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hammered.database.CocktailDatabase
import com.example.hammered.entities.relations.IngredientWithCocktail
import com.example.hammered.repository.CocktailRepository
import kotlinx.coroutines.launch
import timber.log.Timber

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
            if (valueFrom == 1) {
                currVal.ingredient.inStock = !currVal.ingredient.inStock
                Timber.e("Updated stock status ${currVal.ingredient}")
                repository.updateIngredient(currVal.ingredient)
            }
            else if (valueFrom == 3) {
                currVal.ingredient.inCart = !currVal.ingredient.inCart
                Timber.e("Updated cart status ${currVal.ingredient}")
                repository.updateIngredient(currVal.ingredient)
            }
        }
    }

    fun checkedData(id: Int) {
        viewModelScope.launch {
            when (id) {
                1 -> _ingredientData.value = repository.getAllCocktailsFromIngredient()
                2 -> _ingredientData.value = repository.getInStockCocktailsFromIngredient()
                3 -> _ingredientData.value = repository.getInCartCocktailsFromIngredient()
            }
        }

    }

}