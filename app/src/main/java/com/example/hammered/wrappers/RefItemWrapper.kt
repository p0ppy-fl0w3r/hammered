package com.example.hammered.wrappers

import com.example.hammered.entities.relations.IngredientCocktailRef


data class RefItemWrapper<Item>(
    val item: Item,
    val ingredientRef: IngredientCocktailRef
)