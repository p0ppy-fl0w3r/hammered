package com.fl0w3r.hammered.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fl0w3r.hammered.ingredients.IngredientData

@Entity
data class Ingredient(
    @PrimaryKey(autoGenerate = true) var ingredient_id: Long = 0,
    var ingredient_name: String,
    var ingredient_description: String,
    var ingredient_image: String,
    var inStock: Boolean,
    var inCart: Boolean
) {
    fun asIngredientData(): IngredientData {
        return IngredientData(
            ingredient_id,
            ingredient_name,
            ingredient_description,
            inStock,
            inCart
        )
    }
}
