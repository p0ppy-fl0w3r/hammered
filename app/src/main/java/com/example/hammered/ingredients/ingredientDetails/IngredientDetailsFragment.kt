package com.example.hammered.ingredients.ingredientDetails

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hammered.R
import com.example.hammered.databinding.FragmentIngredientDetailsBinding
import com.example.hammered.dialog.CancelAlertDialog
import com.example.hammered.ingredients.createIngredient.CreateIngredientActivity
import com.example.hammered.utils.UiUtils

class IngredientDetailsFragment : Fragment() {
    private lateinit var viewModel: IngredientDetailsViewModel
    private lateinit var binding: FragmentIngredientDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val selectedIngredient = IngredientDetailsFragmentArgs.fromBundle(
            requireArguments()
        ).ingredient

        viewModel =
            ViewModelProvider(this).get(IngredientDetailsViewModel::class.java)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_ingredient_details,
            container,
            false
        )


        viewModel.setIngredient(selectedIngredient.asIngredient())


        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        onClickCartIcon()
        onClickCheckBox()

        val adapter = IngredientDetailsAdapter(IngredientDetailsClickListener {
            findNavController().navigate(
                IngredientDetailsFragmentDirections.ingredientDetailsToCocktailDetails(it.cocktail.asData())
            )
        })

        binding.ingredientDetailRecycler.adapter = adapter
        viewModel.currentIngredient.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.getFromIngredient(selectedIngredient.ingredient_id)
            }
        }

        viewModel.cocktailRefLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.ingredientDeleted.observe(viewLifecycleOwner) {
            if (it == true) {

                viewModel.doneDeleting()
                findNavController().navigate(IngredientDetailsFragmentDirections.detailsToIngredient())

            }
        }

        binding.detailIngredientEdit.setOnClickListener {
            onClickEdit()
        }

        // Inflate the layout for this fragment
        return binding.root
    }


    private fun onClickCartIcon() {
        binding.detailIngredientCart.setOnClickListener {
            viewModel.changeInCart()
            val msg = when (viewModel.currentIngredient.value!!.inCart) {
                false -> "${viewModel.currentIngredient.value!!.ingredient_name} removed from cart!"
                else -> "${viewModel.currentIngredient.value!!.ingredient_name} added to cart!"
            }
            Toast.makeText(
                context,
                msg,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun onClickCheckBox() {
        binding.detailIngredientStockCheck.setOnClickListener {
            viewModel.changeInStock()
            val msg = when (viewModel.currentIngredient.value!!.inStock) {
                false -> "${viewModel.currentIngredient.value!!.ingredient_name} removed from stock!"
                else -> "${viewModel.currentIngredient.value!!.ingredient_name} added to stock!"
            }
            Toast.makeText(
                context,
                msg,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun onClickEdit() {
        val intent = Intent(requireContext(), CreateIngredientActivity::class.java)
        intent.putExtra("ingredient", viewModel.currentIngredient.value!!.asIngredientData())
        startActivity(intent)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.searchItem).isVisible = false
        menu.findItem(R.id.settingsOption).isVisible = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.ingredient_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.delete_ingredient -> CancelAlertDialog(
                getString(R.string.delete_ingredient_message)
            ) { viewModel.deleteCurrentIngredient() }.show(
                requireActivity().supportFragmentManager,
                "CancelAlertDialog"
            )
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    // OnCreate/OnCreateView is not called when a new ingredient is created or edited
    // overriding onStart so that the edited information is updated.
    override fun onStart() {
        super.onStart()

        UiUtils.hideKeyboard(requireContext(), this.requireView())

        val selectedIngredient = IngredientDetailsFragmentArgs.fromBundle(
            requireArguments()
        ).ingredient

        viewModel.setIngredient(selectedIngredient.asIngredient())
    }
}