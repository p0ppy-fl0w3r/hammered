package com.example.hammered.cocktail

import com.example.hammered.entities.relations.CocktailWithIngredient

sealed class CocktailItem {

    data class NormalCocktailItem(val cocktail: CocktailWithIngredient) : CocktailItem() {
        override val id = cocktail.cocktail.cocktail_id
    }

    data class AvailableCocktailItem(val cocktail: CocktailWithIngredient) : CocktailItem() {
        override val id = cocktail.cocktail.cocktail_id
    }

    data class FavoriteCocktailItem(val cocktail: CocktailWithIngredient) : CocktailItem() {
        override val id = cocktail.cocktail.cocktail_id
    }

    class EmptyListItem() : CocktailItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long
}
