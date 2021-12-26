package com.fl0w3r.hammered.mixer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fl0w3r.hammered.database.CocktailDatabase
import com.fl0w3r.hammered.repository.CocktailRepository
import kotlinx.coroutines.launch

class MixerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CocktailRepository(CocktailDatabase.getDatabase(application))

    private val _ingredientList = MutableLiveData<List<IngredientMixerItem>>()
    val ingredientList: LiveData<List<IngredientMixerItem>>
        get() = _ingredientList

    init {
        getIngredients()
    }

    private fun getIngredients() {
        viewModelScope.launch {
            _ingredientList.value = repository.getAllIngredient().map {
                IngredientMixerItem(
                    ingredientName = it.ingredient_name,
                    ingredientImage = it.ingredient_image,
                    id = it.ingredient_id
                )
            }
        }
    }

    fun setIngredientState(ingredient: IngredientMixerItem) {
        viewModelScope.launch {
            _ingredientList.value = _ingredientList.value?.map {
                IngredientMixerItem(
                    ingredientName = it.ingredientName,
                    ingredientImage = it.ingredientImage,
                    id = it.id,
                    isSelected = if (ingredient.id == it.id) !ingredient.isSelected else it.isSelected
                )
            }
        }
    }

}