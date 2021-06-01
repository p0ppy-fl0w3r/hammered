package com.example.hammered

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.hammered.entities.Ingredient
import timber.log.Timber

@BindingAdapter("imageSource")
fun imageSource(imageView: ImageView, imageUrl: String?) {
    imageUrl?.let {
        Glide.with(imageView.context)
            .load(Uri.parse("file:///android_asset/$imageUrl"))
            .into(imageView)
    }
}

@BindingAdapter("ingredientList")
fun ingredientList(textView: TextView, ingredients: List<Ingredient>) {
    val ingredientInStock = mutableListOf<String>()
    val ingredientNotInStock = mutableListOf<String>()
    var hasAll = true
    for (i in ingredients) {
        if (i.inStock) {
            ingredientInStock.add(i.ingredient_name)
        }
        else {
            hasAll = false
            ingredientNotInStock.add(i.ingredient_name)
        }
    }
    if (hasAll) {
        val inStockLast = ingredientInStock.removeLast()

        val inStockFinalStr = if (ingredientInStock.isNotEmpty()) {
            ingredientInStock.joinToString(
                postfix = " & $inStockLast",
                separator = ", "
            ) { it }

        }
        else {
            inStockLast
        }
        textView.text = inStockFinalStr
    }
    else {

        val last = ingredientNotInStock.removeLast()

        val finalStr = if (ingredientNotInStock.isNotEmpty()) {

            ingredientNotInStock.joinToString(
                postfix = " & ${last.lowercase()}",
                separator = ", "
            ) { it.lowercase() }

        }
        else {
            last
        }

        textView.text =
            textView.context.getString(R.string.not_in_stock_msg, finalStr)
    }
}

@BindingAdapter("isFavourite")
fun isFavourite(imageView: ImageView, visible: Boolean) {
    if (visible) {
        imageView.visibility = ImageView.VISIBLE
    }
    else {
        imageView.visibility = ImageView.INVISIBLE
    }
}

@BindingAdapter("isMakable")
fun isMakable(imageView: ImageView, ingredients: List<Ingredient>) {
    var isMakableDrink = true
    for (i in ingredients) {
        if (!i.inStock) {
            isMakableDrink = false
        }
    }

    if (isMakableDrink) {
        imageView.visibility = ImageView.VISIBLE
    }
    else {
        imageView.visibility = ImageView.INVISIBLE
    }
}