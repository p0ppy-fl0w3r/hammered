package com.example.hammered.cocktail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.hammered.R
import com.example.hammered.database.CocktailDatabase
import com.example.hammered.databinding.CocktailFragmentBinding
import timber.log.Timber

class CocktailFragment : Fragment() {


    private lateinit var binding: CocktailFragmentBinding
    private var msg = 1

    private val viewModel: CocktailViewModel by lazy {
        ViewModelProvider(this).get(CocktailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.cocktail_fragment, container, false)

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
            Timber.e("The list size is ${it?.size}")
        }

        binding.cocktailRecycler.adapter = adapter

        return binding.root
    }

    private fun filterDataFromChip(checkedId: Int) {
        when (checkedId) {
            binding.chipAllDrinks.id -> msg = 1
            binding.chipFavoriteDrinks.id -> msg = 2
            binding.chipMyDrinks.id -> msg = 3
        }

        viewModel.checkedData(msg)
    }

}