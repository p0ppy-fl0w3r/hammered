package com.example.hammered.ingredients

import com.example.hammered.entities.relations.IngredientWithCocktail

sealed class IngredientItem {
    data class NormalIngredientItem(val ingredient: IngredientWithCocktail) : IngredientItem() {
        override val id = ingredient.ingredient.ingredient_name
    }

    data class IngredientInStock(val ingredient: IngredientWithCocktail) : IngredientItem() {
        override val id = ingredient.ingredient.ingredient_name
    }

    data class IngredientInCart(val ingredient: IngredientWithCocktail) : IngredientItem() {
        override val id = ingredient.ingredient.ingredient_name
    }

    abstract val id: String
}
