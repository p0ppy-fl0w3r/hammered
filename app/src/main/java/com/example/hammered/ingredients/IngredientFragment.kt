package com.example.hammered.ingredients

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.hammered.Constants
import com.example.hammered.R
import com.example.hammered.database.CocktailDatabase
import com.example.hammered.databinding.IngredientFragmentBinding

class IngredientFragment : Fragment() {

    private lateinit var binding: IngredientFragmentBinding

    private val viewModel by lazy {
        ViewModelProvider(this).get(IngredientViewModel::class.java)
    }

    private var msg = Constants.NORMAL_ITEM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.ingredient_fragment, container, false)

        msg = (arguments?.getInt(Constants.BUNDLE_STARTUP_INT) ?: Constants.NORMAL_ITEM)

        val adapter = IngredientAdapter(IngredientClickListener {
            findNavController().navigate(
                IngredientFragmentDirections.fromIngredientToIngredientDetails(
                    it.ingredient.asIngredientData()
                )
            )
        }, ItemStatusChangeListener { ingredient, valueFrom ->
            viewModel.checkChanged(ingredient, valueFrom)
        })

        val database = CocktailDatabase.getDatabase(requireContext())
        database.cocktailDao.getLiveCocktailsFromIngredients().observe(viewLifecycleOwner) {
            filterDataFromChip(binding.ingredientChipGroup.checkedChipId)
        }

        binding.ingredientChipGroup.setOnCheckedChangeListener { _, checkedId ->
            filterDataFromChip(checkedId)
        }

        viewModel.ingredientData.observe(viewLifecycleOwner) {
            adapter.addFilterAndSubmitList(it, msg)
        }

        binding.ingredientRecycler.adapter = adapter

        setSelectedChip(msg)

        return binding.root
    }

    private fun filterDataFromChip(checkedId: Int) {
        when (checkedId) {
            binding.chipAllIngredient.id -> msg = Constants.NORMAL_ITEM
            binding.chipMyStock.id -> msg = Constants.ITEM_IN_STOCK
            binding.chipShoppingCart.id -> msg = Constants.ITEM_IN_CART
        }

        viewModel.checkedData(msg)
    }

    private fun setSelectedChip(id: Int){
        when(id){
            Constants.NORMAL_ITEM -> binding.chipAllIngredient.isChecked = true
            Constants.ITEM_IN_STOCK -> binding.chipMyStock.isChecked = true
            else -> binding.chipShoppingCart.isChecked = true
        }
    }
}