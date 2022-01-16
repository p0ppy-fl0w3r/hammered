package com.fl0w3r.hammered.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fl0w3r.hammered.cocktail.CocktailData

@Entity
data class Cocktail(
    @PrimaryKey(autoGenerate = false) var cocktail_id: Long,
    var cocktail_name: String,
    var cocktail_description: String,
    var steps: String,
    var isFavorite: Boolean,
    var cocktail_image: String
) {
    fun asData(): CocktailData {
        return CocktailData(
            cocktail_id,
            cocktail_name,
            cocktail_description,
            steps,
            isFavorite,
        )
    }
}
