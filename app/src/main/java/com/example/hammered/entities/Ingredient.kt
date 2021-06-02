package com.example.hammered.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hammered.ingredients.IngredientData

@Entity
data class Ingredient(
    @PrimaryKey(autoGenerate = false) var ingredient_name: String,
    var ingredient_description: String,
    var ingredient_image: String,
    var inStock: Boolean,
    var inCart: Boolean
) {
    fun asIngredientData(): IngredientData {
        return IngredientData(
            ingredient_name,
            ingredient_description,
            ingredient_image,
            inStock,
            inCart
        )
    }
}
