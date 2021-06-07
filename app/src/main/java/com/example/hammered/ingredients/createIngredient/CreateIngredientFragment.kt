package com.example.hammered.ingredients.createIngredient

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hammered.R
import com.example.hammered.databinding.FragmentCreateIngredientBinding
import com.example.hammered.entities.Ingredient

class CreateIngredientFragment : Fragment() {

    var imageUrl = ""

    private val result = registerForActivityResult(GetContent()) {
        imageUrl = it.toString()
    }

    lateinit var binding: FragmentCreateIngredientBinding

    val viewModel: CreateIngredientViewModel by lazy {
        ViewModelProvider(this).get(CreateIngredientViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_ingredient, container, false)

        getImage()

        saveIngredient()

        viewModel.newIngredient.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.doneAddingIngredient()
                Toast.makeText(context, "Ingredient added successfully!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(CreateIngredientFragmentDirections.createToIngredient())
            }
        }

        return binding.root
    }

    private fun getImage() {
        binding.addIngredientImage.setOnClickListener {

            result.launch("image/*")
        }
    }

    private fun saveIngredient() {
        binding.createSave.setOnClickListener {
            val ingredientName = binding.textIngredientName.text.toString()
            val ingredientDescription = binding.ingredientDescriptionText.text.toString()
            val isInStock = binding.createInStock.isChecked

            if (ingredientName.isNotBlank()) {
                val mIngredient = Ingredient(
                    ingredient_name = ingredientName,
                    ingredient_description = ingredientDescription,
                    inStock = isInStock,
                    inCart = false,
                    ingredient_image = imageUrl
                )

                viewModel.addIngredient(mIngredient)
            }
            else {
                // TODO change to a custom dialog
                AlertDialog.Builder(context)
                    .setMessage("Ingredient name is empty!!")
                    .setTitle("Warning")
                    .setCancelable(true)
                    .setNegativeButton("Ok") { dialog, _ ->
                        dialog.cancel()
                    }
                    .create()
                    .show()
            }
        }
    }

}