package com.fl0w3r.hammered.wrappers

import com.fl0w3r.hammered.Constants
import com.fl0w3r.hammered.entities.relations.IngredientCocktailRef

data class NewCocktailRef(
    var ref_number: Int = 0,
    var ingredient_name: String = "",
    var ingredient_id: Long = Long.MIN_VALUE,
    var quantity: String = "",
    var quantityUnitPos: Int = 0,
    var isGarnish: Boolean = false,
    var isOptional: Boolean = false
) {
    fun toIngredientCocktailRef(cocktailId: Long): IngredientCocktailRef {
        return IngredientCocktailRef(
            cocktail_id = cocktailId,
            ingredient_id = ingredient_id,
            ingredient_name = ingredient_name,
            quantity = quantity.toFloat(),
            quantityUnit = Constants.UNITS[quantityUnitPos],
            isGarnish = isGarnish,
            isOptional = isOptional
        )
    }
}
