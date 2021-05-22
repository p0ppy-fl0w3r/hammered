package com.example.hammered.cocktail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.hammered.R
import com.example.hammered.database.CocktailDatabase
import com.example.hammered.databinding.CocktailFragmentBinding
import com.example.hammered.entities.relations.CocktailWithIngredient
import timber.log.Timber

class CocktailFragment : Fragment() {

    private lateinit var viewModel: CocktailViewModel
    private lateinit var binding: CocktailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this).get(CocktailViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.cocktail_fragment, container, false)


        // TEST

        binding.cocktailChipGroup.setOnCheckedChangeListener { _, checkedId ->
            Timber.e("Chip changed.")
            filterDataFromChip(checkedId)
        }
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
        // TODO submit list on background thread
        viewModel.cocktailLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            Timber.e("The list size is ${it?.size}")
        }

        binding.cocktailRecycler.adapter = adapter

        return binding.root
    }

    private fun filterDataFromChip(checkedId: Int) {
        var msg = 0
        when (checkedId) {
            binding.chipAllDrinks.id -> msg = 1
            binding.chipFavoriteDrinks.id -> msg = 2
            binding.chipMyDrinks.id -> msg = 3
        }

        Timber.e("Filter from chip called with selection $msg")

        viewModel.checkedData(msg)
    }

}