package com.fl0w3r.hammered.mixer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fl0w3r.hammered.Constants
import com.fl0w3r.hammered.cocktail.CocktailData
import com.fl0w3r.hammered.database.CocktailDatabase
import com.fl0w3r.hammered.datastore.SettingsPreferences
import com.fl0w3r.hammered.entities.Cocktail
import com.fl0w3r.hammered.repository.CocktailRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MixerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CocktailRepository(CocktailDatabase.getDatabase(application))
    private val preference = SettingsPreferences(application)

    private val _ingredientList = MutableLiveData<List<IngredientMixerItem>>()
    val ingredientList: LiveData<List<IngredientMixerItem>>
        get() = _ingredientList

    private val _cocktailList = MutableLiveData<List<Cocktail?>>()
    val cocktailList: LiveData<List<Cocktail?>>
        get() = _cocktailList

    private val _selectedIngredientList = MutableLiveData<List<Long>>()
    val selectedIngredientList: LiveData<List<Long>>
        get() = _selectedIngredientList

    private val _currentMixerOption = preference.currentMixerOptionSelection

    init {
        getIngredients()
        _selectedIngredientList.value = listOf()
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

    fun getCocktails(selectedIngredients: List<Long>) {
        viewModelScope.launch {
            _currentMixerOption.collect {
                when (it) {
                    Constants.SELECT_ALL -> {
                        _cocktailList.value =
                            repository.getRefFromIngredientId(selectedIngredients).map { ref ->
                                repository.getCocktail(ref.cocktail_id)!!
                            }
                    }
                    else -> {
                        val cocktailIngList = repository.getAllCocktailWithIngredient()
                        val collectionCocktailList = mutableListOf<Cocktail>()
                        for (cocktailWithIngredient in cocktailIngList) {
                            val idList =
                                cocktailWithIngredient.ingredients.map { mIngredients -> mIngredients.ingredient_id }
                            if (selectedIngredients.isNotEmpty()) {
                                if (idList.containsAll(selectedIngredients)) {
                                    collectionCocktailList.add(cocktailWithIngredient.cocktail)
                                }
                            }
                        }
                        _cocktailList.value = collectionCocktailList
                    }
                }
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

            val mList = _selectedIngredientList.value?.toMutableList()


            if (ingredient.isSelected) {

                if (mList != null) {
                    mList.remove(ingredient.id)
                    _selectedIngredientList.value = mList!!
                }
            } else {
                mList?.add(ingredient.id)
                if (mList != null) {
                    _selectedIngredientList.value = mList!!
                }
            }

        }
    }

}