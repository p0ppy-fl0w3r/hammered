package com.fl0w3r.hammered.wrappers

import com.fl0w3r.hammered.entities.Ingredient
import com.fl0w3r.hammered.entities.relations.IngredientCocktailRef

data class SlideWrapper(val step:String, val ingredient: List<RefItemWrapper<Ingredient>>)
