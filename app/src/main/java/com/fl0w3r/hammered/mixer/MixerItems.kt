package com.fl0w3r.hammered.mixer


data class IngredientMixerItem(
    var isSelected: Boolean = false,
    val id: Long,
    val ingredientName: String,
    val ingredientDescription: String,
    val ingredientImage: String
    )