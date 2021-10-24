package com.fl0w3r.hammered.ingredients

import com.fl0w3r.hammered.entities.relations.IngredientWithCocktail

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
