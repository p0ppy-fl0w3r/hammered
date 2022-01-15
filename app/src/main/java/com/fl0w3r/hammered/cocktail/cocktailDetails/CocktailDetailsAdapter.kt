package com.fl0w3r.hammered.cocktail.cocktailDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fl0w3r.hammered.databinding.DetailsIngredientItemBinding
import com.fl0w3r.hammered.entities.Ingredient
import com.fl0w3r.hammered.ingredients.IngredientData
import com.fl0w3r.hammered.wrappers.RefItemWrapper

class CocktailDetailsAdapter(private val clickListener: CocktailDetailsClickListener) :
    ListAdapter<RefItemWrapper<Ingredient>, CocktailDetailsViewHolder>(DetailsDiffUtils()) {

    class DetailsDiffUtils : DiffUtil.ItemCallback<RefItemWrapper<Ingredient>>() {
        override fun areItemsTheSame(
            oldItem: RefItemWrapper<Ingredient>,
            newItem: RefItemWrapper<Ingredient>
        ): Boolean {
            return oldItem.item.ingredient_id == newItem.item.ingredient_id
        }

        override fun areContentsTheSame(
            oldItem: RefItemWrapper<Ingredient>,
            newItem: RefItemWrapper<Ingredient>
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

    fun bind(refIngredient: RefItemWrapper<Ingredient>, clickListener: CocktailDetailsClickListener) {
        binding.refIngredient = refIngredient
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

class CocktailDetailsClickListener(val clickListener: (RefItemWrapper<Ingredient>) -> Unit) {
    fun onClick(refIngredient: RefItemWrapper<Ingredient>) = clickListener(refIngredient)
}