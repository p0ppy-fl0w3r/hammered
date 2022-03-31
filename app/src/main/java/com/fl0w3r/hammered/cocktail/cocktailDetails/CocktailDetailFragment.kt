package com.fl0w3r.hammered.cocktail.cocktailDetails

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fl0w3r.hammered.Constants
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.cocktail.CocktailData
import com.fl0w3r.hammered.cocktail.createCocktail.CreateCocktailActivity
import com.fl0w3r.hammered.databinding.FragmentCocktailDetailBinding
import com.fl0w3r.hammered.dialog.CancelAlertDialog
import com.fl0w3r.hammered.utils.UiUtils


class CocktailDetailFragment : Fragment() {

    lateinit var binding: FragmentCocktailDetailBinding
    lateinit var selectedCocktail: CocktailData

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[CocktailDetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_cocktail_detail, container, false)

        selectedCocktail = CocktailDetailFragmentArgs.fromBundle(requireArguments()).cocktail

        viewModel.setCocktail(selectedCocktail.asCocktail())

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = CocktailDetailsAdapter(CocktailDetailsClickListener {
            findNavController().navigate(
                CocktailDetailFragmentDirections.cocktailDetailToIngredientDetail(
                    it.item.asIngredientData()
                )
            )
        })

        binding.detailIsFav.setOnClickListener {
            viewModel.changeFavourite()
        }

        binding.detailCocktailEdit.setOnClickListener {
            viewModel.editCurrent(selectedCocktail.cocktail_id)
        }

        binding.cocktailDetailRecycler.adapter = adapter

        viewModel.ingredientRefLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }


        viewModel.editCocktail.observe(viewLifecycleOwner){
            if (it != null){
                viewModel.doneEdit()
                val intent = Intent(requireContext(), CreateCocktailActivity::class.java)

                intent.putExtra(Constants.EDIT_COCKTAIL, it.asData())

                startActivity(intent)
            }
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

        viewModel.copyCocktail.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.doneCopy()

                val intent = Intent(requireContext(), CreateCocktailActivity::class.java)
                intent.putExtra(Constants.COPY_AND_EDIT, it.asData())

                startActivity(intent)
            }
        }

        // Click Listeners
        onClickPlay(selectedCocktail)

        return binding.root
    }

    private fun onClickPlay(cocktailData: CocktailData){
        binding.playSlides.setOnClickListener {
            showSlides(cocktailData)
        }
    }

    private fun showSlides(cocktailData: CocktailData){
        val intent = Intent(requireContext(), SlidesActivity::class.java)
        intent.putExtra(Constants.COCKTAIL_DATA, cocktailData)
        startActivity(intent)
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
            R.id.delete_cocktail -> CancelAlertDialog(
                getString(R.string.delete_cocktail_message)
            ) { viewModel.deleteCurrentCocktail() }.show(
                requireActivity().supportFragmentManager,
                "CancelAlertDialog"
            )
            R.id.copy_and_edit -> viewModel.copyAndEdit(selectedCocktail.cocktail_id)
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