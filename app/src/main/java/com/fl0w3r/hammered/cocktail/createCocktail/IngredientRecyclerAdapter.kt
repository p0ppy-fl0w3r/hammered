package com.fl0w3r.hammered.cocktail.createCocktail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.databinding.CreateCocktailIngredientItemBinding
import com.fl0w3r.hammered.entities.relations.IngredientCocktailRef


class CreateCocktailAdapter(
    private val onClickListener: ItemOnClickListener,
    var arrayAdapter: ArrayAdapter<String>
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
        holder.bind(item, onClickListener, arrayAdapter)
    }
}

class CocktailIngredientViewHolder(private val binding: CreateCocktailIngredientItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: IngredientCocktailRef,
        clickListener: ItemOnClickListener,
        arrayAdapter: ArrayAdapter<String>
    ) {
        binding.ingredientItem = item
        binding.clickListener = clickListener

        // Setting adapters for autofill
        // TODO move these.
//        binding.RefIngredientName.threshold = 2
//        binding.RefIngredientName.setAdapter(arrayAdapter)

        // Creating and attaching adapter for spinner
        // TODO move these too.
//        ArrayAdapter.createFromResource(
//            binding.cocktailIngRecyclerUnits.context,
//            R.array.units_array,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            binding.cocktailIngRecyclerUnits.adapter = adapter
//        }

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
    override fun areItemsTheSame(oldItem: IngredientCocktailRef, newItem: IngredientCocktailRef): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: IngredientCocktailRef, newItem: IngredientCocktailRef): Boolean {
        return newItem == oldItem
    }
}


class ItemOnClickListener(val listener: (itemNumber: Int) -> Unit) {
    fun onItemClick(itemNumber: Int) = listener(itemNumber)
}

