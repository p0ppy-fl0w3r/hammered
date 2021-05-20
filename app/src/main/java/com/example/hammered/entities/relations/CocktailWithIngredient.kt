package com.example.hammered.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.hammered.entities.Cocktail
import com.example.hammered.entities.Ingredient

data class CocktailWithIngredient(
    @Embedded val cocktail: Cocktail,
    @Relation(
        parentColumn = "cocktail_id",
        entityColumn = "ingredient_name",
        associateBy = Junction(IngredientCocktailRef::class)
    ) val ingredients: List<Ingredient>

)
