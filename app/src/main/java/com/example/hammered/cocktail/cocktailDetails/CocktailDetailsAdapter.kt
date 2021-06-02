package com.example.hammered.cocktail.cocktailDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hammered.databinding.DetailsIngredientItemBinding

class CocktailDetailsAdapter(private val clickListener: CocktailDetailsClickListener) :
    ListAdapter<RefIngredientWrapper, CocktailDetailsViewHolder>(DetailsDiffUtils()) {

    class DetailsDiffUtils : DiffUtil.ItemCallback<RefIngredientWrapper>() {
        override fun areItemsTheSame(
            oldItem: RefIngredientWrapper,
            newItem: RefIngredientWrapper
        ): Boolean {
            return oldItem.ingredient.ingredient_name == newItem.ingredient.ingredient_name
        }

        override fun areContentsTheSame(
            oldItem: RefIngredientWrapper,
            newItem: RefIngredientWrapper
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocktailDetailsViewHolder {
        return CocktailDetailsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CocktailDetailsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

}

class CocktailDetailsViewHolder private constructor(private val binding: DetailsIngredientItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(refIngredient: RefIngredientWrapper, clickListener: CocktailDetailsClickListener) {
        binding.ingredientRef = refIngredient
        binding.clickListener = clickListener

        binding.executePendingBindings()

    }

    companion object {
        fun from(parent: ViewGroup): CocktailDetailsViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DetailsIngredientItemBinding.inflate(inflater, parent, false)
            return CocktailDetailsViewHolder(binding)
        }
    }
}

class CocktailDetailsClickListener(val clickListener: (RefIngredientWrapper) -> Unit) {
    fun onClick(refIngredient: RefIngredientWrapper) = clickListener(refIngredient)
}