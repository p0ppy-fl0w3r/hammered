package com.example.hammered.cocktail.cocktailDetails

import com.example.hammered.entities.relations.IngredientCocktailRef
import com.example.hammered.ingredients.IngredientData

data class RefIngredientWrapper(
    val ingredient: IngredientData,
    val ingredientRef: IngredientCocktailRef
)