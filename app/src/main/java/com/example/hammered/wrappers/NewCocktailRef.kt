package com.example.hammered.wrappers

import com.example.hammered.Constants
import com.example.hammered.R
import com.example.hammered.entities.relations.IngredientCocktailRef

data class NewCocktailRef(
    var ref_number: Int = 0,
    var ingredient_name: String = "",
    var quantity: String = "",
    var quantityUnitPos: Int = 0,
    var isGarnish: Boolean = false,
    var isOptional: Boolean = false
) {
    fun toIngredientCocktailRef(cocktailId: Long): IngredientCocktailRef {
        return IngredientCocktailRef(
            cocktail_id = cocktailId,
            ingredient_name = ingredient_name,
            quantity = quantity.toFloat(),
            quantityUnit = Constants.UNITS[quantityUnitPos],
            isGarnish = isGarnish,
            isOptional = isOptional
        )
    }
}
