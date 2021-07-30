package com.example.hammered.cocktail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.hammered.Constants
import com.example.hammered.R
import com.example.hammered.database.CocktailDatabase
import com.example.hammered.databinding.CocktailFragmentBinding
import timber.log.Timber

class CocktailFragment : Fragment() {


    private lateinit var binding: CocktailFragmentBinding
    private var msg = Constants.NORMAL_COCKTAIL_ITEM

    private val viewModel: CocktailViewModel by lazy {
        ViewModelProvider(this).get(CocktailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.cocktail_fragment, container, false)
        msg = arguments?.getInt(Constants.BUNDLE_STARTUP_INT) ?: Constants.NORMAL_COCKTAIL_ITEM

        // Change the data set when a different chip is selected
        binding.cocktailChipGroup.setOnCheckedChangeListener { _, checkedId ->
            filterDataFromChip(checkedId)
        }

        // Observe the data in database for changes
        val database = CocktailDatabase.getDatabase(requireContext())
        database.cocktailDao.getLiveIngredientFromCocktail().observe(viewLifecycleOwner) {
            filterDataFromChip(binding.cocktailChipGroup.checkedChipId)
        }

        val adapter = CocktailAdapter(CocktailClickListener {
            val clickedData = it.cocktail.asData()
            findNavController().navigate(
                CocktailFragmentDirections.actionCocktailFragmentToCocktailDetailFragment(
                    clickedData
                )
            )
        })

        viewModel.cocktailLiveData.observe(viewLifecycleOwner) {
            adapter.applyFilterAndSubmitList(it, msg)
        }

        binding.cocktailRecycler.adapter = adapter

        setSelectedChip(msg)

        return binding.root
    }

    private fun filterDataFromChip(checkedId: Int) {
        when (checkedId) {
            binding.chipAllDrinks.id -> msg = Constants.NORMAL_COCKTAIL_ITEM
            binding.chipMyDrinks.id -> msg = Constants.AVAILABLE_COCKTAIL_ITEM
            binding.chipFavoriteDrinks.id -> msg = Constants.FAVORITE_COCKTAIL_ITEM
        }

        viewModel.checkedData(msg)
    }

    private fun setSelectedChip(id: Int) {
        when (id) {
            Constants.NORMAL_COCKTAIL_ITEM -> binding.chipAllDrinks.isChecked = true
            Constants.AVAILABLE_COCKTAIL_ITEM -> binding.chipMyDrinks.isChecked = true
            else -> binding.chipFavoriteDrinks.isChecked = true
        }
    }
}