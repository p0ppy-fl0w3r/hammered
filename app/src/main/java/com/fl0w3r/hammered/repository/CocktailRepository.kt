package com.fl0w3r.hammered.repository

import com.fl0w3r.hammered.database.CocktailDatabase
import com.fl0w3r.hammered.entities.Cocktail
import com.fl0w3r.hammered.entities.Ingredient
import com.fl0w3r.hammered.entities.relations.CocktailWithIngredient
import com.fl0w3r.hammered.entities.relations.IngredientCocktailRef
import com.fl0w3r.hammered.entities.relations.IngredientWithCocktail
import timber.log.Timber

class CocktailRepository(private val database: CocktailDatabase) {


    suspend fun insertAll() {

        if (database.cocktailDao.getIngredientCocktailRefCount() == 0) {
            Timber.i("Insert started")

            // TODO add the data in a json file.
            val ing_arr: Array<Ingredient> = arrayOf(

                Ingredient(1, "Lemon", "Nice", "file:///android_asset/lemon.png", true, false),

                Ingredient(
                    2,
                    "Salt",
                    "Very nice",
                    "file:///android_asset/salt.png",
                    true,
                    false
                ),

                Ingredient(3, "Water", "Nice", "file:///android_asset/water.jpg", true, false),
                Ingredient(4, "Gin", "Nice", "file:///android_asset/gin.png", false, true),
                Ingredient(5, "Vodka", "Nice", "file:///android_asset/vodka.webp", false, true),
                Ingredient(
                    6,
                    " Anisette",
                    "Found in your local supermarket",
                    "file:///android_asset/salt.png",
                    true,
                    false
                ),


                Ingredient(
                    7,
                    " Dry Vermouth",
                    "Best served fresh!",
                    "file:///android_asset/salt.png",
                    true,
                    false
                ),


                Ingredient(
                    8,
                    " Powdered Sugar",
                    "Onr of the best things you can add to your drink.",
                    "file:///android_asset/salt.png",
                    false,
                    true
                ),


                Ingredient(
                    9,
                    "Chilled Champagne",
                    "Onr of the best things you can add to your drink.",
                    "file:///android_asset/salt.png",
                    false,
                    true
                ),


                Ingredient(
                    10,
                    " Cranberry Juice",
                    "Onr of the best things you can add to your drink.",
                    "file:///android_asset/salt.png",
                    true,
                    true
                ),


                Ingredient(
                    11,
                    " Sloe gin",
                    "Found in your local supermarket",
                    "file:///android_asset/salt.png",
                    true,
                    false
                ),


                Ingredient(
                    12,
                    " Pineapple Juice",
                    "Add this to make your drink taste electric.",
                    "file:///android_asset/salt.png",
                    true,
                    true
                ),


                Ingredient(
                    13,
                    " Fresh lemon juice",
                    "Best served fresh!",
                    "file:///android_asset/salt.png",
                    true,
                    true
                ),


                Ingredient(
                    14,
                    " Soda water",
                    "Add this to make your drink taste electric.",
                    "file:///android_asset/salt.png",
                    true,
                    true
                ),


                Ingredient(
                    15,
                    " Orange Juice",
                    "Add this to make your drink taste electric.",
                    "file:///android_asset/salt.png",
                    true,
                    true
                ),


                Ingredient(
                    16,
                    " Bourbon whiskey",
                    "Onr of the best things you can add to your drink.",
                    "file:///android_asset/salt.png",
                    true,
                    false
                ),


                Ingredient(
                    17,
                    " Light Rum",
                    "Best served fresh!",
                    "file:///android_asset/salt.png",
                    true,
                    true
                ),


                Ingredient(
                    18,
                    " Orange and lemon wheels, maraschino cherry",
                    "Onr of the best things you can add to your drink.",
                    "file:///android_asset/salt.png",
                    false,
                    false
                ),


                Ingredient(
                    19,
                    " Passion Fruit Syrup",
                    "Onr of the best things you can add to your drink.",
                    "file:///android_asset/salt.png",
                    true,
                    false
                ),


                Ingredient(
                    20,
                    " Lemon Juice",
                    "Best served fresh!",
                    "file:///android_asset/salt.png",
                    false,
                    true
                ),


                Ingredient(
                    21,
                    " Juice of Orange",
                    "Found in your local supermarket",
                    "file:///android_asset/salt.png",
                    true,
                    false
                ),


                Ingredient(
                    22,
                    " Sour Mix",
                    "Found in your local supermarket",
                    "file:///android_asset/salt.png",
                    true,
                    true
                ),


                Ingredient(
                    23,
                    " Grenadine",
                    "Best served fresh!",
                    "file:///android_asset/salt.png",
                    false,
                    false
                ),


                Ingredient(
                    24,
                    " Bacardi Rum",
                    "Found in your local supermarket",
                    "file:///android_asset/salt.png",
                    false,
                    true
                ),


                Ingredient(
                    25,
                    " Maraschino",
                    "Onr of the best things you can add to your drink.",
                    "file:///android_asset/salt.png",
                    true,
                    false
                ),


                Ingredient(
                    26,
                    " Tawny port",
                    "Found in your local supermarket",
                    "file:///android_asset/salt.png",
                    false,
                    false
                ),


                Ingredient(
                    27,
                    " Apple slice",
                    "Best served fresh!",
                    "file:///android_asset/salt.png",
                    true,
                    true
                ),


                Ingredient(
                    28,
                    " Sweet Vermouth",
                    "Onr of the best things you can add to your drink.",
                    "file:///android_asset/salt.png",
                    false,
                    true
                ),


                Ingredient(
                    29,
                    " Agave nectar",
                    "Best served fresh!",
                    "file:///android_asset/salt.png",
                    false,
                    false
                ),


                Ingredient(
                    30,
                    " Juice of a Lime",
                    "Best served fresh!",
                    "file:///android_asset/salt.png",
                    true,
                    false
                ),


                Ingredient(
                    31,
                    " Apricot Flavored Brandy",
                    "Onr of the best things you can add to your drink.",
                    "file:///android_asset/salt.png",
                    true,
                    true
                ),


                Ingredient(
                    32,
                    "nan",
                    "Best served fresh!",
                    "file:///android_asset/salt.png",
                    true,
                    true
                ),


                Ingredient(
                    33,
                    " Light cream",
                    "Onr of the best things you can add to your drink.",
                    "file:///android_asset/salt.png",
                    true,
                    true
                ),


                Ingredient(
                    34,
                    " Triple Sec",
                    "Found in your local supermarket",
                    "file:///android_asset/salt.png",
                    false,
                    true
                ),


                Ingredient(
                    35,
                    " Lime Juice",
                    "Best served fresh!",
                    "file:///android_asset/salt.png",
                    false,
                    false
                ),


                Ingredient(
                    36,
                    " Fresh carrot juice",
                    "Found in your local supermarket",
                    "file:///android_asset/salt.png",
                    false,
                    true
                ),


                Ingredient(
                    37,
                    " Simple Syrup",
                    "Best served fresh!",
                    "file:///android_asset/salt.png",
                    false,
                    false
                ),


                Ingredient(
                    38,
                    " Brandy",
                    "Onr of the best things you can add to your drink.",
                    "file:///android_asset/salt.png",
                    false,
                    false
                ),


                Ingredient(
                    39,
                    " Cinnamon schnapps",
                    "Add this to make your drink taste electric.",
                    "file:///android_asset/salt.png",
                    false,
                    false
                ),


                Ingredient(
                    40,
                    " Amaretto",
                    "Add this to make your drink taste electric.",
                    "file:///android_asset/salt.png",
                    false,
                    true
                ),


                Ingredient(
                    41,
                    " Apple Brandy",
                    "Best served fresh!",
                    "file:///android_asset/salt.png",
                    false,
                    true
                ),


                Ingredient(
                    42,
                    " White creme de menthe",
                    "Best served fresh!",
                    "file:///android_asset/salt.png",
                    false,
                    true
                ),


                Ingredient(
                    43,
                    " Creme de banana",
                    "Add this to make your drink taste electric.",
                    "file:///android_asset/salt.png",
                    false,
                    true
                ),


                Ingredient(
                    44,
                    " cherry-flavored brandy",
                    "Onr of the best things you can add to your drink.",
                    "file:///android_asset/salt.png",
                    true,
                    true
                ),


                Ingredient(
                    45,
                    " Gin",
                    "Add this to make your drink taste electric.",
                    "file:///android_asset/salt.png",
                    false,
                    false
                ),


                Ingredient(
                    46,
                    " Dark rum",
                    "Best served fresh!",
                    "file:///android_asset/salt.png",
                    false,
                    false
                ),


                Ingredient(
                    47,
                    " Scotch Whiskey",
                    "Add this to make your drink taste electric.",
                    "file:///android_asset/salt.png",
                    true,
                    true
                ),


                Ingredient(
                    48,
                    " Apple schnapps",
                    "Found in your local supermarket",
                    "file:///android_asset/salt.png",
                    false,
                    true
                ),


                Ingredient(
                    49,
                    " Orange curacao",
                    "Add this to make your drink taste electric.",
                    "file:///android_asset/salt.png",
                    false,
                    false
                ),
            )

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


            for (i in ing_arr) {
                database.cocktailDao.insertIngredient(i)
            }

            for (i in cocktailList) {
                database.cocktailDao.insertCocktail(i)
            }



            for (i in arrayOf(
                IngredientCocktailRef(1, 1, "Lemon", 1.0f, "Oz", false, false),
                IngredientCocktailRef(1, 2, "Salt", 1f, "Oz", false, false),
                IngredientCocktailRef(1, 3, "Water", 3f, "Oz", false, false),
                IngredientCocktailRef(2, 4, "Gin", 1f, "Oz", true, false),
                IngredientCocktailRef(2, 3, "Water", 1f, "Oz", false, false),
                IngredientCocktailRef(2, 5, "Vodka", 10f, "Gm", false, false),
                IngredientCocktailRef(2, 1, "Lemon", 1f, "Oz", true, false),
                IngredientCocktailRef(3, 1, "Lemon", 15f, "Gm", false, false),
                IngredientCocktailRef(3, 3, "Water", 1f, "Oz", true, true),
                IngredientCocktailRef(3, 2, "Salt", 1f, "Oz", false, false),
                IngredientCocktailRef(4, 47, "Light Rum", 2f, "Oz", true, false),
                IngredientCocktailRef(4, 29, "Passion Fruit Syrup", 1f, "Oz", false, true),
                IngredientCocktailRef(4, 45, "Lemon Juice", 1f, "Oz", false, false),
                IngredientCocktailRef(4, 43, "Lime Juice", 1f, "Oz", false, true),
                IngredientCocktailRef(5, 47, "Light Rum", 1f, "Oz", false, false),
                IngredientCocktailRef(5, 26, "Sweet Vermouth", 0.5f, "Oz", true, true),
                IngredientCocktailRef(5, 7, "Juice of Orange", 1 / 4f, "Oz", false, true),
                IngredientCocktailRef(5, 35, "Juice of a Lime", 1 / 4f, "Oz", true, false),
                IngredientCocktailRef(6, 15, "Apple schnapps", 3f, "Oz", false, true),
                IngredientCocktailRef(6, 32, "Cinnamon schnapps", 1f, "Oz", false, true),
                IngredientCocktailRef(7, 35, "Juice of a Lime", 1 / 2f, "Oz", false, false),
                IngredientCocktailRef(7, 36, "Powdered Sugar", 1 / 2f, "Oz", false, false),
                IngredientCocktailRef(7, 47, "Light Rum", 2f, "Oz", false, true),
                IngredientCocktailRef(8, 25, "Dark rum", 1f, "1/2", false, true),
                IngredientCocktailRef(8, 24, "Cranberry Juice", 2f, "Oz", true, false),
                IngredientCocktailRef(8, 18, "Pineapple Juice", 2f, "Oz", false, false),
                IngredientCocktailRef(8, 48, "Orange curacao", 1f, "Oz", false, true),
                IngredientCocktailRef(8, 17, "Sour Mix", 1f, "Oz", false, true),
                IngredientCocktailRef(9, 28, "Bourbon whiskey", 2f, "Oz", true, true),
                IngredientCocktailRef(9, 30, "Fresh lemon juice", 1f, "Oz", true, true),
                IngredientCocktailRef(9, 42, "Simple Syrup", 1 / 2f, "Oz", false, false),
                IngredientCocktailRef(9, 22, "Soda water", 1f, "Oz", false, true),
                IngredientCocktailRef(
                    9,
                    11,
                    "Orange and lemon wheels, maraschino cherry",
                    1f,
                    "Oz",
                    true,
                    true
                ),
                IngredientCocktailRef(10, 47, "Light Rum", 1f, "1/4", false, false),
                IngredientCocktailRef(10, 41, "cherry-flavored brandy", 1f, "1/2", false, true),
                IngredientCocktailRef(10, 14, "Light cream", 1f, "Oz", false, true),
                IngredientCocktailRef(11, 47, "Light Rum", 2f, "Oz", false, true),
                IngredientCocktailRef(11, 43, "Lime Juice", 1f, "1/2", false, true),
                IngredientCocktailRef(11, 12, "Triple Sec", 1f, "1/2", true, true),
                IngredientCocktailRef(11, 16, "Maraschino", 1f, "1/2", false, true),
                IngredientCocktailRef(12, 47, "Light Rum", 1 / 2f, "Oz", true, true),
                IngredientCocktailRef(12, 49, "Creme de banana", 1 / 2f, "Oz", true, false),
                IngredientCocktailRef(13, 21, "Amaretto", 1f, "1/2", true, false),
                IngredientCocktailRef(13, 30, "Fresh lemon juice", 1 / 4f, "Oz", true, false),
                IngredientCocktailRef(13, 42, "Simple Syrup", 1 / 4f, "Oz", false, true),
                IngredientCocktailRef(14, 23, "Scotch Whiskey", 1f, "1/2", false, false),
                IngredientCocktailRef(14, 38, "Fresh carrot juice", 1f, "Oz", true, false),
                IngredientCocktailRef(14, 46, "Tawny port", 3 / 4f, "Oz", false, false),
                IngredientCocktailRef(14, 30, "Fresh lemon juice", 1 / 2f, "Oz", false, true),
                IngredientCocktailRef(14, 31, "Agave nectar", 1f, "Oz", false, false),
                IngredientCocktailRef(15, 6, "Bacardi Rum", 1f, "1/2", true, true),
                IngredientCocktailRef(15, 35, "Juice of a Lime", 1 / 2f, "Oz", true, true),
                IngredientCocktailRef(15, 33, "Grenadine", 1 / 2f, "Oz", true, true),
                IngredientCocktailRef(16, 47, "Light Rum", 3 / 4f, "Oz", false, false),
                IngredientCocktailRef(16, 26, "Sweet Vermouth", 3 / 4f, "Oz", false, true),
                IngredientCocktailRef(16, 8, "Apple Brandy", 1f, "Oz", false, true),
                IngredientCocktailRef(16, 33, "Grenadine", 1 / 2f, "Oz", false, false),
                IngredientCocktailRef(16, 45, "Lemon Juice", 1f, "Oz", true, false),
                IngredientCocktailRef(17, 40, "Brandy", 1 / 2f, "Oz", false, true),
                IngredientCocktailRef(17, 9, "Gin", 1 / 2f, "Oz", true, true),
                IngredientCocktailRef(17, 39, "Anisette", 1 / 2f, "Oz", true, false),
                IngredientCocktailRef(17, 26, "Sweet Vermouth", 1f, "Oz", true, false),
                IngredientCocktailRef(17, 33, "Grenadine", 1f, "Oz", false, false),
                IngredientCocktailRef(18, 9, "Gin", 1f, "Oz", false, false),
                IngredientCocktailRef(18, 13, "Orange Juice", 1f, "Oz", false, true),
                IngredientCocktailRef(18, 10, "Dry Vermouth", 1 / 2f, "Oz", false, true),
                IngredientCocktailRef(18, 26, "Sweet Vermouth", 1 / 2f, "Oz", false, true),
                IngredientCocktailRef(19, 9, "Gin", 1f, "Oz", true, true),
                IngredientCocktailRef(19, 37, "Apricot Flavored Brandy", 1f, "Oz", false, true),
                IngredientCocktailRef(19, 10, "Dry Vermouth", 1 / 2f, "Oz", false, true),
                IngredientCocktailRef(19, 45, "Lemon Juice", 1f, "Oz", false, true),
                IngredientCocktailRef(20, 21, "Amaretto", 1f, "1/2", true, false),
                IngredientCocktailRef(20, 20, "White creme de menthe", 3 / 4f, "Oz", false, true),
                IngredientCocktailRef(21, 9, "Gin", 1f, "Oz", true, false),
                IngredientCocktailRef(21, 10, "Dry Vermouth", 1f, "Oz", true, false),
                IngredientCocktailRef(21, 37, "Apricot Flavored Brandy", 1f, "Oz", false, true),
                IngredientCocktailRef(21, 45, "Lemon Juice", 1f, "Oz", false, false),
                IngredientCocktailRef(22, 35, "Juice of a Lime", 1 / 2f, "Oz", false, false),
                IngredientCocktailRef(22, 37, "Apricot Flavored Brandy", 1f, "1/2", true, false),
                IngredientCocktailRef(22, 10, "Dry Vermouth", 1 / 2f, "Oz", true, false),
                IngredientCocktailRef(22, 9, "Gin", 1f, "Oz", true, true),
                IngredientCocktailRef(23, 44, "Sloe gin", 3 / 4f, "Oz", false, true),
                IngredientCocktailRef(23, 9, "Gin", 1f, "1/2", false, true),
                IngredientCocktailRef(23, 33, "Grenadine", 1 / 2f, "Oz", false, false),
            )) {
                Timber.e("Inserted $i")
                database.cocktailDao.insertIngredientCocktailRef(i)
            }
        }
        Timber.i("data inserted")

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

    suspend fun getAllCocktailWithIngredient():List<CocktailWithIngredient> {
        return database.cocktailDao.getIngredientFromCocktail()
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

    suspend fun getRefFromIngredientId(ids: List<Long>): List<IngredientCocktailRef> {
        return database.cocktailDao.getRefFromIngredientId(ids)
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