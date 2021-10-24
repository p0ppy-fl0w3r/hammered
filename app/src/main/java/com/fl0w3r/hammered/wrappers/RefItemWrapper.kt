package com.fl0w3r.hammered.wrappers

import com.fl0w3r.hammered.entities.relations.IngredientCocktailRef


data class RefItemWrapper<Item>(
    val item: Item,
    val ingredientRef: IngredientCocktailRef
)