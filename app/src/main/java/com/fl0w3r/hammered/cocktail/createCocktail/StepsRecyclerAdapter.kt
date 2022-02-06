package com.fl0w3r.hammered.cocktail.createCocktail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.databinding.CocktailStepsItemBinding
import com.fl0w3r.hammered.wrappers.StepsWrapper

// TODO add a onEditListener
class StepsRecyclerAdapter(private val clickListener: ClickListener) :
    ListAdapter<StepsWrapper, StepsViewHolder>(StepsDiffUtils()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepsViewHolder {
        return StepsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: StepsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,position+1, clickListener)
    }

}

class StepsDiffUtils : DiffUtil.ItemCallback<StepsWrapper>() {
    override fun areItemsTheSame(oldItem: StepsWrapper, newItem: StepsWrapper): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: StepsWrapper, newItem: StepsWrapper): Boolean {
        return oldItem == newItem
    }
}

class StepsViewHolder(private val binding: CocktailStepsItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(step: StepsWrapper, index: Int, clickListener: ClickListener) {
        binding.value = step
        binding.stepNumber.text = index.toString()

        binding.deleteStep.setOnClickListener { clickListener.listener(step) }

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): StepsViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<CocktailStepsItemBinding>(
                inflater,
                R.layout.cocktail_steps_item,
                parent,
                false
            )
            return StepsViewHolder(binding)
        }
    }
}

class ClickListener(val listener: (StepsWrapper) -> Unit) {
    fun onClick(step: StepsWrapper) = listener(step)
}
