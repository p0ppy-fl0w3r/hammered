package com.fl0w3r.hammered.cocktail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fl0w3r.hammered.Constants
import com.fl0w3r.hammered.database.CocktailDatabase
import com.fl0w3r.hammered.entities.relations.CocktailWithIngredient
import com.fl0w3r.hammered.repository.CocktailRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class CocktailViewModel(application: Application) : AndroidViewModel(application) {


    private val _cocktailLiveData = MutableLiveData<List<CocktailWithIngredient>?>()

    val cocktailLiveData: LiveData<List<CocktailWithIngredient>?>
        get() = _cocktailLiveData

    private val database = CocktailDatabase.getDatabase(application.applicationContext)
    private val repository = CocktailRepository(database)


    fun checkedData(id: Int) {
        viewModelScope.launch {
            when (id) {
                Constants.NORMAL_COCKTAIL_ITEM -> _cocktailLiveData.value =
                    database.cocktailDao.getAllIngredientFromCocktail()
                Constants.AVAILABLE_COCKTAIL_ITEM -> _cocktailLiveData.value = filterMakableDrinks()
                Constants.FAVORITE_COCKTAIL_ITEM -> _cocktailLiveData.value =
                    database.cocktailDao.getFavouriteIngredientWithCocktail()

            }
            Timber.i("Databases updated form $id")
        }

    }

    private suspend fun filterMakableDrinks(): List<CocktailWithIngredient> {

        /**
         * Returns drinks with ingredients if the ingredients are in stock.
         *
         * @return List of CocktailWithIngredients
         */

        val allDrinks = database.cocktailDao.getAllIngredientFromCocktail()
        val makableDrinks: MutableList<CocktailWithIngredient> = mutableListOf()

        for (i in allDrinks) {
            var hasAll = true
            for (j in i.ingredients) {
                if (!j.inStock) {
                    hasAll = false
                    break
                }
            }
            if (hasAll) {
                makableDrinks.add(i)
            }
        }

        return makableDrinks
    }

}