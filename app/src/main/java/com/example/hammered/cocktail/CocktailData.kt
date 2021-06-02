package com.example.hammered.cocktail

import android.os.Parcelable
import com.example.hammered.entities.Cocktail
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CocktailData(
    var cocktail_id: Long,
    var cocktail_name: String,
    var cocktail_description: String,
    var steps: String,
    var isFavorite: Boolean,
    var cocktail_image: String
) : Parcelable {
    fun asCocktail(): Cocktail {
        return Cocktail(
            cocktail_id,
            cocktail_name,
            cocktail_description,
            steps,
            isFavorite,
            cocktail_image
        )
    }
}
