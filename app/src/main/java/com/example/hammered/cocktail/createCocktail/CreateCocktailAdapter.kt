package com.example.hammered.cocktail.createCocktail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hammered.R
import com.example.hammered.databinding.CreateCocktailIngredientItemBinding
import timber.log.Timber


class CreateCocktailAdapter(
    private val spinnerItemListener: SpinnerItemSelectListener,
    private val onClickListener: ItemOnClickListener
) :
    ListAdapter<NewCocktailRef, CocktailIngredientViewHolder>(ItemDiffUtil()) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CocktailIngredientViewHolder {
        return CocktailIngredientViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CocktailIngredientViewHolder, position: Int) {
        // ehh... Good enough for now.
        Timber.e("Item pos is $position")
        val item = getItem(position)
        holder.bind(item, spinnerItemListener, onClickListener)

//        holder.itemView.findViewById<EditText>(R.id.RefIngredientName)
//            .doOnTextChanged { text, _, _, _ ->
//                eventListener.listener(item.ref_number, text.toString())
//                Timber.e("Text was changed in ${item.ref_number}")
//            }
    }


}

class CocktailIngredientViewHolder(private val binding: CreateCocktailIngredientItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: NewCocktailRef,
        spinnerItemListener: SpinnerItemSelectListener,
        onClickListener: ItemOnClickListener
    ) {
        // TODO clean the code and write more comments
        binding.ingredientItem = item
        binding.onClickListener = onClickListener


        // Creating and attaching adapter for spinner
        ArrayAdapter.createFromResource(
            binding.cocktailIngRecyclerUnits.context,
            R.array.units_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.cocktailIngRecyclerUnits.adapter = adapter
        }

        binding.cocktailIngRecyclerUnits.onItemSelectedListener = spinnerItemListener


        binding.cocktailIngRecyclerUnits.setSelection(item.quantityUnitPos)

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): CocktailIngredientViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<CreateCocktailIngredientItemBinding>(
                layoutInflater,
                R.layout.create_cocktail_ingredient_item,
                parent, false
            )
            return CocktailIngredientViewHolder(binding)
        }
    }

}

class ItemDiffUtil : DiffUtil.ItemCallback<NewCocktailRef>() {
    override fun areItemsTheSame(oldItem: NewCocktailRef, newItem: NewCocktailRef): Boolean {
        return oldItem.ref_number == newItem.ref_number
    }

    override fun areContentsTheSame(oldItem: NewCocktailRef, newItem: NewCocktailRef): Boolean {
        return newItem == oldItem
    }
}

class EditTextEventListener(val listener: (position: Int, newText: String) -> Unit) {
    fun onEvent(position: Int, newText: String) = listener(position, newText)
}

class SpinnerItemSelectListener(
    val listener: (position: Int) -> Unit
) :
    AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
        listener(position)

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Timber.e("Noting was selected.")
    }
}


class ItemOnClickListener(val listener: (itemNumber: Int, clickableView: Int) -> Unit) {
    fun onItemClick(position: Int, clickableView: Int) = listener(position, clickableView)
}

// TEST class for creating cocktail

data class NewCocktailRef(
    var ref_number: Int = 0,
    var ingredient_name: String = "",
    var quantity: String = "",
    var quantityUnitPos: Int = 0,
    var isGarnish: Boolean = false,
    var isOptional: Boolean = false
)