package com.example.hammered

object Constants {

    const val EMPTY_ITEM = -1

    // Ingredient, Ingredient Adaptors and Layouts
    const val NORMAL_ITEM = 3
    const val ITEM_IN_STOCK = 4
    const val ITEM_IN_CART = 5

    // Cocktail
    const val NORMAL_COCKTAIL_ITEM = 0
    const val AVAILABLE_COCKTAIL_ITEM = 1
    const val FAVORITE_COCKTAIL_ITEM = 2

    val ALL_ITEM_LIST = listOf(
        "All Drinks",
        "My Drinks",
        "Favorite Drinks",
        "All ingredients",
        "My Stock",
        "Shopping Cart"
    )

    // Quantity units list
    val UNITS = listOf(
        "Ml",
        "Oz",
        "Gm",
        "Unit/s",
        "Drops"
    )

    // Status code for create cocktail
    const val VALUE_OK = -2
    const val NO_VALUES = -1
    const val NO_INGREDIENT_IN_DATABASE = -3
    const val QUANTITY_FIELD_EMPTY = -4
    const val INGREDIENT_NAME_EMPTY = -5

    // Create cocktail extra key
    const val EDIT_COCKTAIL = "cocktail"

    // Search item ids
    const val SEARCH_COCKTAIL = 6
    const val SEARCH_INGREDIENT = 7

    // Copy and edit cocktail.
    const val COPY_AND_EDIT = "copy_and_edit_cocktail"

    // Splash startup screen
    const val STARTUP_SCREEN_ID = "startup_screen_id"
    const val BUNDLE_STARTUP_INT = "bundle_startup_int"

    // Export to json status codes
    const val DATA_SAVE_SUCCESS = 8
    const val FILE_CREATION_FAILED = 9
    const val DIRECTORY_INVALID = 10
    const val GET_DIR_FAILED = 11
    const val DIRECTORY_CREATE_FAILED = 12

    // JSON file names
    const val INGREDIENT_JSON_FILE = "ingredient.json"
    const val COCKTAIL_JSON_FILE = "cocktail.json"
    const val BRIDGING_JSON_FILE = "ref.json"

    // Import from json status code
    const val FOLDER_INVALID = 13
    const val IMPORT_FAILED = 14
    const val IMPORT_SUCCESS = 15
}
