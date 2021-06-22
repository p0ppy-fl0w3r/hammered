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

    // Quantity units list
    val UNITS = listOf(
        "Ml",
        "Oz",
        "Gm",
        "Unit/s",
        "Drops"
    )

    // Error handlers for create cocktail
    const val VALUE_OK = -2
    const val NO_VALUES = -1
    const val NO_INGREDIENT_IN_DATABASE = -3
    const val QUANTITY_FIELD_EMPTY = -4
    const val INGREDIENT_NAME_EMPTY = -5

    // Search item ids
    const val SEARCH_COCKTAIL = 1
    const val SEARCH_INGREDIENT = 2
}
