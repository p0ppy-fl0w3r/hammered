package com.example.hammered.ingredients.ingredientDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hammered.databinding.DetailsCocktailItemBinding
import com.example.hammered.entities.relations.CocktailWithIngredient

class IngredientDetailsAdapter(private val clickListener: IngredientDetailsClickListener) :
    ListAdapter<CocktailWithIngredient, IngredientDetailsViewHolder>(DetailsDiffUtils()) {

    class DetailsDiffUtils : DiffUtil.ItemCallback<CocktailWithIngredient>() {
        override fun areItemsTheSame(
            oldItem: CocktailWithIngredient,
            newItem: CocktailWithIngredient
        ): Boolean {
            return oldItem.cocktail.cocktail_id == newItem.cocktail.cocktail_id
        }

        override fun areContentsTheSame(
            oldItem: CocktailWithIngredient,
            newItem: CocktailWithIngredient
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientDetailsViewHolder {
        return IngredientDetailsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: IngredientDetailsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

}

class IngredientDetailsViewHolder private constructor(private val binding: DetailsCocktailItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        refCocktail: CocktailWithIngredient,
        clickListener: IngredientDetailsClickListener
    ) {
        binding.refCocktail = refCocktail
        binding.clickListener = clickListener

        binding.executePendingBindings()

    }

    companion object {
        fun from(parent: ViewGroup): IngredientDetailsViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DetailsCocktailItemBinding.inflate(inflater, parent, false)
            return IngredientDetailsViewHolder(binding)
        }
    }
}

class IngredientDetailsClickListener(val clickListener: (CocktailWithIngredient) -> Unit) {
    fun onClick(refCocktail: CocktailWithIngredient) = clickListener(refCocktail)
}
