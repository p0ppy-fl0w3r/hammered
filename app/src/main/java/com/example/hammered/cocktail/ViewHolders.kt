package com.example.hammered.cocktail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hammered.databinding.AllCocktailItemBinding
import com.example.hammered.databinding.AvailableCocktailItemBinding
import com.example.hammered.databinding.EmptyItemBinding
import com.example.hammered.databinding.FavoriteCocktailItemBinding
import com.example.hammered.entities.relations.CocktailWithIngredient

class AllCocktailViewHolder(private val binding: AllCocktailItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(clickListener: CocktailClickListener, cocktailWithIngredient: CocktailWithIngredient) {
        binding.cocktailWithIngredient = cocktailWithIngredient
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): AllCocktailViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = AllCocktailItemBinding.inflate(inflater, parent, false)

            return AllCocktailViewHolder(binding)
        }
    }
}

class AvailableCocktailViewHolder(private val binding: AvailableCocktailItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(clickListener: CocktailClickListener, cocktailWithIngredient: CocktailWithIngredient) {
        binding.cocktailWithIngredient = cocktailWithIngredient
        binding.clickListener = clickListener

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): AvailableCocktailViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = AvailableCocktailItemBinding.inflate(inflater, parent, false)

            return AvailableCocktailViewHolder(binding)
        }
    }
}

class FavoriteCocktailViewHolder(private val binding: FavoriteCocktailItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(clickListener: CocktailClickListener, cocktailWithIngredient: CocktailWithIngredient) {
        binding.cocktailWithIngredient = cocktailWithIngredient
        binding.clickListener = clickListener

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): FavoriteCocktailViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = FavoriteCocktailItemBinding.inflate(inflater, parent, false)

            return FavoriteCocktailViewHolder(binding)
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