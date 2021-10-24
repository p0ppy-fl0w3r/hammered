package com.fl0w3r.hammered.cocktail

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fl0w3r.hammered.Constants
import com.fl0w3r.hammered.entities.relations.CocktailWithIngredient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException


class CocktailAdapter(private val clickListener: CocktailClickListener) :
    ListAdapter<CocktailItem, RecyclerView.ViewHolder>(ItemsDiffUtils()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    class ItemsDiffUtils() : DiffUtil.ItemCallback<CocktailItem>() {
        override fun areItemsTheSame(oldItem: CocktailItem, newItem: CocktailItem): Boolean {
            return newItem.id == oldItem.id
        }


        override fun areContentsTheSame(
            oldItem: CocktailItem,
            newItem: CocktailItem
        ): Boolean {
            return oldItem == newItem
        }

    }

    fun applyFilterAndSubmitList(list: List<CocktailWithIngredient>?, filterVal: Int) {
        adapterScope.launch {
            if (!list.isNullOrEmpty()) {
                val items = when (filterVal) {
                    Constants.NORMAL_COCKTAIL_ITEM -> list.map { CocktailItem.NormalCocktailItem(it) }
                    Constants.AVAILABLE_COCKTAIL_ITEM -> list.map {
                        CocktailItem.AvailableCocktailItem(
                            it
                        )
                    }
                    Constants.FAVORITE_COCKTAIL_ITEM -> list.map {
                        CocktailItem.FavoriteCocktailItem(
                            it
                        )
                    }
                    else -> throw IllegalArgumentException("Filter val not found: $filterVal")
                }
                withContext(Dispatchers.Main) {
                    submitList(items)
                }
            }
            else {
                val emptyItem = CocktailItem.EmptyListItem()
                withContext(Dispatchers.Main) {
                    submitList(listOf(emptyItem))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.NORMAL_COCKTAIL_ITEM -> AllCocktailViewHolder.from(parent)
            Constants.AVAILABLE_COCKTAIL_ITEM -> AvailableCocktailViewHolder.from(parent)
            Constants.FAVORITE_COCKTAIL_ITEM -> FavoriteCocktailViewHolder.from(parent)

            else -> EmptyItemViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        when (holder) {
            is AllCocktailViewHolder -> {
                val item = currentItem as CocktailItem.NormalCocktailItem
                holder.bind(clickListener, item.cocktail)
            }

            is AvailableCocktailViewHolder -> {
                val item = currentItem as CocktailItem.AvailableCocktailItem
                holder.bind(clickListener, item.cocktail)
            }

            is FavoriteCocktailViewHolder -> {
                val item = currentItem as CocktailItem.FavoriteCocktailItem
                holder.bind(clickListener, item.cocktail)
            }

            is EmptyItemViewHolder -> {
                holder.bind()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CocktailItem.NormalCocktailItem -> Constants.NORMAL_COCKTAIL_ITEM
            is CocktailItem.AvailableCocktailItem -> Constants.AVAILABLE_COCKTAIL_ITEM
            is CocktailItem.FavoriteCocktailItem -> Constants.FAVORITE_COCKTAIL_ITEM
            is CocktailItem.EmptyListItem -> Constants.EMPTY_ITEM
        }
    }
}