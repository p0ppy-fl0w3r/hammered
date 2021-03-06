package com.fl0w3r.hammered.ingredients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fl0w3r.hammered.databinding.AllIngredientItemBinding
import com.fl0w3r.hammered.databinding.CartIngredientItemBinding
import com.fl0w3r.hammered.databinding.EmptyItemBinding
import com.fl0w3r.hammered.databinding.StockIngredientItemBinding
import com.fl0w3r.hammered.entities.relations.IngredientWithCocktail

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

class EmptyItemViewHolder(private val binding: EmptyItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind() {
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): EmptyItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = EmptyItemBinding.inflate(inflater, parent, false)

            return EmptyItemViewHolder(binding)
        }
    }
}