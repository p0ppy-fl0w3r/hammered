package com.example.hammered.cocktail.cocktailDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hammered.databinding.DetailsIngredientItemBinding
import com.example.hammered.ingredients.IngredientData
import com.example.hammered.wrappers.RefItemWrapper

class CocktailDetailsAdapter(private val clickListener: CocktailDetailsClickListener) :
    ListAdapter<RefItemWrapper<IngredientData>, CocktailDetailsViewHolder>(DetailsDiffUtils()) {

    class DetailsDiffUtils : DiffUtil.ItemCallback<RefItemWrapper<IngredientData>>() {
        override fun areItemsTheSame(
            oldItem: RefItemWrapper<IngredientData>,
            newItem: RefItemWrapper<IngredientData>
        ): Boolean {
            return oldItem.item.ingredient_id == newItem.item.ingredient_id
        }

        override fun areContentsTheSame(
            oldItem: RefItemWrapper<IngredientData>,
            newItem: RefItemWrapper<IngredientData>
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

    fun bind(refIngredient: RefItemWrapper<IngredientData>, clickListener: CocktailDetailsClickListener) {
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

class CocktailDetailsClickListener(val clickListener: (RefItemWrapper<IngredientData>) -> Unit) {
    fun onClick(refIngredient: RefItemWrapper<IngredientData>) = clickListener(refIngredient)
}