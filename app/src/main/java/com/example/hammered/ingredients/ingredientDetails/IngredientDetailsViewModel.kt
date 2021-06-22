package com.example.hammered.ingredients.ingredientDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hammered.database.CocktailDatabase
import com.example.hammered.entities.Ingredient
import com.example.hammered.entities.relations.CocktailWithIngredient
import com.example.hammered.repository.CocktailRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class IngredientDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CocktailRepository(CocktailDatabase.getDatabase(application))

    private val _cocktailRefLiveData = MutableLiveData<List<CocktailWithIngredient>>()

    val cocktailRefLiveData: LiveData<List<CocktailWithIngredient>>
        get() = _cocktailRefLiveData

    private val _currentIngredient = MutableLiveData<Ingredient?>()

    val currentIngredient: LiveData<Ingredient?>
        get() = _currentIngredient


    fun getFromIngredient(name: String) {
        viewModelScope.launch {
            val allIngredientRef = mutableListOf<CocktailWithIngredient>()
            val allRefFromCocktail = repository.getRefFromIngredient(name)

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
            _currentIngredient.value = repository.getIngredient(ingredient.ingredient_name)
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


}