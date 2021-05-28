package com.example.hammered.cocktail


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hammered.databinding.AllCocktailItemBinding
import com.example.hammered.databinding.AvailableCocktailItemBinding
import com.example.hammered.databinding.FavoriteCocktailItemBinding
import com.example.hammered.entities.relations.CocktailWithIngredient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.IllegalArgumentException


// Create a sealed class for different layouts

private const val NORMAL_COCKTAIL_ITEM = 1
private const val AVAILABLE_COCKTAIL_ITEM = 2
private const val FAVORITE_COCKTAIL_ITEM = 3

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
                    NORMAL_COCKTAIL_ITEM -> list.map { CocktailItem.NormalCocktailItem(it) }
                    AVAILABLE_COCKTAIL_ITEM -> list.map { CocktailItem.AvailableCocktailItem(it) }
                    FAVORITE_COCKTAIL_ITEM -> list.map { CocktailItem.FavoriteCocktailItem(it) }
                    else -> throw IllegalArgumentException("Filter val not found: $filterVal")
                }
                withContext(Dispatchers.Main){
                    submitList(items)
                }
            }
            else{
                Timber.e("The list was empty or null $filterVal")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            NORMAL_COCKTAIL_ITEM -> AllCocktailViewHolder.from(parent)
            AVAILABLE_COCKTAIL_ITEM -> AvailableCocktailViewHolder.from(parent)
            FAVORITE_COCKTAIL_ITEM -> FavoriteCocktailViewHolder.from(parent)
            else -> throw ClassNotFoundException("View Class not found $viewType")
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
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CocktailItem.NormalCocktailItem -> NORMAL_COCKTAIL_ITEM
            is CocktailItem.AvailableCocktailItem -> AVAILABLE_COCKTAIL_ITEM
            is CocktailItem.FavoriteCocktailItem -> FAVORITE_COCKTAIL_ITEM
        }
    }
}

class AllCocktailViewHolder(private val binding: AllCocktailItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(clickListener: CocktailClickListener, cocktailWithIngredient: CocktailWithIngredient) {
        binding.cocktailWithIngredient = cocktailWithIngredient
        binding.clickListener = clickListener

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): AllCocktailViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = AllCocktailItemBinding.inflate(inflater, parent, false)

            return AllCocktailViewHolder(binding)
        }
    }
}

class AvailableCocktailViewHolder(private val binding: AvailableCocktailItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(clickListener: CocktailClickListener, cocktailWithIngredient: CocktailWithIngredient) {
        binding.cocktailWithIngredient = cocktailWithIngredient
        binding.clickListener = clickListener

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): AvailableCocktailViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = AvailableCocktailItemBinding.inflate(inflater, parent, false)

            return AvailableCocktailViewHolder(binding)
        }
    }
}

class FavoriteCocktailViewHolder(private val binding: FavoriteCocktailItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(clickListener: CocktailClickListener, cocktailWithIngredient: CocktailWithIngredient) {
        binding.cocktailWithIngredient = cocktailWithIngredient
        binding.clickListener = clickListener

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): FavoriteCocktailViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = FavoriteCocktailItemBinding.inflate(inflater, parent, false)

            return FavoriteCocktailViewHolder(binding)
        }
    }
}

class CocktailClickListener(val clickListener: (CocktailWithIngredient) -> Unit) {
    fun onClick(cocktailWithIngredient: CocktailWithIngredient) =
        clickListener(cocktailWithIngredient)
}

sealed class CocktailItem {

    data class NormalCocktailItem(val cocktail: CocktailWithIngredient) : CocktailItem() {
        override val id = cocktail.cocktail.cocktail_id
    }

    data class AvailableCocktailItem(val cocktail: CocktailWithIngredient) : CocktailItem() {
        override val id = cocktail.cocktail.cocktail_id
    }

    data class FavoriteCocktailItem(val cocktail: CocktailWithIngredient) : CocktailItem() {
        override val id = cocktail.cocktail.cocktail_id
    }

    abstract val id: Long
}