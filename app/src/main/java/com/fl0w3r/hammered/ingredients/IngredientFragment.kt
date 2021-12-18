package com.fl0w3r.hammered.ingredients

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fl0w3r.hammered.Constants
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.database.CocktailDatabase
import com.fl0w3r.hammered.databinding.IngredientFragmentBinding

class IngredientFragment : Fragment() {

    private lateinit var binding: IngredientFragmentBinding

    private val viewModel by lazy {
        ViewModelProvider(this).get(IngredientViewModel::class.java)
    }

    private var chipSelection = Constants.NORMAL_ITEM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.ingredient_fragment, container, false)

        // To make quantity of ingredient bigger than it actually is, display all ingredients instead of stock.
        chipSelection = arguments?.getInt(Constants.BUNDLE_STARTUP_INT) ?: Constants.NORMAL_ITEM

        if (chipSelection !in listOf(
                Constants.NORMAL_ITEM,
                Constants.ITEM_IN_CART,
                Constants.ITEM_IN_STOCK
            )
        ){
            chipSelection = Constants.NORMAL_ITEM
        }


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
            adapter.addFilterAndSubmitList(it, chipSelection)
        }

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        binding.ingredientRecycler.layoutManager = staggeredGridLayoutManager
        binding.ingredientRecycler.adapter = adapter

        setSelectedChip(chipSelection)

        return binding.root
    }

    private fun filterDataFromChip(checkedId: Int) {
        when (checkedId) {
            binding.chipAllIngredient.id -> chipSelection = Constants.NORMAL_ITEM
            binding.chipMyStock.id -> chipSelection = Constants.ITEM_IN_STOCK
            binding.chipShoppingCart.id -> chipSelection = Constants.ITEM_IN_CART
        }

        viewModel.checkedData(chipSelection)
    }

    private fun setSelectedChip(id: Int) {
        when (id) {
            Constants.NORMAL_ITEM -> binding.chipAllIngredient.isChecked = true
            Constants.ITEM_IN_STOCK -> binding.chipMyStock.isChecked = true
            else -> binding.chipShoppingCart.isChecked = true
        }
    }
}