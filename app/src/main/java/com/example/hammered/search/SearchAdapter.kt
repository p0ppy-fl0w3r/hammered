package com.example.hammered.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hammered.Constants
import com.example.hammered.R
import com.example.hammered.databinding.SearchCocktailItemBinding
import com.example.hammered.databinding.SearchIngredientItemBinding
import com.example.hammered.entities.relations.CocktailWithIngredient
import com.example.hammered.entities.relations.IngredientWithCocktail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.IllegalArgumentException

class SearchAdapter(val clickListener: SearchItemClickListener) :
    ListAdapter<SearchDataItem, RecyclerView.ViewHolder>(SearchItemDiff()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    class SearchItemDiff : DiffUtil.ItemCallback<SearchDataItem>() {
        override fun areItemsTheSame(oldItem: SearchDataItem, newItem: SearchDataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SearchDataItem, newItem: SearchDataItem): Boolean {
            return oldItem == newItem
        }
    }

    fun addFilterAndSubmitList(list: List<Any>) {
        adapterScope.launch {
            if (!list.isNullOrEmpty()) {
                val items = mutableListOf<SearchDataItem>()

                for (mItems in list) {
                    if (mItems is IngredientWithCocktail) {
                        items.add(SearchDataItem.SearchIngredientDataItem(mItems))
                    }
                    else {
                        items.add(SearchDataItem.SearchCocktailDataItem(mItems as CocktailWithIngredient))
                    }
                }

                withContext(Dispatchers.Main) {
                    submitList(items)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.SEARCH_COCKTAIL -> SearchCocktailViewHolder.from(parent)
            Constants.SEARCH_INGREDIENT -> SearchIngredientViewHolder.from(parent)
            else -> throw IllegalArgumentException("The viewType is invalid: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        when (holder) {

            is SearchIngredientViewHolder -> holder.bind(
                (currentItem as SearchDataItem.SearchIngredientDataItem).ingredientWithCocktail,
                clickListener
            )
            is SearchCocktailViewHolder -> holder.bind(
                (currentItem as SearchDataItem.SearchCocktailDataItem).cocktailWithIngredient,
                clickListener
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SearchDataItem.SearchCocktailDataItem -> Constants.SEARCH_COCKTAIL
            is SearchDataItem.SearchIngredientDataItem -> Constants.SEARCH_INGREDIENT
        }
    }
}

class SearchCocktailViewHolder(private val binding: SearchCocktailItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(cocktail: CocktailWithIngredient, clickListener: SearchItemClickListener) {
        binding.cocktailWithIngredient = cocktail
        binding.cocktailSearchClickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): SearchCocktailViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<SearchCocktailItemBinding>(
                layoutInflater,
                R.layout.search_cocktail_item,
                parent,
                false
            )
            return SearchCocktailViewHolder(binding)
        }
    }
}

class SearchIngredientViewHolder(private val binding: SearchIngredientItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(ingredient: IngredientWithCocktail, clickListener: SearchItemClickListener) {
        binding.ingredientWithCocktail = ingredient
        binding.ingredientSearchClickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): SearchIngredientViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<SearchIngredientItemBinding>(
                layoutInflater,
                R.layout.search_ingredient_item,
                parent,
                false
            )
            return SearchIngredientViewHolder(binding)
        }
    }
}

sealed class SearchDataItem {
    abstract val id: String

    data class SearchCocktailDataItem(val cocktailWithIngredient: CocktailWithIngredient) :
        SearchDataItem() {
        override val id =
            cocktailWithIngredient.cocktail.cocktail_name + cocktailWithIngredient.cocktail.cocktail_id.toString()
    }

    data class SearchIngredientDataItem(val ingredientWithCocktail: IngredientWithCocktail) :
        SearchDataItem() {
        override val id: String
            get() = ingredientWithCocktail.ingredient.ingredient_name
    }
}

class SearchItemClickListener(val listener: (Any) -> Unit) {
    fun onClick(item: Any) = listener(item)
}