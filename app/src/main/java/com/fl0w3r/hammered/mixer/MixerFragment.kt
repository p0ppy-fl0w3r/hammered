package com.fl0w3r.hammered.mixer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.databinding.FragmentMixerBinding
import timber.log.Timber


class MixerFragment : Fragment() {

    private lateinit var binding: FragmentMixerBinding

    private val viewModel: MixerViewModel by lazy { ViewModelProvider(this)[MixerViewModel::class.java] }

    // TODO add a option settings where users can select if the cocktails should have a combination or individual ingredients.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mixer, container, false)

        val ingredientAdapter = MixerIngredientAdapter(
            MixerIngredientClickListener {
                viewModel.setIngredientState(it)
            }
        )

        val cocktailAdapter = MixerCocktailAdapter(
            MixerCocktailClickListener {

            }
        )

        binding.ingredientSelectRecycler.adapter = ingredientAdapter
        binding.cocktailRecycler.adapter = cocktailAdapter

        viewModel.ingredientList.observe(viewLifecycleOwner) {
            if (it != null) {
                ingredientAdapter.submitList(it)
            }
        }


        viewModel.cocktailList.observe(viewLifecycleOwner) {
            if (it != null) {
                cocktailAdapter.submitList(it)
            }
        }

        viewModel.selectedIngredientList.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.getCocktails(it)
                Timber.e("The length is ${it.size}")

            }
            else{
                Timber.e("Null")
            }
        }


        return binding.root
    }

}