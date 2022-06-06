package com.fl0w3r.hammered.mixer

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.databinding.FragmentMixerBinding
import com.fl0w3r.hammered.dialog.IngredientInfoDialog
import com.fl0w3r.hammered.dialog.WarningDialog


class MixerFragment : Fragment() {

    private lateinit var binding: FragmentMixerBinding

    private val viewModel: MixerViewModel by lazy { ViewModelProvider(this)[MixerViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mixer, container, false)

        val ingredientAdapter = MixerIngredientAdapter(
            MixerIngredientClickListener(clickListener = {
                viewModel.setIngredientState(it)
            },
                longClickListener = {
                    IngredientInfoDialog(it)
                        .show(parentFragmentManager, "ingredient_info_dialog")
                })
        )

        val cocktailAdapter = MixerCocktailAdapter(
            MixerCocktailClickListener {
                findNavController().navigate(MixerFragmentDirections.mixerToCocktail(it))
            }
        )

        binding.ingredientSelectRecycler.adapter = ingredientAdapter
        binding.cocktailRecycler.adapter = cocktailAdapter

        viewModel.ingredientList.observe(viewLifecycleOwner) {
            if (it != null) {
                ingredientAdapter.submitList(it)
            }
        }


        viewModel.cocktailList.observe(viewLifecycleOwner) {
            if (it != null) {
                // Prevent any repeating cocktails from showing.
                cocktailAdapter.submitList(it.toSet().toList())
            }
        }

        viewModel.selectedIngredientList.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.getCocktails(it)
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

}