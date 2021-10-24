package com.fl0w3r.hammered.entities.relations

import androidx.room.Entity


@Entity(primaryKeys = ["cocktail_id", "ingredient_id"])
data class IngredientCocktailRef(
    var cocktail_id: Long,
    var ingredient_id: Long,
    var ingredient_name: String,
    var quantity:Float,
    var quantityUnit: String,
    var isGarnish:Boolean,
    var isOptional:Boolean
)
