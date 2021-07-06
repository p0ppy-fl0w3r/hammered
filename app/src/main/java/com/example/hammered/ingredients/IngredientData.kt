package com.example.hammered.ingredients

import android.os.Parcelable
import com.example.hammered.entities.Ingredient
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IngredientData(
    var ingredient_id: Long,
    var ingredient_name: String,
    var ingredient_description: String,
    var ingredient_image: String,
    var inStock: Boolean,
    var inCart: Boolean
) : Parcelable {
    fun asIngredient(): Ingredient {
        return Ingredient(
            ingredient_id,
            ingredient_name,
            ingredient_description,
            ingredient_image,
            inStock,
            inCart
        )
    }
}