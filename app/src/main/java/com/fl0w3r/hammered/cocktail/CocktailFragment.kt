package com.fl0w3r.hammered.cocktail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fl0w3r.hammered.Constants
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.database.CocktailDatabase
import com.fl0w3r.hammered.databinding.CocktailFragmentBinding


class CocktailFragment : Fragment() {


    private lateinit var binding: CocktailFragmentBinding
    private var chipSelection = Constants.NORMAL_COCKTAIL_ITEM

    private val viewModel: CocktailViewModel by lazy {
        ViewModelProvider(this)[CocktailViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.cocktail_fragment, container, false)
        chipSelection =
            arguments?.getInt(Constants.BUNDLE_STARTUP_INT) ?: Constants.NORMAL_COCKTAIL_ITEM

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

            binding.cocktailRecycler.layoutManager =
                if (it.isNullOrEmpty()) LinearLayoutManager(requireContext()) else StaggeredGridLayoutManager(
                    2,
                    StaggeredGridLayoutManager.VERTICAL
                )
            adapter.applyFilterAndSubmitList(it, chipSelection)


        }

        binding.cocktailRecycler.adapter = adapter

        setSelectedChip(chipSelection)

        return binding.root
    }

    private fun filterDataFromChip(checkedId: Int) {
        when (checkedId) {
            binding.chipAllDrinks.id -> chipSelection = Constants.NORMAL_COCKTAIL_ITEM
            binding.chipMyDrinks.id -> chipSelection = Constants.AVAILABLE_COCKTAIL_ITEM
            binding.chipFavoriteDrinks.id -> chipSelection = Constants.FAVORITE_COCKTAIL_ITEM
        }

        viewModel.checkedData(chipSelection)
    }

    private fun setSelectedChip(id: Int) {
        when (id) {
            Constants.NORMAL_COCKTAIL_ITEM -> binding.chipAllDrinks.isChecked = true
            Constants.AVAILABLE_COCKTAIL_ITEM -> binding.chipMyDrinks.isChecked = true
            else -> binding.chipFavoriteDrinks.isChecked = true
        }
    }
}