package com.fl0w3r.hammered.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.fl0w3r.hammered.entities.Cocktail
import com.fl0w3r.hammered.entities.Ingredient

data class CocktailWithIngredient(
    @Embedded val cocktail: Cocktail,
    @Relation(
        parentColumn = "cocktail_id",
        entityColumn = "ingredient_id",
        associateBy = Junction(IngredientCocktailRef::class)
    ) val ingredients: List<Ingredient>

)
