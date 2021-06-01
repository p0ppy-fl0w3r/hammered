package com.example.hammered.entities.relations

import androidx.room.Entity


@Entity(primaryKeys = ["cocktail_id", "ingredient_name"])
data class IngredientCocktailRef(
    var cocktail_id: Long,
    var ingredient_name: String,
    var quantity:Int,
    var quantityUnit: String,
    var isGarnish:Boolean,
    var isOptional:Boolean
    // TODO add typeConverter for :- var substitutes:List<String>
)
