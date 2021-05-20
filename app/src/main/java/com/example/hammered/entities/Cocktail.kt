package com.example.hammered.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cocktail(
    @PrimaryKey(autoGenerate = false) var cocktail_id:Long,
    var cocktail_name: String,
    var cocktail_description: String,
    var steps: String,
    var isFavorite: Boolean,
    var cocktail_image: String
)
