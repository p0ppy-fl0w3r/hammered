package com.example.hammered.cocktail.createCocktail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hammered.R
import com.example.hammered.databinding.CreateCocktailIngredientItemBinding
import com.example.hammered.wrappers.NewCocktailRef


class CreateCocktailAdapter(
    private val onClickListener: ItemOnClickListener,
    var arrayAdapter: ArrayAdapter<String>
) :
    ListAdapter<NewCocktailRef, CocktailIngredientViewHolder>(ItemDiffUtil()) {


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
        item: NewCocktailRef,
        clickListener: ItemOnClickListener,
        arrayAdapter: ArrayAdapter<String>
    ) {
        binding.ingredientItem = item
        binding.clickListener = clickListener

        // Setting adapters for autofill
        binding.RefIngredientName.threshold = 2
        binding.RefIngredientName.setAdapter(arrayAdapter)

        // Creating and attaching adapter for spinner
        ArrayAdapter.createFromResource(
            binding.cocktailIngRecyclerUnits.context,
            R.array.units_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.cocktailIngRecyclerUnits.adapter = adapter
        }

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

class ItemDiffUtil : DiffUtil.ItemCallback<NewCocktailRef>() {
    override fun areItemsTheSame(oldItem: NewCocktailRef, newItem: NewCocktailRef): Boolean {
        return oldItem.ref_number == newItem.ref_number
    }

    override fun areContentsTheSame(oldItem: NewCocktailRef, newItem: NewCocktailRef): Boolean {
        return newItem == oldItem
    }
}


class ItemOnClickListener(val listener: (itemNumber: Int) -> Unit) {
    fun onItemClick(itemNumber: Int) = listener(itemNumber)
}

