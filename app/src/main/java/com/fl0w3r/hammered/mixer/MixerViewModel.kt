package com.fl0w3r.hammered.mixer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fl0w3r.hammered.cocktail.CocktailData
import com.fl0w3r.hammered.database.CocktailDatabase
import com.fl0w3r.hammered.repository.CocktailRepository
import kotlinx.coroutines.launch

class MixerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CocktailRepository(CocktailDatabase.getDatabase(application))

    private val _ingredientList = MutableLiveData<List<IngredientMixerItem>>()
    val ingredientList: LiveData<List<IngredientMixerItem>>
        get() = _ingredientList

    private val _cocktailList = MutableLiveData<List<CocktailData?>>()
    val cocktailList: LiveData<List<CocktailData?>>
        get() = _cocktailList

    private val _selectedIngredientList = MutableLiveData<List<Long>>()
    val selectedIngredientList : LiveData<List<Long>>
        get() = _selectedIngredientList

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

    fun getCocktails(selectedIngredients: List<Long>){
        viewModelScope.launch {
            _cocktailList.value = repository.getRefFromIngredientId(selectedIngredients).map {
                repository.getCocktail(it.cocktail_id)!!.asData()
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


            if (ingredient.isSelected){

                if (mList != null){
                    mList.remove(ingredient.id)
                    _selectedIngredientList.value = mList!!
                }
            }
            else{
                mList?.add(ingredient.id)
                if (mList != null){
                    _selectedIngredientList.value = mList!!
                }
            }

        }
    }

}