package com.fl0w3r.hammered.ingredients.ingredientDetails

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.databinding.FragmentIngredientDetailsBinding
import com.fl0w3r.hammered.dialog.CancelAlertDialog
import com.fl0w3r.hammered.dialog.VendorDialogs
import com.fl0w3r.hammered.ingredients.IngredientData
import com.fl0w3r.hammered.ingredients.createIngredient.CreateIngredientActivity
import com.fl0w3r.hammered.utils.UiUtils
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class IngredientDetailsFragment : Fragment() {
    private lateinit var viewModel: IngredientDetailsViewModel
    private lateinit var binding: FragmentIngredientDetailsBinding
    private lateinit var selectedIngredient: IngredientData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        selectedIngredient = IngredientDetailsFragmentArgs.fromBundle(
            requireArguments()
        ).ingredient

        viewModel =
            ViewModelProvider(this)[IngredientDetailsViewModel::class.java]

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_ingredient_details,
            container,
            false
        )

        viewModel.setIngredient(selectedIngredient.asIngredient())

        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        val adapter = IngredientDetailsAdapter(IngredientDetailsClickListener {
            findNavController().navigate(
                IngredientDetailsFragmentDirections.ingredientDetailsToCocktailDetails(it.cocktail.cocktail_id)
            )
        })

        binding.ingredientDetailRecycler.adapter = adapter
        viewModel.currentIngredient.observe(viewLifecycleOwner) {
            if (it != null) {
                Glide.with(requireContext())
                    .load(it.ingredient_image)
                    .apply(RequestOptions().error(R.drawable.no_drinks))
                    .into(binding.ingredientDetailImage)
                viewModel.getFromIngredient(selectedIngredient.ingredient_id)

                if (it.inCart) {
                    Snackbar.make(
                        requireContext(),
                        binding.root,
                        "This item is on your shopping list.",
                        Snackbar.LENGTH_SHORT
                    )
                        .setBackgroundTint(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.secondaryLightColor
                            )
                        )
                        .setAction("Search online.") {
                            showOnlineVendors()
                        }
                        .setTextColor(Color.BLACK)
                        .show()
                }
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

        onClickCartIcon()
        onClickCheckBox()

        // Inflate the layout for this fragment
        return binding.root
    }


    private fun showOnlineVendors() {
        VendorDialogs() {
            openVendorSite(it, selectedIngredient.ingredient_name)
        }.show(parentFragmentManager, "Vendor Dialog")
    }

    private fun openVendorSite(vendor: Int, ingredientName: String){
        val uri = when(vendor){
            R.id.vendor_amazon -> Uri.parse( "https://www.amazon.com/s?k=${ingredientName}")
            R.id.vendor_daraz -> Uri.parse( "https://www.daraz.com.np/catalog/?q=${ingredientName}")
            else -> Uri.parse("https://cheers.com.np/search/?q=${ingredientName}")
        }

        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    private fun onClickCartIcon() {
        binding.detailIngredientCart.setOnClickListener {
            viewModel.changeInCart()
            val msg = when (viewModel.currentIngredient.value!!.inCart) {
                false -> "${viewModel.currentIngredient.value!!.ingredient_name} removed from shopping list!"
                else -> "${viewModel.currentIngredient.value!!.ingredient_name} added to shopping list!"
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
        if (::selectedIngredient.isInitialized) {
            val intent = Intent(requireContext(), CreateIngredientActivity::class.java)
            intent.putExtra("ingredient", selectedIngredient.ingredient_id)
            startActivity(intent)
        }
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

            R.id.search_online -> showOnlineVendors()
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