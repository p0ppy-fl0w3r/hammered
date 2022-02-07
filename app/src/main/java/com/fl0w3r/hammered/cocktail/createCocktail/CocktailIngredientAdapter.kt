package com.fl0w3r.hammered.cocktail.createCocktail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.databinding.CreateCocktailIngredientItemBinding
import com.fl0w3r.hammered.entities.relations.IngredientCocktailRef


class CocktailIngredientAdapter(
    private val onDeleteListener: ItemOnClickListener,
    private val onEditListener: ItemOnClickListener
) :
    ListAdapter<IngredientCocktailRef, CocktailIngredientViewHolder>(ItemDiffUtil()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CocktailIngredientViewHolder {
        return CocktailIngredientViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CocktailIngredientViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position, onEditListener, onDeleteListener)
    }
}

class CocktailIngredientViewHolder(private val binding: CreateCocktailIngredientItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: IngredientCocktailRef,
        position: Int,
        onEditListener: ItemOnClickListener,
        onDeleteListener: ItemOnClickListener,
    ) {
        binding.ingredientItem = item

        binding.editIngredient.setOnClickListener { onEditListener.listener(position, item) }
        binding.deleteCurrentIngredient.setOnClickListener { onDeleteListener.listener(position, item) }

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): CocktailIngredientViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<CreateCocktailIngredientItemBinding>(
                layoutInflater,
                R.layout.create_cocktail_ingredient_item,
                parent, false
            )
            return CocktailIngredientViewHolder(binding)
        }
    }
}

class ItemDiffUtil : DiffUtil.ItemCallback<IngredientCocktailRef>() {
    override fun areItemsTheSame(
        oldItem: IngredientCocktailRef,
        newItem: IngredientCocktailRef
    ): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: IngredientCocktailRef,
        newItem: IngredientCocktailRef
    ): Boolean {
        return newItem == oldItem
    }
}


class ItemOnClickListener(val listener: (itemNumber: Int, ingredient: IngredientCocktailRef) -> Unit) {
    fun onItemClick(itemNumber: Int, ingredient: IngredientCocktailRef) =
        listener(itemNumber, ingredient)
}

