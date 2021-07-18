package com.example.hammered.cocktail.cocktailDetails

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hammered.Constants
import com.example.hammered.MainActivity
import com.example.hammered.R
import com.example.hammered.cocktail.createCocktail.CreateCocktailActivity
import com.example.hammered.databinding.FragmentCocktailDetailBinding
import com.example.hammered.dialog.CancelAlertDialog
import com.example.hammered.utils.UiUtils


class CocktailDetailFragment : Fragment() {

    lateinit var binding: FragmentCocktailDetailBinding
    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(
            CocktailDetailsViewModel::class.java
        )
    }

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

        viewModel.currentCocktail.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.setIngredient(it.cocktail_id)
            }
        }

        viewModel.cocktailDeleted.observe(viewLifecycleOwner) {
            if (it == true) {
                viewModel.doneDeleting()

                // Navigate to cocktail fragment after deleting the cocktail.
                findNavController().navigate(CocktailDetailFragmentDirections.detailsToCocktail())
            }
        }

        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.searchItem).isVisible = false
        menu.findItem(R.id.settingsOption).isVisible = false

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        // Add delete and copy options without clearing the original options menu options.
        inflater.inflate(R.menu.cocktail_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_cocktail -> CancelAlertDialog(getString(R.string.delete_cocktail_message)
            ) { viewModel.deleteCurrentCocktail() }.show(
                requireActivity().supportFragmentManager,
                "CancelAlertDialog"
            )
            R.id.copy_and_edit -> viewModel.copyAndEdit()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()

        // Hides the keyboard in case the user navigated to this fragment using search.
        UiUtils.hideKeyboard(requireContext(), this.requireView())

        val selectedCocktail = CocktailDetailFragmentArgs.fromBundle(requireArguments()).cocktail
        viewModel.setCocktail(selectedCocktail.asCocktail())
    }

}