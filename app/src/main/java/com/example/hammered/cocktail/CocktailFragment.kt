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
import com.example.hammered.databinding.CocktailFragmentBinding

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

        filterDataFromChip(binding.cocktailChipGroup.checkedChipId)

        binding.cocktailChipGroup.setOnCheckedChangeListener { _, checkedId ->
            filterDataFromChip(checkedId)
        }

        val adapter = CocktailAdapter(CocktailClickListener {
            val clickedData = it.asData()
            findNavController().navigate(
                CocktailFragmentDirections.actionCocktailFragmentToCocktailDetailFragment(
                    clickedData
                )
            )
        })
        // TODO submit list on background thread
        viewModel.cocktailLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
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

        viewModel.checkedData(msg)
    }

}