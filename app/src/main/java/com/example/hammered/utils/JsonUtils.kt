package com.example.hammered.utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object JsonUtils {
    inline fun <reified T> getJsonFromClass(itemList: List<T>): String {

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val listType = Types.newParameterizedType(List::class.java, T::class.java)
        val adapter: JsonAdapter<List<T>> = moshi.adapter(listType)

        return adapter.toJson(itemList)
    }

    inline fun <reified T> getClassFromJson(jsonString: String): List<T>? {

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val listType = Types.newParameterizedType(List::class.java, T::class.java)
        val adapter: JsonAdapter<List<T>> = moshi.adapter(listType)

        return adapter.fromJson(jsonString)
    }
}