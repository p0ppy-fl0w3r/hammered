package com.example.hammered.cocktail.cocktailDetails

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hammered.Constants
import com.example.hammered.R
import com.example.hammered.cocktail.createCocktail.CreateCocktailActivity
import com.example.hammered.databinding.FragmentCocktailDetailBinding
import com.example.hammered.utils.UiUtils


class CocktailDetailFragment : Fragment() {

    lateinit var binding: FragmentCocktailDetailBinding
    private val viewModel by lazy { ViewModelProvider(requireActivity()).get(CocktailDetailsViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_cocktail_detail, container, false)

        val selectedCocktail = CocktailDetailFragmentArgs.fromBundle(requireArguments()).cocktail

        viewModel.setCocktail(selectedCocktail.asCocktail())

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = CocktailDetailsAdapter(CocktailDetailsClickListener {
            findNavController().navigate(
                CocktailDetailFragmentDirections.cocktailDetailToIngredientDetail(
                    it.item
                )
            )
        })

        binding.detailIsFav.setOnClickListener {
            viewModel.changeFavourite()
        }

        binding.detailCocktailEdit.setOnClickListener {
            val intent = Intent(requireContext(), CreateCocktailActivity::class.java)

            intent.putExtra(Constants.EDIT_COCKTAIL, selectedCocktail)

            startActivity(intent)
        }

        binding.cocktailDetailRecycler.adapter = adapter

        viewModel.ingredientRefLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.currentCocktail.observe(viewLifecycleOwner){
            if(it != null){
                viewModel.setIngredient(it.cocktail_id)
            }
        }

        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.searchItem).isVisible = false

        UiUtils.hideKeyboard(requireContext(), this.requireView())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()

        val selectedCocktail = CocktailDetailFragmentArgs.fromBundle(requireArguments()).cocktail
        viewModel.setCocktail(selectedCocktail.asCocktail())
    }

}