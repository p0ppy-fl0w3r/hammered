package com.example.hammered.ingredients

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hammered.R
import com.example.hammered.database.CocktailDatabase
import com.example.hammered.databinding.IngredientFragmentBinding


class IngredientFragment : Fragment() {

    private lateinit var binding: IngredientFragmentBinding

    private val viewModel by lazy {
        ViewModelProvider(this).get(IngredientViewModel::class.java)
    }

    private var msg = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.ingredient_fragment, container, false)

        val adapter = IngredientAdapter(IngredientClickListener {
            Toast.makeText(
                requireContext(),
                it.ingredient.ingredient_description,
                Toast.LENGTH_SHORT
            ).show()
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
        return binding.root
    }

    private fun filterDataFromChip(checkedId: Int) {
        when (checkedId) {
            binding.chipAllIngredient.id -> msg = 1
            binding.chipMyStock.id -> msg = 2
            binding.chipShoppingCart.id -> msg = 3
        }

        viewModel.checkedData(msg)
    }

}