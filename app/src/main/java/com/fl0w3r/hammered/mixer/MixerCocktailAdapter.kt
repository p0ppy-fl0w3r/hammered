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
import com.fl0w3r.hammered.cocktail.CocktailData
import com.fl0w3r.hammered.databinding.MixerCocktailItemBinding
import com.fl0w3r.hammered.entities.Cocktail


class MixerCocktailAdapter(private val clickListener: MixerCocktailClickListener) :
    ListAdapter<Cocktail, MixerCocktailAdapter.MixerCocktailViewHolder>(
        MixerCocktailDiffUtils()
    ) {

    class MixerCocktailDiffUtils : DiffUtil.ItemCallback<Cocktail>() {
        override fun areItemsTheSame(oldItem: Cocktail, newItem: Cocktail): Boolean {
            return newItem.cocktail_id == oldItem.cocktail_id
        }

        override fun areContentsTheSame(oldItem: Cocktail, newItem: Cocktail): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MixerCocktailViewHolder {
        return MixerCocktailViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MixerCocktailViewHolder, position: Int) {
        holder.bind(clickListener, getItem(position))
    }

    class MixerCocktailViewHolder(private val binding: MixerCocktailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            clickListener: MixerCocktailClickListener,
            cocktail: Cocktail
        ) {
            binding.root.setOnClickListener { clickListener.onClick(cocktail = cocktail.asData()) }

            binding.cocktailName.text = cocktail.cocktail_name

            Glide.with(binding.cocktailImage).load(cocktail.cocktail_image).apply(
                RequestOptions().error(R.drawable.no_drinks)
            ).into(binding.cocktailImage)

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MixerCocktailViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = MixerCocktailItemBinding.inflate(inflater, parent, false)

                return MixerCocktailViewHolder(binding)
            }
        }
    }

}

class MixerCocktailClickListener(val clickListener: (CocktailData) -> Unit) {
    fun onClick(cocktail: CocktailData) {
        clickListener(cocktail)
    }
}