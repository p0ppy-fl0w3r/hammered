package com.example.hammered.repository

import com.example.hammered.database.CocktailDatabase
import com.example.hammered.entities.Cocktail
import com.example.hammered.entities.Ingredient
import com.example.hammered.entities.relations.IngredientCocktailRef
import com.example.hammered.entities.relations.IngredientWithCocktail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class CocktailRepository(private val database: CocktailDatabase) {

    suspend fun insertAll() {
        withContext(Dispatchers.IO) {
            if (database.cocktailDao.getIngredientCocktailRefCount() == 0) {
                Timber.e("Insert started")
                val ing1 = Ingredient("Lemon", "Nice", "lemon.png", true, false)
                val ing2 = Ingredient("Salt", "Very nice", "salt.png", true, false)
                val ing3 = Ingredient("Water", "Nice", "water.jpg", true, false)
                val ing4 = Ingredient("Gin", "Nice", "gin.png", false, true)
                val ing5 = Ingredient("Vodka", "Nice", "vodka.webp", false, true)

                val cok1 = Cocktail(1, "Tonic", "Strong", "1. make it", false, "vodka.webp")
                val cok2 = Cocktail(2, "Bionic", "Light", "1. make it", true, "gin.png")
                val cok3 = Cocktail(3, "Ronic", "Strong", "1. Break it", true, "water.jpg")

                for (i in arrayOf(ing1, ing2, ing3, ing4, ing5)) {
                    database.cocktailDao.insertIngredient(i)
                }

                for (i in arrayOf(cok1, cok2, cok3)) {
                    database.cocktailDao.insertCocktail(i)
                }

                val ref1 = IngredientCocktailRef(1, "Lemon", 1.0f, "oz", false, false)
                val ref2 = IngredientCocktailRef(1, "Salt", 1f, "oz", false, false)
                val ref3 = IngredientCocktailRef(1, "Water", 3f, "oz", false, false)
                val ref4 = IngredientCocktailRef(2, "Gin", 1f, "oz", true, false)
                val ref5 = IngredientCocktailRef(2, "Water", 1f, "oz", false, false)
                val ref6 = IngredientCocktailRef(2, "Vodka", 10f, "gram", false, false)
                val ref7 = IngredientCocktailRef(2, "Lemon", 1f, "oz", true, false)
                val ref8 = IngredientCocktailRef(3, "Lemon", 15f, "kilo", false, false)
                val ref9 =
                    IngredientCocktailRef(3, "Water", 1f, "oz", true, true)
                val ref10 = IngredientCocktailRef(3, "Salt", 1f, "oz", false, false)

                for (i in arrayOf(
                    ref1,
                    ref2,
                    ref3,
                    ref4,
                    ref5,
                    ref6,
                    ref7,
                    ref8,
                    ref9,
                    ref10
                )) {
                    database.cocktailDao.insertIngredientCocktailRef(i)
                }
            }
            Timber.e("data inserted")
        }
    }

    suspend fun getIngredient(name: String): Ingredient {
        return database.cocktailDao.getIngredient(name)
    }

    suspend fun getCocktail(id:Long):Cocktail{
        return database.cocktailDao.getCocktail(id)
    }

    suspend fun getRefFromCocktail(id: Long): List<IngredientCocktailRef> {
        return database.cocktailDao.getRefFromCocktail(id)
    }

    suspend fun getRefFromIngredient(name: String): List<IngredientCocktailRef>{
        return database.cocktailDao.getRefFromIngredient(name)
    }

    suspend fun updateCocktail(cocktail: Cocktail) {
        database.cocktailDao.updateCocktail(cocktail)
    }

    suspend fun updateIngredient(ingredient: Ingredient) {
        database.cocktailDao.updateIngredient(ingredient)
    }

    suspend fun getAllCocktailsFromIngredient(): List<IngredientWithCocktail> {
        return database.cocktailDao.getAllCocktailsFromIngredient()
    }

    suspend fun getInStockCocktailsFromIngredient(): List<IngredientWithCocktail> {
        return database.cocktailDao.getInStockCocktailsFromIngredient()
    }

    suspend fun getInCartCocktailsFromIngredient(): List<IngredientWithCocktail> {
        return database.cocktailDao.getInCartCocktailsFromIngredient()
    }

}