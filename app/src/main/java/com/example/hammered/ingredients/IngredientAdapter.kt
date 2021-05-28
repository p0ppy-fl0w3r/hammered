package com.example.hammered.ingredients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hammered.databinding.MyIngredientItemBinding
import com.example.hammered.entities.relations.IngredientWithCocktail

// Create a sealed class for different layouts

class IngredientAdapter(private val clickListener: IngredientClickListener) :
    ListAdapter<IngredientWithCocktail, MyIngredientViewHolder>(ItemsDiffUtils()) {

    class ItemsDiffUtils() : DiffUtil.ItemCallback<IngredientWithCocktail>() {
        override fun areItemsTheSame(
            oldItem: IngredientWithCocktail,
            newItem: IngredientWithCocktail
        ): Boolean {
            return newItem.ingredient.ingredient_name == oldItem.ingredient.ingredient_name
        }

        override fun areContentsTheSame(
            oldItem: IngredientWithCocktail,
            newItem: IngredientWithCocktail
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyIngredientViewHolder {
        return MyIngredientViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyIngredientViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(clickListener, currentItem)
    }


}

class MyIngredientViewHolder(private val binding: MyIngredientItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(clickListener: IngredientClickListener, ingredient: IngredientWithCocktail) {
        binding.ingredientWithCocktail = ingredient
        binding.clickListener = clickListener

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): MyIngredientViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = MyIngredientItemBinding.inflate(inflater, parent, false)

            return MyIngredientViewHolder(binding)
        }
    }
}

class IngredientClickListener(val clickListener: (IngredientWithCocktail) -> Unit) {
    fun onClick(ingredient: IngredientWithCocktail) = clickListener(ingredient)
}