package com.example.hammered.ingredients.ingredientDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hammered.cocktail.CocktailData
import com.example.hammered.databinding.DetailsCocktailItemBinding
import com.example.hammered.wrappers.RefItemWrapper

class IngredientDetailsAdapter(private val clickListener: IngredientDetailsClickListener) :
    ListAdapter<RefItemWrapper<CocktailData>, IngredientDetailsViewHolder>(DetailsDiffUtils()) {

    class DetailsDiffUtils : DiffUtil.ItemCallback<RefItemWrapper<CocktailData>>() {
        override fun areItemsTheSame(
            oldItem: RefItemWrapper<CocktailData>,
            newItem: RefItemWrapper<CocktailData>
        ): Boolean {
            return oldItem.item.cocktail_id == newItem.item.cocktail_id
        }

        override fun areContentsTheSame(
            oldItem: RefItemWrapper<CocktailData>,
            newItem: RefItemWrapper<CocktailData>
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
        refCocktail: RefItemWrapper<CocktailData>,
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

class IngredientDetailsClickListener(val clickListener: (RefItemWrapper<CocktailData>) -> Unit) {
    fun onClick(refCocktail: RefItemWrapper<CocktailData>) = clickListener(refCocktail)
}
