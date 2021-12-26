package com.fl0w3r.hammered.mixer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.databinding.MixerIngredientItemBinding


class MixerIngredientAdapter(private val clickListener: MixerIngredientClickListener) :
    ListAdapter<IngredientMixerItem, MixerIngredientAdapter.MixerIngredientViewHolder>(
        MixerIngredientDiffUtils()
    ) {

    class MixerIngredientDiffUtils : DiffUtil.ItemCallback<IngredientMixerItem>() {
        override fun areItemsTheSame(oldItem: IngredientMixerItem, newItem: IngredientMixerItem): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: IngredientMixerItem, newItem: IngredientMixerItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MixerIngredientViewHolder {
        return MixerIngredientViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MixerIngredientViewHolder, position: Int) {
        holder.bind(clickListener, getItem(position))
    }

    class MixerIngredientViewHolder(private val binding: MixerIngredientItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            clickListener: MixerIngredientClickListener,
            ingredient: IngredientMixerItem
        ) {
            binding.root.setOnClickListener { clickListener.onClick(ingredient = ingredient) }

            binding.ingredientName.text = ingredient.ingredientName
            binding.checkedImage.visibility =
                if(ingredient.isSelected) View.VISIBLE else View.INVISIBLE

            Glide.with(binding.ingredientImage).load(ingredient.ingredientImage).apply(
                RequestOptions().error(R.drawable.no_drinks)
            ).into(binding.ingredientImage)

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MixerIngredientViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = MixerIngredientItemBinding.inflate(inflater, parent, false)

                return MixerIngredientViewHolder(binding)
            }
        }
    }

}

class MixerIngredientClickListener(val clickListener: (IngredientMixerItem) -> Unit) {
    fun onClick(ingredient: IngredientMixerItem) {
        clickListener(ingredient)
    }
}