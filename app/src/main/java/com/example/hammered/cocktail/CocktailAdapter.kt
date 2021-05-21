package com.example.hammered.cocktail


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hammered.databinding.CocktailItemBinding
import com.example.hammered.entities.relations.CocktailWithIngredient


// Create a sealed class for different layouts

class CocktailAdapter(private val clickListener: CocktailClickListener): ListAdapter< CocktailWithIngredient,MyCocktailViewHolder>(ItemsDiffUtils()) {

    class ItemsDiffUtils():DiffUtil.ItemCallback<CocktailWithIngredient>(){
        override fun areItemsTheSame(oldItem: CocktailWithIngredient, newItem: CocktailWithIngredient): Boolean {
            return newItem.cocktail.cocktail_id == oldItem.cocktail.cocktail_id
        }


        override fun areContentsTheSame(
            oldItem: CocktailWithIngredient,
            newItem: CocktailWithIngredient
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCocktailViewHolder {
        return MyCocktailViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyCocktailViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(clickListener, currentItem)
    }


}

class MyCocktailViewHolder(private val binding: CocktailItemBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(clickListener: CocktailClickListener, cocktailWithIngredient: CocktailWithIngredient){
        binding.cocktailWithIngredient = cocktailWithIngredient
        binding.clickListener = clickListener

        binding.executePendingBindings()
    }

    companion object{
        fun from(parent: ViewGroup): MyCocktailViewHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = CocktailItemBinding.inflate(inflater,parent, false)

            return MyCocktailViewHolder(binding)
        }
    }
}

class CocktailClickListener(val clickListener: (CocktailWithIngredient) -> Unit){
    fun onClick(cocktailWithIngredient: CocktailWithIngredient) = clickListener(cocktailWithIngredient)
}