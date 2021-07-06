package com.example.hammered.ingredients

import com.example.hammered.entities.relations.IngredientWithCocktail

sealed class IngredientItem {
    data class NormalIngredientItem(val ingredient: IngredientWithCocktail) : IngredientItem() {
        override val id = ingredient.ingredient.ingredient_id
    }

    data class IngredientInStock(val ingredient: IngredientWithCocktail) : IngredientItem() {
        override val id = ingredient.ingredient.ingredient_id
    }

    data class IngredientInCart(val ingredient: IngredientWithCocktail) : IngredientItem() {
        override val id = ingredient.ingredient.ingredient_id
    }

    class EmptyListItem() : IngredientItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long
}
