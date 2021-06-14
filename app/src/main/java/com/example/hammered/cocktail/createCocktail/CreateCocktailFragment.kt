package com.example.hammered.cocktail.createCocktail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.hammered.R
import com.example.hammered.databinding.FragmentCreateCocktailBinding
import timber.log.Timber

// TODO the ingredients may not be valid
// FIXME two ingredients with same name can be added.
class CreateCocktailFragment : Fragment() {

    private lateinit var binding: FragmentCreateCocktailBinding

    var imageUrl = "content://com.android.providers.media.documents/document/image%3A6139"

    private val result = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUrl = it.toString()
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

        getImage()
        test()

        val adapter = CreateCocktailAdapter(ItemOnClickListener { itemNumber ->
            viewModel.removeIngredient(itemNumber)
        })

        val stepsAdapter = StepsRecyclerAdapter(ClickListener {
            viewModel.removeStep(it)
        })

        binding.stepsRecycler.adapter = stepsAdapter
        binding.ingRefRecycler.adapter = adapter

        binding.addIngredient.setOnClickListener {
            viewModel.addIngredient()
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
                    Timber.e("The data is valid")
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

        viewModel.ingredientValid.observe(viewLifecycleOwner) {
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

        viewModel.saveCocktail.observe(viewLifecycleOwner) {
            if (it == true) {
                val cocktailName = binding.textCocktailName.text.toString()
                val description = binding.cocktailDescriptionText.text.toString()

                viewModel.saveCocktail(cocktailName, description, imageUrl)
            }
        }

        binding.addStep.setOnClickListener {
            viewModel.addStep()
        }

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

        return binding.root
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