package com.fl0w3r.hammered.cocktail.cocktailDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fl0w3r.hammered.databinding.StepSlideLayoutBinding
import com.fl0w3r.hammered.entities.Ingredient
import com.fl0w3r.hammered.entities.relations.IngredientCocktailRef
import com.fl0w3r.hammered.wrappers.RefItemWrapper
import com.fl0w3r.hammered.wrappers.SlideWrapper
import com.fl0w3r.hammered.wrappers.StepsWrapper
import timber.log.Timber

class SlidesAdapter(private val closeButtonClicked: CloseClickListener) :
    ListAdapter<SlideWrapper, SlidesAdapter.SlidesViewHolder>(SlideDiffUtils()) {

    class SlideDiffUtils : DiffUtil.ItemCallback<SlideWrapper>() {
        override fun areItemsTheSame(
            oldItem: SlideWrapper,
            newItem: SlideWrapper
        ): Boolean {
            return oldItem.step == newItem.step
        }

        override fun areContentsTheSame(
            oldItem: SlideWrapper,
            newItem: SlideWrapper
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlidesViewHolder {
        return SlidesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SlidesViewHolder, position: Int) {

        holder.bind(
            getItem(position),
            (position + 1).toString(),
            position == itemCount - 1,
            closeButtonClicked
        )
    }

    class SlidesViewHolder(private val binding: StepSlideLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(slideWrapper: SlideWrapper, index: String, isLast: Boolean, closeClicked: CloseClickListener) {
            binding.slideStepText.text = slideWrapper.step

            val ingredientAdapter = SlideRecyclerAdapter()
            binding.slideRecycler.adapter = ingredientAdapter
            ingredientAdapter.submitList(slideWrapper.ingredient)

            if (isLast) {
                binding.closeButton.visibility = View.VISIBLE
                binding.closeButton.setOnClickListener {
                    closeClicked.closeButtonClicked()
                }
            } else {
                binding.closeButton.visibility = View.INVISIBLE
            }

            binding.serialNumber.text = index
        }

        companion object {
            fun from(parent: ViewGroup): SlidesViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = StepSlideLayoutBinding.inflate(inflater, parent, false)
                return SlidesViewHolder(binding)
            }
        }
    }

    class CloseClickListener(val closeButtonClicked: ()-> Unit){
        fun closeClicked() =  closeButtonClicked()
    }
}