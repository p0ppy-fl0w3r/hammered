package com.example.hammered.utils

import com.example.hammered.entities.Ingredient
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Export data in to json file
 * Export images
 * TODO 3) Import data and images
 * TODO 4) Let the user manage the conflict strategy.
 * Either let them replace the existing data with a new one from the json file or let them ignore
 * the new one and keep the existing one. See the insert methods used on CreateIngredientViewModel
 * class to get an idea about how the insert should be handled.
 **/

object JsonUtils {
    inline fun <reified T> getJsonFromClass(ingList: List<T>): String {

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val listType = Types.newParameterizedType(List::class.java, T::class.java)
        val adapter: JsonAdapter<List<T>> = moshi.adapter(listType)

        return adapter.toJson(ingList)
    }
}