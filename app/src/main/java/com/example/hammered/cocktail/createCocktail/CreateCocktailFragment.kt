package com.example.hammered.cocktail.createCocktail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.hammered.R
import com.example.hammered.databinding.FragmentCreateCocktailBinding
import kotlinx.android.synthetic.main.fragment_create_cocktail.*
import timber.log.Timber

// TODO add error messages for when the ingredients or cocktail are invalid
// FIXME two ingredients with same name can be added.
// FIXME SPAN_EXCLUSIVE_EXCLUSIVE spans cannot have a zero length
// TODO consider creating this and ingredient in an activity
class CreateCocktailFragment : Fragment() {

    private lateinit var binding: FragmentCreateCocktailBinding

    // TEST this should be empty
    private var imageUrl = "content://com.android.providers.media.documents/document/image%3A6139"

    private val result = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUrl = it.toString()
        val newCocktailImage = binding.addCocktailImage
        // Display the new image in the add image button
        Glide.with(requireContext()).load(imageUrl).into(newCocktailImage)
    }

    private val viewModel: CreateCocktailViewModel by lazy {
        ViewModelProvider(this).get(CreateCocktailViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_create_cocktail,
                container,
                false
            )

        // TEST
        test()

        // Array adapter for autoCompleteTextView
        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, mutableListOf(""))

        val adapter = CreateCocktailAdapter(ItemOnClickListener { itemNumber ->
            viewModel.removeIngredient(itemNumber)
        }, arrayAdapter)

        val stepsAdapter = StepsRecyclerAdapter(ClickListener {
            viewModel.removeStep(it)
        })

        binding.stepsRecycler.adapter = stepsAdapter
        binding.ingRefRecycler.adapter = adapter


        viewModel.allIngredientList.observe(viewLifecycleOwner) {
            if (it != null) {
                val ingredientNameList = mutableListOf<String>()
                for (i in it) {
                    ingredientNameList.add(i.ingredient_name)
                }

                // Create an array adapter with all known ingredient name from the database.
                val mAdapter =
                    ArrayAdapter(
                        requireContext(),
                        R.layout.autofill_dialog_item,
                        ingredientNameList
                    )
                adapter.arrayAdapter = mAdapter
                ingRefRecycler.adapter = adapter
            }
        }

        viewModel.ingredientList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }

        viewModel.stepsList.observe(viewLifecycleOwner) {
            stepsAdapter.submitList(it)
            stepsAdapter.notifyDataSetChanged()
        }

        // TODO add animation to error fields
        viewModel.stepsValid.observe(viewLifecycleOwner) {
            when (it) {
                -2 -> {
                    Timber.e("The cocktail data is valid")
                }
                -1 -> {
                    Timber.e("There are no steps")
                }
                else -> {
                    binding.stepsRecycler.smoothScrollToPosition(it)
                    Timber.e("The step is blank at $it")

                }
            }

        }

        viewModel.ingredientValid.observe(viewLifecycleOwner)
        {
            when (it) {
                -2 -> {
                    Timber.e("The ingredient data is valid")
                    viewModel.setCocktailChecked()
                }
                -1 -> {
                    Timber.e("There are no ingredient")
                }
                else -> {
                    binding.stepsRecycler.smoothScrollToPosition(it)
                    Timber.e("The ingredient is blank at $it")
                }
            }
        }

        viewModel.saveCocktail.observe(viewLifecycleOwner)
        {
            if (it == true) {
                val cocktailName = binding.textCocktailName.text.toString()
                val description = binding.cocktailDescriptionText.text.toString()

                viewModel.saveCocktail(cocktailName, description, imageUrl)
                Timber.e("Cocktail saved!")
            }
        }

        // Click listeners
        getImage()
        addIngredient()
        addStep()
        saveCocktail()

        return binding.root
    }

    private fun addIngredient() {
        binding.addIngredient.setOnClickListener {
            viewModel.addIngredient()
        }
    }

    private fun addStep() {
        binding.addStep.setOnClickListener {
            //TEST
            Timber.e("The list is ${viewModel.ingredientList.value}")
            viewModel.addStep()
        }
    }

    private fun saveCocktail() {
        //TODO add an error message when the user has not selected an image for the cocktail
        binding.saveCocktail.setOnClickListener {
            val cocktailName = binding.textCocktailName.text
            // TODO consider allowing the user to set the description to blank
            val description = binding.cocktailDescriptionText.text
            var isValid = true
            if (cocktailName.isBlank() || description.isBlank() || imageUrl.isBlank()) {
                isValid = false
                Timber.e("One of these is not correct $cocktailName $description $imageUrl")
            }
            if (isValid) {
                Timber.e("The values are $cocktailName $description $imageUrl")
                viewModel.validate()
            }
        }
    }

    private fun getImage() {
        binding.addCocktailImage.setOnClickListener {
            result.launch("image/*")
        }
    }

    //TEST
    private fun test() {
        binding.textCocktailName.setText("Super Sonic")
        binding.cocktailDescriptionText.setText("This is very strong")
    }
}