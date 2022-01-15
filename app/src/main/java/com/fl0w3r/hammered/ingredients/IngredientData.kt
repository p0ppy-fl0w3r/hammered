package com.fl0w3r.hammered.ingredients

import android.os.Parcelable
import com.fl0w3r.hammered.entities.Ingredient
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IngredientData(
    var ingredient_id: Long,
    var ingredient_name: String,
    var ingredient_description: String,
    var inStock: Boolean,
    var inCart: Boolean
) : Parcelable {
    fun asIngredient(): Ingredient {
        return Ingredient(
            ingredient_id,
            ingredient_name,
            ingredient_description,
            "",
            inStock,
            inCart
        )
    }
}
