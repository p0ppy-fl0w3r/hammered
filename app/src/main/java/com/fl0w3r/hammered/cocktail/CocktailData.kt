package com.fl0w3r.hammered.cocktail

import android.os.Parcelable
import com.fl0w3r.hammered.entities.Cocktail
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CocktailData(
    var cocktail_id: Long,
    var cocktail_name: String,
    var cocktail_description: String,
    var steps: String,
    var isFavorite: Boolean,
    var makeCount:Int = 0,
    var cocktail_image:String = ""
) : Parcelable {
    fun asCocktail(): Cocktail {
        return Cocktail(
            cocktail_id,
            cocktail_name,
            cocktail_description,
            steps,
            isFavorite,
            makeCount,
            cocktail_image
        )
    }
}
