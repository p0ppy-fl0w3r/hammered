package com.fl0w3r.hammered.cocktail

import com.fl0w3r.hammered.entities.relations.CocktailWithIngredient

class CocktailClickListener(val clickListener: (CocktailWithIngredient) -> Unit) {
    fun onClick(cocktailWithIngredient: CocktailWithIngredient) =
        clickListener(cocktailWithIngredient)
}