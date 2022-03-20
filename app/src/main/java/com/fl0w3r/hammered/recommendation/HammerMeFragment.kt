package com.fl0w3r.hammered.recommendation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.cocktail.CocktailAdapter
import com.fl0w3r.hammered.cocktail.CocktailClickListener
import com.fl0w3r.hammered.cocktail.CocktailFragmentDirections
import com.fl0w3r.hammered.databinding.FragmentHammerMeBinding
import com.fl0w3r.hammered.ingredients.ingredientDetails.IngredientDetailsAdapter
import com.fl0w3r.hammered.ingredients.ingredientDetails.IngredientDetailsClickListener
import com.fl0w3r.hammered.ingredients.ingredientDetails.IngredientDetailsFragmentDirections


class HammerMeFragment : Fragment() {

    private lateinit var binding: FragmentHammerMeBinding

    private val viewModel: HammerViewModel by lazy {
        ViewModelProvider(this)[HammerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // TODO add loading animation to import and export screens
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hammer_me, container, false)

        viewModel.getRecommendation()

        val adapter = IngredientDetailsAdapter(IngredientDetailsClickListener {
            // TODO add navigation
        })

        binding.recommenderRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recommenderRecycler.adapter = adapter

        // LiveData observers
        viewModel.recommendationScore.observe(viewLifecycleOwner){
            it.let {
                // Score less han 0.01 can be negligible
                if (it.values.sum() < 0.01f){
                    viewModel.showRandomRecommendation()
                    return@observe
                }

                showTitleMessage()
                viewModel.showRecommendation(it.keys)

            }
        }

        viewModel.cocktailList.observe(viewLifecycleOwner){
            it.let {
                adapter.submitList(it)
            }
        }

        viewModel.isEmpty.observe(viewLifecycleOwner){
            if (it != null){
                if (it){
                    getString(R.string.empty_list_msg).also { binding.defaultMessage.text = it }
                }
            }
        }


        return binding.root
    }

    private fun showTitleMessage(){
        binding.defaultMessage.text = getString(R.string.rec_msg)
    }

}