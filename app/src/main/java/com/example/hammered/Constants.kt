package com.example.hammered

object Constants {

    const val EMPTY_ITEM = -1

    // Ingredient, Ingredient Adaptors and Layouts
    const val NORMAL_ITEM = 1
    const val ITEM_IN_STOCK = 2
    const val ITEM_IN_CART = 3

    // Cocktail
    const val NORMAL_COCKTAIL_ITEM = 1
    const val AVAILABLE_COCKTAIL_ITEM = 2
    const val FAVORITE_COCKTAIL_ITEM = 3

    // Create Cocktail
    const val SPINNER_ITEM = 1
    const val IS_OPTIONAL_CHECK = 2
    const val IS_GARNISH_CHECK = 3

    // Quantity units list
    val UNITS = listOf(
        "Ml",
        "Oz",
        "Gm",
        "Unit/s",
        "Drops"
    )
}