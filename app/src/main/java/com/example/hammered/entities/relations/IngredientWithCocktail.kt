package com.example.hammered.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.hammered.entities.Cocktail
import com.example.hammered.entities.Ingredient

data class IngredientWithCocktail(
    @Embedded val ingredient: Ingredient,
    @Relation(
        parentColumn = "ingredient_id",
        entityColumn = "cocktail_id",
        associateBy = Junction(IngredientCocktailRef::class)
    ) val cocktails: List<Cocktail>

)
