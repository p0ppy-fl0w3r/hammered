package com.fl0w3r.hammered.recommendation

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hammer_me, container, false)

        viewModel.getRecommendation()

        val adapter = IngredientDetailsAdapter(IngredientDetailsClickListener {
            findNavController().navigate(HammerMeFragmentDirections.hammerToCocktail(it.cocktail.asData()))
        })

        binding.recommenderRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recommenderRecycler.adapter = adapter

        // LiveData observers
        viewModel.recommendationScore.observe(viewLifecycleOwner) {
            it.let {
                // Score less han 0.01 can be negligible
                if (it.values.sum() < 0.01f) {
                    viewModel.showRandomRecommendation()
                    showTitleMessage(R.string.hammer_me_msg)
                    return@observe
                }

                showTitleMessage(R.string.rec_msg)
                viewModel.showRecommendation(if(it.keys.size < 4) it.keys else it.keys.toList().subList(0,4))

            }
        }

        viewModel.cocktailList.observe(viewLifecycleOwner) {
            it.let {
                adapter.submitList(it)
            }
        }

        viewModel.isEmpty.observe(viewLifecycleOwner) { empty ->
            if (empty != null) {
                if (empty) {
                    showTitleMessage(R.string.empty_list_msg)

                }
            }
        }


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    private fun showTitleMessage(resId: Int) {
        binding.defaultMessage.text = getString(resId)
    }

}