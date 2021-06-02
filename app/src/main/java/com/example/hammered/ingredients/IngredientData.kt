package com.example.hammered.ingredients

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IngredientData(
    var ingredient_name: String,
    var ingredient_description: String,
    var ingredient_image: String,
    var inStock: Boolean,
    var inCart: Boolean
):Parcelable