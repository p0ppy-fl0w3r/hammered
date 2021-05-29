package com.example.hammered.ingredients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hammered.databinding.AllIngredientItemBinding
import com.example.hammered.databinding.CartIngredientItemBinding
import com.example.hammered.databinding.StockIngredientItemBinding
import com.example.hammered.entities.relations.IngredientWithCocktail

class AllIngredientViewHolder(private val binding: AllIngredientItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        clickListener: IngredientClickListener,
        itemStatusChangeListener: ItemStatusChangeListener,
        ingredient: IngredientWithCocktail
    ) {
        binding.ingredientWithCocktail = ingredient
        binding.clickListener = clickListener
        binding.itemStockChangeListener = itemStatusChangeListener

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): AllIngredientViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = AllIngredientItemBinding.inflate(inflater, parent, false)

            return AllIngredientViewHolder(binding)
        }
    }
}

class StockIngredientViewHolder(private val binding: StockIngredientItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(clickListener: IngredientClickListener, ingredient: IngredientWithCocktail) {
        binding.ingredientWithCocktail = ingredient
        binding.clickListener = clickListener

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): StockIngredientViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = StockIngredientItemBinding.inflate(inflater, parent, false)

            return StockIngredientViewHolder(binding)
        }
    }
}

class CartIngredientViewHolder(private val binding: CartIngredientItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        clickListener: IngredientClickListener,
        itemStatusChangeListener: ItemStatusChangeListener,
        ingredient: IngredientWithCocktail
    ) {
        binding.ingredientWithCocktail = ingredient
        binding.clickListener = clickListener
        binding.cartStatusChangeListener = itemStatusChangeListener

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): CartIngredientViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = CartIngredientItemBinding.inflate(inflater, parent, false)

            return CartIngredientViewHolder(binding)
        }
    }
}