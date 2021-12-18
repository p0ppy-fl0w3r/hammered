package com.fl0w3r.hammered.repository

import com.fl0w3r.hammered.database.CocktailDatabase
import com.fl0w3r.hammered.entities.Cocktail
import com.fl0w3r.hammered.entities.Ingredient
import com.fl0w3r.hammered.entities.relations.CocktailWithIngredient
import com.fl0w3r.hammered.entities.relations.IngredientCocktailRef
import com.fl0w3r.hammered.entities.relations.IngredientWithCocktail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class CocktailRepository(private val database: CocktailDatabase) {


    suspend fun insertAll() {
        withContext(Dispatchers.IO) {
            if (database.cocktailDao.getIngredientCocktailRefCount() == 0) {
                Timber.i("Insert started")
                val ing1 =
                    Ingredient(1, "Lemon", "Nice", "file:///android_asset/lemon.png", true, false)
                val ing2 =
                    Ingredient(
                        2,
                        "Salt",
                        "Very nice",
                        "file:///android_asset/salt.png",
                        true,
                        false
                    )
                val ing3 =
                    Ingredient(3, "Water", "Nice", "file:///android_asset/water.jpg", true, false)
                val ing4 =
                    Ingredient(4, "Gin", "Nice", "file:///android_asset/gin.png", false, true)
                val ing5 =
                    Ingredient(5, "Vodka", "Nice", "file:///android_asset/vodka.webp", false, true)


                val cocktailList: Array<Cocktail> = arrayOf(
                    Cocktail(
                        1,
                        "Tonic",
                        "Strong",
                        "1. make it",
                        false,
                        "file:///android_asset/vodka.webp"
                    ),
                    Cocktail(
                        2,
                        "Bionic",
                        "Light",
                        "1. make it",
                        true,
                        "file:///android_asset/gin.png"
                    ),
                    Cocktail(
                        3,
                        "Ronic",
                        "Strong",
                        "1. Break it",
                        true,
                        "file:///android_asset/water.jpg"
                    ),
                    Cocktail(
                        4,
                        "Gauguin",
                        "Best served with friends.",
                        "Combine ingredients with a cup of crushed ice in blender and blend at low speed. Serve in old-fashioned glass. Top with a cherry.",
                        true,
                        "file:///android_asset/water.jpg"
                    ),
                    Cocktail(
                        5,
                        "Fort Lauderdale",
                        "Best served with friends.",
                        "Shake with ice and strain into old-fashioned glass over ice cubes. Add a slice of orange.",
                        true,
                        "file:///android_asset/water.jpg"
                    ),
                    Cocktail(
                        6,
                        "Apple Pie",
                        "Refreshing drink best served cold",
                        "Pour into ice-filled old-fashioned glass. Garnish with apple and top with cinnamon.",
                        true,
                        "file:///android_asset/water.jpg"
                    ),
                    Cocktail(
                        7,
                        "Cuban Cocktail No. 1",
                        "Prefect for a hectic day!",
                        "Shake with ice and  strain into cocktail glass.",
                        true,
                        "file:///android_asset/water.jpg"
                    ),
                    Cocktail(
                        8,
                        "Cool Carlos",
                        "One of the most liked drinks out there today",
                        "Mix all ingredients except curacao with ice, shake well. Pour into collins glass and float curacao on top. Garnish with pineapple and orange slices, and a cherry.",
                        false,
                        "file:///android_asset/water.jpg"
                    ),
                    Cocktail(
                        9,
                        "John Collins",
                        "One of the most popular drink out there!",
                        "Shake first three ingredients with ice and strain into Collins glass. Add ice, fill with soda water, and stir. Garnish with orange, lemon, and cherry. Serve with straws.",
                        true,
                        "file:///android_asset/water.jpg"
                    ),
                    Cocktail(
                        10,
                        "Cherry Rum",
                        "One of the most popular drink out there!",
                        "Shake with ice and strain into cocktail glass.",
                        true,
                        "file:///android_asset/water.jpg"
                    ),
                    Cocktail(
                        11,
                        "Casa Blanca",
                        "Best served with friends.",
                        "Shake with ice and strain into cocktail glass.",
                        false,
                        "file:///android_asset/water.jpg"
                    ),
                    Cocktail(
                        12,
                        "Caribbean Champagne",
                        "Prefect for a hectic day!",
                        "Pour rum and banana liqueur into champagne flute. Fill with champagne and stir lightly. Add a slice of banana.",
                        true,
                        "file:///android_asset/water.jpg"
                    ),
                    Cocktail(
                        13,
                        "Amber Amour",
                        "Refreshing drink best served cold",
                        "Pour amaretto, lemon juice, and syrup into ice-filled Collins glass. Top with soda water and stir. Garnish with cherry.",
                        false,
                        "file:///android_asset/water.jpg"
                    ),
                    Cocktail(
                        14,
                        "The Joe Lewis",
                        "Refreshing drink best served cold",
                        "Shake with ice and strain into chilled cocktail glass.",
                        false,
                        "file:///android_asset/water.jpg"
                    ),
                    Cocktail(
                        15,
                        "Bacardi Cocktail",
                        "Prefect for a hectic day!",
                        "Shake with ice and strain into cocktail glass.",
                        false,
                        "file:///android_asset/water.jpg"
                    ),
                    Cocktail(
                        16,
                        "Apple Pie No. 1",
                        "Refreshing drink best served cold",
                        "Shake with ice and strain into cocktail glass.",
                        true,
                        "file:///android_asset/water.jpg"
                    ),
                    Cocktail(
                        17,
                        "Yolanda",
                        "One of the most popular drink out there!",
                        "Shake with ice and strain into cocktail glass. Add a twist of orange peel.",
                        true,
                        "file:///android_asset/water.jpg"
                    ),
                    Cocktail(
                        18,
                        "Yellow Rattler",
                        "One of the most liked drinks out there today",
                        "Shake with ice and strain into cocktail glass. Add a cocktail onion.",
                        false,
                        "file:///android_asset/water.jpg"
                    ),
                    Cocktail(
                        19,
                        "Why Not?",
                        "Best served with friends.",
                        "Shake with ice and strain into cocktail glass.",
                        false,
                        "file:///android_asset/water.jpg"
                    ),
                    Cocktail(
                        20,
                        "Amaretto Stinger",
                        "Prefect for a hectic day!",
                        "Shake with ice and strain into chilled cocktail glass.",
                        false,
                        "file:///android_asset/water.jpg"
                    ),
                    Cocktail(
                        21,
                        "What The Hell",
                        "Prefect for a hectic day!",
                        "Stir in old-fashioned glass over ice cubes.",
                        true,
                        "file:///android_asset/water.jpg"
                    ),
                    Cocktail(
                        22,
                        "Webster Cocktail",
                        "Best served with friends.",
                        "Shake with ice and strain into cocktail glass.",
                        true,
                        "file:///android_asset/water.jpg"
                    ),
                    Cocktail(
                        23,
                        "Union Jack Cocktail",
                        "One of the most liked drinks out there today",
                        "Shake with ice and strain into cocktail glass.",
                        true,
                        "file:///android_asset/water.jpg"
                    ),
                    )


                for (i in arrayOf(ing1, ing2, ing3, ing4, ing5)) {
                    database.cocktailDao.insertIngredient(i)
                }

                for (i in cocktailList) {
                    database.cocktailDao.insertCocktail(i)
                }

                val ref1 = IngredientCocktailRef(1, 1, "Lemon", 1.0f, "Oz", false, false)
                val ref2 = IngredientCocktailRef(1, 2, "Salt", 1f, "Oz", false, false)
                val ref3 = IngredientCocktailRef(1, 3, "Water", 3f, "Oz", false, false)
                val ref4 = IngredientCocktailRef(2, 4, "Gin", 1f, "Oz", true, false)
                val ref5 = IngredientCocktailRef(2, 3, "Water", 1f, "Oz", false, false)
                val ref6 = IngredientCocktailRef(2, 5, "Vodka", 10f, "Gm", false, false)
                val ref7 = IngredientCocktailRef(2, 1, "Lemon", 1f, "Oz", true, false)
                val ref8 = IngredientCocktailRef(3, 1, "Lemon", 15f, "Gm", false, false)
                val ref9 =
                    IngredientCocktailRef(3, 3, "Water", 1f, "Oz", true, true)
                val ref10 = IngredientCocktailRef(3, 2, "Salt", 1f, "Oz", false, false)

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
            Timber.i("data inserted")
        }
    }

    suspend fun insertIngredient(ingredient: Ingredient) {
        database.cocktailDao.insertIngredient(ingredient)
    }

    suspend fun insertCocktail(cocktail: Cocktail) {
        database.cocktailDao.insertCocktail(cocktail)
    }

    suspend fun ignoreInsertIngredient(ingredient: Ingredient) {
        database.cocktailDao.ignoreAndInsertIngredient(ingredient)
    }

    suspend fun ignoreInsertCocktail(cocktail: Cocktail) {
        database.cocktailDao.ignoreAndInsertCocktail(cocktail)
    }

    suspend fun insertIngredientCocktailRef(ingredientCocktailRef: IngredientCocktailRef) {
        database.cocktailDao.insertIngredientCocktailRef(ingredientCocktailRef)
    }

    suspend fun ignoreInsertIngredientCocktailRef(ingredientCocktailRef: IngredientCocktailRef) {
        database.cocktailDao.ignoreInsertIngredientCocktailRef(ingredientCocktailRef)
    }

    suspend fun getIngredient(id: Long): Ingredient? {
        return database.cocktailDao.getIngredient(id)
    }

    suspend fun getIngredient(name: String): Ingredient? {
        return database.cocktailDao.getIngredient(name)
    }

    suspend fun getIngredientByName(name: String): List<IngredientWithCocktail> {
        return database.cocktailDao.getIngredientFromName(name)
    }

    suspend fun getALlCocktail(): List<Cocktail> {
        return database.cocktailDao.getAllCocktail()
    }

    suspend fun getAllIngredientCocktailRef(): List<IngredientCocktailRef> {
        return database.cocktailDao.getAllIngredientCocktailRef()
    }

    suspend fun getCocktailByName(name: String): List<CocktailWithIngredient> {
        return database.cocktailDao.getCocktailFromName(name)
    }

    suspend fun getCocktail(id: Long): Cocktail? {
        return database.cocktailDao.getCocktail(id)
    }

    suspend fun getCocktailWithIngredient(id: Long): CocktailWithIngredient {
        return database.cocktailDao.getIngredientFromCocktail(id)
    }

    suspend fun getAllIngredient(): List<Ingredient> {
        return database.cocktailDao.getAllIngredient()
    }

    suspend fun getLastCocktailId(): Long {
        return database.cocktailDao.getLastCocktailId()
    }

    suspend fun getLastIngredientId(): Long {
        return database.cocktailDao.getLastIngredientId()
    }

    suspend fun getRefFromCocktail(id: Long): List<IngredientCocktailRef> {
        return database.cocktailDao.getRefFromCocktail(id)
    }

    suspend fun getRefFromIngredient(name: String): List<IngredientCocktailRef> {
        return database.cocktailDao.getRefFromIngredient(name)
    }

    suspend fun getRefFromIngredientId(id: Long): List<IngredientCocktailRef> {
        return database.cocktailDao.getRefFromIngredientId(id)
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

    suspend fun deleteAllRefOfCocktail(id: Long) {
        database.cocktailDao.deleteAllRefOfCocktail(id)
    }

    suspend fun deleteAllRefOfIngredient(id: Long) {
        database.cocktailDao.deleteAllRefOfIngredient(id)
    }

    suspend fun deleteCocktail(cocktail: Cocktail) {
        database.cocktailDao.deleteCocktail(cocktail)
    }

    suspend fun deleteIngredient(ingredient: Ingredient) {
        database.cocktailDao.deleteIngredient(ingredient)
    }

    suspend fun deleteAllCocktail() {
        database.cocktailDao.deleteAllCocktail()
    }

    suspend fun deleteAllIngredient() {
        database.cocktailDao.deleteAllIngredient()
    }

    suspend fun deleteAllRef() {
        database.cocktailDao.deleteAllRef()
    }

}