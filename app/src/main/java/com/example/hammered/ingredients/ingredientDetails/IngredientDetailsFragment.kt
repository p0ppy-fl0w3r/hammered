package com.example.hammered.ingredients.ingredientDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hammered.R
import com.example.hammered.databinding.FragmentIngredientDetailsBinding


class IngredientDetailsFragment : Fragment() {
    private lateinit var viewModel: IngredientDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val selectedIngredient = IngredientDetailsFragmentArgs.fromBundle(
            requireArguments()
        ).ingredient

        val binding = DataBindingUtil.inflate<FragmentIngredientDetailsBinding>(
            inflater,
            R.layout.fragment_ingredient_details,
            container,
            false
        )

        viewModel = ViewModelProvider(this).get(IngredientDetailsViewModel::class.java)

        viewModel.setIngredient(selectedIngredient.asIngredient())

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = IngredientDetailsAdapter(IngredientDetailsClickListener {
            findNavController().navigate(
                IngredientDetailsFragmentDirections.ingredientDetailsToCocktailDetails(it.item)
            )
        })

        binding.ingredientDetailRecycler.adapter = adapter
        viewModel.getFromIngredient(selectedIngredient.ingredient_name)

        viewModel.cocktailRefLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }


        // Inflate the layout for this fragment
        return binding.root
    }

}