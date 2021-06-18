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
    private lateinit var binding: FragmentIngredientDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val selectedIngredient = IngredientDetailsFragmentArgs.fromBundle(
            requireArguments()
        ).ingredient

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_ingredient_details,
            container,
            false
        )

        viewModel = ViewModelProvider(this).get(IngredientDetailsViewModel::class.java)

        viewModel.setIngredient(selectedIngredient.asIngredient())

        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        onClickCartIcon()
        onClickCheckBox()

        val adapter = IngredientDetailsAdapter(IngredientDetailsClickListener {
            findNavController().navigate(
                IngredientDetailsFragmentDirections.ingredientDetailsToCocktailDetails(it.cocktail.asData())
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

    private fun onClickCartIcon() {
        binding.detailIngredientCart.setOnClickListener {
            viewModel.changeInCart()
            val msg = when (viewModel.currentIngredient.value!!.inCart) {
                false -> "${viewModel.currentIngredient.value!!.ingredient_name} removed from cart!"
                else -> "${viewModel.currentIngredient.value!!.ingredient_name} added to cart!"
            }
            Toast.makeText(
                context,
                msg,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun onClickCheckBox() {
        binding.detailIngredientStockCheck.setOnClickListener {
            viewModel.changeInStock()
            val msg = when (viewModel.currentIngredient.value!!.inStock) {
                false -> "${viewModel.currentIngredient.value!!.ingredient_name} removed from stock!"
                else -> "${viewModel.currentIngredient.value!!.ingredient_name} added to stock!"
            }
            Toast.makeText(
                context,
                msg,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

