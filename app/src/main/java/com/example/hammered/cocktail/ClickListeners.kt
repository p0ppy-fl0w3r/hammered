package com.example.hammered.cocktail

import com.example.hammered.entities.relations.CocktailWithIngredient

class CocktailClickListener(val clickListener: (CocktailWithIngredient) -> Unit) {
    fun onClick(cocktailWithIngredient: CocktailWithIngredient) =
        clickListener(cocktailWithIngredient)
}