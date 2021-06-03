package com.example.hammered.ingredients

import com.example.hammered.cocktail.CocktailItem
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

    class EmptyListItem() : IngredientItem() {
        override val id = "b00164b3dd0bfb5f71135d8abodedEmptyList"
    }

    abstract val id: String
}
