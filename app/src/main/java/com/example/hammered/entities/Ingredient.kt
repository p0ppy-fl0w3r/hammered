package com.example.hammered.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ingredient(
    @PrimaryKey(autoGenerate = false) var ingredient_name: String,
    var ingredient_description: String,
    var ingredient_image: String,
    var inStock: Boolean
)
