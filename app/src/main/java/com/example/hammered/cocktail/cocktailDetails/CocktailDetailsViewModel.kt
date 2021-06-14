package com.example.hammered.cocktail.cocktailDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hammered.database.CocktailDatabase
import com.example.hammered.entities.Cocktail
import com.example.hammered.ingredients.IngredientData
import com.example.hammered.repository.CocktailRepository
import com.example.hammered.wrappers.RefItemWrapper
import kotlinx.coroutines.launch

class CocktailDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CocktailRepository(CocktailDatabase.getDatabase(application))

    private val _ingredientRefLiveData = MutableLiveData<List<RefItemWrapper<IngredientData>>>()

    val ingredientRefLiveData: LiveData<List<RefItemWrapper<IngredientData>>>
        get() = _ingredientRefLiveData

    private val _currentCocktail = MutableLiveData<Cocktail?>()

    val currentCocktail: LiveData<Cocktail?>
        get() = _currentCocktail



    fun getFromCocktail(id: Long) {
        viewModelScope.launch {
            val allIngredientRef = mutableListOf<RefItemWrapper<IngredientData>>()
            val allRefFromCocktail = repository.getRefFromCocktail(id)

            for (ref in allRefFromCocktail) {
                val ingredient = repository.getIngredient(ref.ingredient_name)
                val ingredientRef = RefItemWrapper(ingredient.asIngredientData(), ref)
                allIngredientRef.add(ingredientRef)
            }
            _ingredientRefLiveData.value = allIngredientRef
        }
    }

    fun setCocktail(cocktail: Cocktail){
        _currentCocktail.value = cocktail
    }

    fun changeFavourite() {
        viewModelScope.launch {
            val cocktail = _currentCocktail.value
            cocktail!!.isFavorite = !cocktail.isFavorite
            repository.updateCocktail(cocktail)
            _currentCocktail.value = cocktail
        }
    }
}