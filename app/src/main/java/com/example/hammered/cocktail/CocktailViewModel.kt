package com.example.hammered.cocktail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hammered.database.CocktailDatabase
import com.example.hammered.entities.Cocktail
import com.example.hammered.entities.Ingredient
import com.example.hammered.entities.relations.CocktailWithIngredient
import com.example.hammered.entities.relations.IngredientCocktailRef
import kotlinx.coroutines.launch
import timber.log.Timber

class CocktailViewModel(application: Application) : AndroidViewModel(application) {


    private val _cocktailLiveData = MutableLiveData<List<CocktailWithIngredient>?>()

    val cocktailLiveData: LiveData<List<CocktailWithIngredient>?>
        get() = _cocktailLiveData

    private val database = CocktailDatabase.getDatabase(application)


    // TODO make a file containing constants to reference the chip id/names
    // TODO find an alternative for insert
    fun checkedData(id: Int) {
        viewModelScope.launch {
            insert()
            when (id) {
                1 -> _cocktailLiveData.value = database.cocktailDao.getAllIngredientFromCocktail()
                2 -> _cocktailLiveData.value =
                    database.cocktailDao.getFavouriteIngredientFromCocktail()
                3 -> _cocktailLiveData.value = filterMakableDrinks()
            }
            Timber.e("Databases updated form $id")
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


    //TEST
    suspend fun insert() {
        if (database.cocktailDao.getIngredientCocktailRefCount() == 0) {
            val ing1 = Ingredient("Lemon", "Nice", "lemon.png", true)
            val ing2 = Ingredient("Salt", "Very nice", "salt.png", true)
            val ing3 = Ingredient("Water", "Nice", "water.jpg", true)
            val ing4 = Ingredient("Gin", "Nice", "gin.png", false)
            val ing5 = Ingredient("Vodka", "Nice", "vodka.webp", false)

            val cok1 = Cocktail(1, "Tonic", "Strong", "1. make it", false, "vodka.webp")
            val cok2 = Cocktail(2, "Bionic", "Light", "1. make it", true, "gin.png")

            for (i in arrayOf(ing1, ing2, ing3, ing4, ing5)) {
                database.cocktailDao.insertIngredient(i)
            }

            for (i in arrayOf(cok1, cok2)) {
                database.cocktailDao.insertCocktail(i)
            }

            val ref1 = IngredientCocktailRef(1, "Lemon")
            val ref2 = IngredientCocktailRef(1, "Salt")
            val ref3 = IngredientCocktailRef(1, "Water")
            val ref4 = IngredientCocktailRef(2, "Gin")
            val ref5 = IngredientCocktailRef(2, "Water")
            val ref6 = IngredientCocktailRef(2, "Vodka")
            val ref7 = IngredientCocktailRef(2, "Lemon")



            for (i in arrayOf(
                ref1,
                ref2,
                ref3,
                ref4,
                ref5,
                ref6,
                ref7
            )) {
                database.cocktailDao.insertIngredientCocktailRef(i)
            }
        }
        Timber.e("data inserted")
    }


    //End test

}