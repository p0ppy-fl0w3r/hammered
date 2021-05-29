package com.example.hammered.ingredients

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hammered.Constants
import com.example.hammered.entities.relations.IngredientWithCocktail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException

// TODO add layout for empty list
class IngredientAdapter(
    private val clickListener: IngredientClickListener,
    private val itemStatusChangeListener: ItemStatusChangeListener
) :
    ListAdapter<IngredientItem, RecyclerView.ViewHolder>(ItemsDiffUtils()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    class ItemsDiffUtils : DiffUtil.ItemCallback<IngredientItem>() {
        override fun areItemsTheSame(
            oldItem: IngredientItem,
            newItem: IngredientItem
        ): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(
            oldItem: IngredientItem,
            newItem: IngredientItem
        ): Boolean {
            return oldItem == newItem
        }

    }

    fun addFilterAndSubmitList(list: List<IngredientWithCocktail>?, filterVal: Int) {
        adapterScope.launch {
            // TODO add condition for empty list
            val items = when (filterVal) {
                Constants.NORMAL_ITEM -> list?.map { IngredientItem.NormalIngredientItem(it) }
                Constants.ITEM_IN_STOCK -> list?.map { IngredientItem.IngredientInStock(it) }
                Constants.ITEM_IN_CART -> list?.map { IngredientItem.IngredientInCart(it) }
                else -> throw IllegalArgumentException("Filter val not found: $filterVal")
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.NORMAL_ITEM -> AllIngredientViewHolder.from(parent)
            Constants.ITEM_IN_STOCK -> StockIngredientViewHolder.from(parent)
            Constants.ITEM_IN_CART -> CartIngredientViewHolder.from(parent)
            else -> throw ClassNotFoundException("View class not found $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        when (holder) {

            is AllIngredientViewHolder -> {
                val allIngredient = currentItem as IngredientItem.NormalIngredientItem
                holder.bind(clickListener, itemStatusChangeListener, allIngredient.ingredient)
            }

            is StockIngredientViewHolder -> {
                val stockIngredient = currentItem as IngredientItem.IngredientInStock
                holder.bind(clickListener, stockIngredient.ingredient)
            }

            is CartIngredientViewHolder -> {
                val cartIngredient = currentItem as IngredientItem.IngredientInCart
                holder.bind(clickListener, itemStatusChangeListener, cartIngredient.ingredient)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is IngredientItem.NormalIngredientItem -> Constants.NORMAL_ITEM
            is IngredientItem.IngredientInStock -> Constants.ITEM_IN_STOCK
            is IngredientItem.IngredientInCart -> Constants.ITEM_IN_CART
        }
    }
}