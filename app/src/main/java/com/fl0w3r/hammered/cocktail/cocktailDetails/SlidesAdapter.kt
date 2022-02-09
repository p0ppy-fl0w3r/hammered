package com.fl0w3r.hammered.cocktail.cocktailDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fl0w3r.hammered.databinding.StepSlideLayoutBinding

class SlidesAdapter(private val stepList: List<String>) : RecyclerView.Adapter<SlidesAdapter.SlidesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlidesViewHolder {
        return SlidesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SlidesViewHolder, position: Int) {
        holder.bind(stepList[position])
    }

    override fun getItemCount(): Int {
        return stepList.size
    }

    class SlidesViewHolder(private val binding: StepSlideLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(step: String){
                binding.slideStepText.text = step
            }

            companion object{
                fun from(parent: ViewGroup):SlidesViewHolder{
                    val inflater = LayoutInflater.from(parent.context)
                    val binding = StepSlideLayoutBinding.inflate(inflater, parent, false)
                    return SlidesViewHolder(binding)
                }
            }
        }
}