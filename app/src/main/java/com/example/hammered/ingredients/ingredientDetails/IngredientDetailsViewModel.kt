package com.example.hammered.ingredients.ingredientDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hammered.cocktail.CocktailData
import com.example.hammered.database.CocktailDatabase
import com.example.hammered.entities.Ingredient
import com.example.hammered.repository.CocktailRepository
import com.example.hammered.wrappers.RefItemWrapper
import kotlinx.coroutines.launch

class IngredientDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CocktailRepository(CocktailDatabase.getDatabase(application))

    private val _cocktailRefLiveData = MutableLiveData<List<RefItemWrapper<CocktailData>>>()

    val cocktailRefLiveData: LiveData<List<RefItemWrapper<CocktailData>>>
        get() = _cocktailRefLiveData

    private val _currentIngredient = MutableLiveData<Ingredient?>()

    val currentIngredient: LiveData<Ingredient?>
        get() = _currentIngredient


    fun getFromIngredient(name: String) {
        viewModelScope.launch {
            val allIngredientRef = mutableListOf<RefItemWrapper<CocktailData>>()
            val allRefFromCocktail = repository.getRefFromIngredient(name)

            for (ref in allRefFromCocktail) {
                val cocktail = repository.getCocktail(ref.cocktail_id)
                val cocktailRef = RefItemWrapper(cocktail.asData(), ref)
                allIngredientRef.add(cocktailRef)
            }
            _cocktailRefLiveData.value = allIngredientRef
        }
    }

    fun setIngredient(ingredient: Ingredient){
        _currentIngredient.value = ingredient
    }


}