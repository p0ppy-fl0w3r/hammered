package com.fl0w3r.hammered.ingredients

import com.fl0w3r.hammered.entities.relations.IngredientWithCocktail

class IngredientClickListener(val clickListener: (IngredientWithCocktail) -> Unit) {
    fun onClick(ingredient: IngredientWithCocktail) = clickListener(ingredient)
}

class ItemStatusChangeListener(val checkedChangeListener: (IngredientWithCocktail, itemFrom: Int) -> Unit) {
    fun onCheckedChangeListener(ingredient: IngredientWithCocktail, itemFrom: Int) =
        checkedChangeListener(ingredient, itemFrom)
}