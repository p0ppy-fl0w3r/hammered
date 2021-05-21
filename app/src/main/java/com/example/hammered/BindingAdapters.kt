package com.example.hammered

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.hammered.entities.Ingredient

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
    var ingredientString = ""
    for (i in ingredients) {
        ingredientString += i.ingredient_name + ", "
    }
    textView.text = ingredientString.dropLast(2)
}