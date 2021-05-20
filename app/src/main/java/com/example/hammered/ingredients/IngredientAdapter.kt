package com.example.hammered.ingredients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hammered.databinding.MyIngredientItemBinding
import com.example.hammered.entities.Ingredient

// Create a sealed class for different layouts

class IngredientAdapter(private val clickListener: IngredientClickListener): ListAdapter< Ingredient,MyIngredientViewHolder>(ItemsDiffUtils()) {

    class ItemsDiffUtils():DiffUtil.ItemCallback<Ingredient>(){
        override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return newItem.ingredient_name == oldItem.ingredient_name
        }

        override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyIngredientViewHolder {
        return MyIngredientViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyIngredientViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(clickListener, currentItem)
    }


}

class MyIngredientViewHolder(private val binding: MyIngredientItemBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(clickListener: IngredientClickListener, ingredient: Ingredient){
        binding.ingredient = ingredient
        binding.clickListener = clickListener

        binding.executePendingBindings()
    }

    companion object{
        fun from(parent: ViewGroup): MyIngredientViewHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = MyIngredientItemBinding.inflate(inflater,parent, false)

            return MyIngredientViewHolder(binding)
        }
    }
}

class IngredientClickListener(val clickListener: (Ingredient) -> Unit){
    fun onClick(ingredient: Ingredient) = clickListener(ingredient)
}