package com.example.hammered.cocktail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hammered.database.CocktailDatabase
import com.example.hammered.entities.Cocktail
import com.example.hammered.entities.Ingredient
import com.example.hammered.entities.relations.IngredientCocktailRef
import kotlinx.coroutines.launch

class CocktailViewModel(application: Application) : AndroidViewModel(application) {

    private val _cocktailLiveData = MutableLiveData<List<Cocktail>>()

    val cocktailLiveData: MutableLiveData<List<Cocktail>>
        get() = _cocktailLiveData

    private val database = CocktailDatabase.getDatabase(application)


    init {
        test()
    }

    // TODO make a file containing constants to reference the chip id/names
    fun checkedData(id: Int) {
        viewModelScope.launch {
            when (id) {
                1 -> _cocktailLiveData.value = database.cocktailDao.getAllCocktails()
                2 -> _cocktailLiveData.value = database.cocktailDao.getFavouriteCocktails()
                3 -> _cocktailLiveData.value = database.cocktailDao.getAllCocktails()
            }
        }

    }

// Test

    fun test() {

        viewModelScope.launch {
            insert()
        }
    }

    suspend fun insert() {
        if (database.cocktailDao.getIngredientCocktailRefCount() == 0) {
            val ing1 = Ingredient("Lemon", "Nice", "lemon.png", false)
            val ing2 = Ingredient("Salt", "Very nice", "salt.png", false)
            val ing3 = Ingredient("Water", "Nice", "water.jpg", false)
            val ing4 = Ingredient("Gin", "Nice", "gin.png", true)
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
    }
    //End test

}