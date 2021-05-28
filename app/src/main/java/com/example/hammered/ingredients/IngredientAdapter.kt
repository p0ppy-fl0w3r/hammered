package com.example.hammered.ingredients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hammered.databinding.AllIngredientItemBinding
import com.example.hammered.databinding.CartIngredientItemBinding
import com.example.hammered.databinding.StockIngredientItemBinding
import com.example.hammered.entities.relations.IngredientWithCocktail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.IllegalArgumentException

private const val NORMAL_ITEM = 1
private const val ITEM_IN_STOCK = 2
private const val ITEM_IN_CART = 3


class IngredientAdapter(private val clickListener: IngredientClickListener) :
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
            if (!list.isNullOrEmpty()) {
                val items = when (filterVal) {
                    NORMAL_ITEM -> list.map { IngredientItem.NormalIngredientItem(it) }
                    ITEM_IN_STOCK -> list.map { IngredientItem.IngredientInStock(it) }
                    ITEM_IN_CART -> list.map { IngredientItem.IngredientInCart(it) }
                    else -> throw IllegalArgumentException("Filter val not found: $filterVal")
                }
                withContext(Dispatchers.Main) {
                    submitList(items)
                }
            } else {
                Timber.e("The list is empty or null: Chip selected $filterVal")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            NORMAL_ITEM -> AllIngredientViewHolder.from(parent)
            ITEM_IN_STOCK -> StockIngredientViewHolder.from(parent)
            ITEM_IN_CART -> CartIngredientViewHolder.from(parent)
            else -> throw ClassNotFoundException("View class not found $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        when (holder) {

            is AllIngredientViewHolder -> {
                val allIngredient = currentItem as IngredientItem.NormalIngredientItem
                holder.bind(clickListener, allIngredient.ingredient)
            }

            is StockIngredientViewHolder -> {
                val stockIngredient = currentItem as IngredientItem.IngredientInStock
                holder.bind(clickListener, stockIngredient.ingredient)
            }

            is CartIngredientViewHolder -> {
                val cartIngredient = currentItem as IngredientItem.IngredientInCart
                holder.bind(clickListener, cartIngredient.ingredient)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is IngredientItem.NormalIngredientItem -> NORMAL_ITEM
            is IngredientItem.IngredientInStock -> ITEM_IN_STOCK
            is IngredientItem.IngredientInCart -> ITEM_IN_CART
        }
    }
}

class AllIngredientViewHolder(private val binding: AllIngredientItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(clickListener: IngredientClickListener, ingredient: IngredientWithCocktail) {
        binding.ingredientWithCocktail = ingredient
        binding.clickListener = clickListener

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): AllIngredientViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = AllIngredientItemBinding.inflate(inflater, parent, false)

            return AllIngredientViewHolder(binding)
        }
    }
}

class StockIngredientViewHolder(private val binding: StockIngredientItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(clickListener: IngredientClickListener, ingredient: IngredientWithCocktail) {
        binding.ingredientWithCocktail = ingredient
        binding.clickListener = clickListener

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): StockIngredientViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = StockIngredientItemBinding.inflate(inflater, parent, false)

            return StockIngredientViewHolder(binding)
        }
    }
}

class CartIngredientViewHolder(private val binding: CartIngredientItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(clickListener: IngredientClickListener, ingredient: IngredientWithCocktail) {
        binding.ingredientWithCocktail = ingredient
        binding.clickListener = clickListener

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): CartIngredientViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = CartIngredientItemBinding.inflate(inflater, parent, false)

            return CartIngredientViewHolder(binding)
        }
    }
}

class IngredientClickListener(val clickListener: (IngredientWithCocktail) -> Unit) {
    fun onClick(ingredient: IngredientWithCocktail) = clickListener(ingredient)
}

sealed class IngredientItem {
    data class NormalIngredientItem(val ingredient: IngredientWithCocktail) : IngredientItem() {
        override val id = ingredient.ingredient.ingredient_name
    }

    data class IngredientInStock(val ingredient: IngredientWithCocktail) : IngredientItem() {
        override val id = ingredient.ingredient.ingredient_name
    }

    data class IngredientInCart(val ingredient: IngredientWithCocktail) : IngredientItem() {
        override val id = ingredient.ingredient.ingredient_name
    }

    abstract val id: String
}