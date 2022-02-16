package com.fl0w3r.hammered.cocktail.cocktailDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fl0w3r.hammered.databinding.SlideItemBinding
import com.fl0w3r.hammered.entities.Ingredient
import com.fl0w3r.hammered.wrappers.RefItemWrapper

class SlideRecyclerAdapter() :
    ListAdapter<RefItemWrapper<Ingredient>, StepSlideViewHolder>(SlideItemDiff()) {
    class SlideItemDiff() : DiffUtil.ItemCallback<RefItemWrapper<Ingredient>>() {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepSlideViewHolder {
        return StepSlideViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: StepSlideViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class StepSlideViewHolder(private val slideItemBinding: SlideItemBinding) :
    RecyclerView.ViewHolder(slideItemBinding.root) {
    fun bind(refIngredient: RefItemWrapper<Ingredient>) {
        slideItemBinding.ingredientItem = refIngredient

        slideItemBinding.executePendingBindings()

    }

    companion object {
        fun from(parent: ViewGroup): StepSlideViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = SlideItemBinding.inflate(inflater, parent, false)
            return StepSlideViewHolder(binding)
        }
    }
}