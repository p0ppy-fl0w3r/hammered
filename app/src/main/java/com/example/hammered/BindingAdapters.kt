package com.example.hammered

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.hammered.entities.Cocktail
import com.example.hammered.entities.Ingredient
import com.example.hammered.entities.relations.IngredientCocktailRef
import com.example.hammered.ingredients.IngredientData

@BindingAdapter("imageSource")
fun imageSource(imageView: ImageView, imageUrl: String?) {
    imageUrl?.let {
        Glide.with(imageView.context)
            .load(Uri.parse("$imageUrl"))
            .apply(RequestOptions().error(R.drawable.no_drinks))
            .into(imageView)
    }
}

@BindingAdapter("ingredientList")
fun ingredientList(textView: TextView, ingredients: List<Ingredient>) {
    if (!ingredients.isNullOrEmpty()) {
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

@BindingAdapter("isInStock")
fun isInStock(imageView: ImageView, ingredient: IngredientData) {
    if (ingredient.inStock) {
        imageView.visibility = ImageView.VISIBLE
    }
    else {
        imageView.visibility = ImageView.INVISIBLE
    }
}

@BindingAdapter("detailCocktailFavourite")
fun detailCocktailFavourite(imageView: ImageView, cocktail: Cocktail) {
    if (cocktail.isFavorite) {
        Glide.with(imageView.context).load(R.drawable.star).into(imageView)
    }
    else {
        Glide.with(imageView.context).load(R.drawable.star_unselect).into(imageView)
    }
}

@BindingAdapter("quantity")
fun quantity(textView: TextView, ingredientRef: IngredientCocktailRef) {

    val refRem = ingredientRef.quantity % 1
    if (refRem <= 0.01f) {
        textView.text = textView.context.getString(
            R.string.quantity,
            ingredientRef.quantity.toInt(),
            ingredientRef.quantityUnit
        )
    }
    else {
        textView.text = textView.context.getString(
            R.string.quantity_f,
            ingredientRef.quantity,
            ingredientRef.quantityUnit
        )
    }
}

@BindingAdapter("cocktailsFromIngredients")
fun cocktailsFromIngredients(textView: TextView, cocktails: List<Cocktail>) {
    if (cocktails.isNotEmpty()) {
        if (cocktails.size < 3) {
            val cocktailLast = cocktails.last()

            val cocktailsFinal = if (cocktails.size > 1) {

                cocktails.subList(0, cocktails.size - 1).joinToString(
                    separator = ", ",
                    postfix = " & ${cocktailLast.cocktail_name}"
                ) {
                    it.cocktail_name
                }
            }
            else {
                cocktailLast.cocktail_name
            }

            textView.text = textView.context.getString(R.string.used_in_msg, cocktailsFinal)
        }
        else {
            textView.text = textView.context.getString(R.string.used_in_num_msg, cocktails.size)
        }
    }
}

@BindingAdapter("detailInCart")
fun detailInCart(imageView: ImageView, inCart: Boolean) {
    if (inCart) {
        Glide.with(imageView.context).load(R.drawable.ic_cart_filled).into(imageView)
    }
    else {
        Glide.with(imageView.context).load(R.drawable.ic_cart_empty).into(imageView)
    }
}

@BindingAdapter("usedInText")
fun usedInText(textView: TextView, name: String) {
    textView.text = textView.context.getString(R.string.used_in, name)
}