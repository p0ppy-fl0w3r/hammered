package com.example.hammered.cocktail.createCocktail

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.hammered.Constants
import com.example.hammered.R
import com.example.hammered.databinding.FragmentCreateCocktailBinding
import kotlinx.android.synthetic.main.fragment_create_cocktail.*
import timber.log.Timber

// TODO consider creating this and ingredient in an activity
class CreateCocktailFragment : Fragment() {

    private lateinit var binding: FragmentCreateCocktailBinding

    // TEST this should be empty
    private var imageUrl = "content://com.android.providers.media.documents/document/image%3A6139"

    private val result = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUrl = it.toString()
        val newCocktailImage = binding.addCocktailImage
        // Display the selected image in the add image button
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

        val nestedScrollView = binding.nestedScrollView
        val ingredientRecycler = binding.ingRefRecycler
        val stepsRecycler = binding.stepsRecycler

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


        viewModel.stepsValid.observe(viewLifecycleOwner) {
            when (it) {
                Constants.VALUE_OK -> {
                    Timber.e("The cocktail data is valid")
                }
                Constants.NO_VALUES -> {
                    Toast.makeText(
                        requireContext(),
                        "There are no steps.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    stepsRecycler.post {
                        val yPos = stepsRecycler.y + stepsRecycler.getChildAt(it).y
                        nestedScrollView.smoothScrollTo(0, yPos.toInt())
                    }
                    val errorItem = stepsRecycler[it].findViewById<EditText>(R.id.stepText)
                    animateError(errorItem)

                    Toast.makeText(
                        requireContext(),
                        "Looks like a step is blank!",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

        }


        viewModel.ingredientValid.observe(viewLifecycleOwner)
        {

            when (it[0]) {
                Constants.VALUE_OK -> {
                    Timber.e("The values are ok")
                    viewModel.setCocktailChecked()
                }

                Constants.NO_VALUES -> {
                    Toast.makeText(
                        requireContext(),
                        "You need to add an ingredient!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                Constants.INGREDIENT_NAME_EMPTY -> {
                    Toast.makeText(
                        requireContext(),
                        "Looks like an ingredient name is blank.",
                        Toast.LENGTH_SHORT
                    ).show()

                    val errorItem =
                        ingredientRecycler[it[1]].findViewById<AutoCompleteTextView>(R.id.RefIngredientName)

                    // Scroll to error item
                    ingredientRecycler.post {
                        val yPos = ingredientRecycler.y + ingredientRecycler.getChildAt(it[1]).y
                        nestedScrollView.smoothScrollTo(0, yPos.toInt())
                    }

                    animateError(errorItem)
                }

                Constants.NO_INGREDIENT_IN_DATABASE -> {
                    Toast.makeText(
                        requireContext(),
                        "Invalid ingredient name!",
                        Toast.LENGTH_SHORT
                    ).show()

                    val errorItem =
                        ingredientRecycler[it[1]].findViewById<AutoCompleteTextView>(R.id.RefIngredientName)

                    ingredientRecycler.post {
                        val yPos = ingredientRecycler.y + ingredientRecycler.getChildAt(it[1]).y
                        nestedScrollView.smoothScrollTo(0, yPos.toInt())
                    }

                    animateError(errorItem)
                }

                Constants.QUANTITY_FIELD_EMPTY -> {
                    binding.ingRefRecycler.smoothScrollToPosition(it[1])
                    val errorItem =
                        ingredientRecycler[it[1]].findViewById<EditText>(R.id.refQuantity)

                    ingredientRecycler.post {
                        val yPos = ingredientRecycler.y + ingredientRecycler.getChildAt(it[1]).y
                        nestedScrollView.smoothScrollTo(0, yPos.toInt())
                    }

                    animateError(errorItem)
                    Toast.makeText(
                        requireContext(),
                        "Quantity is invalid.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    throw IllegalArgumentException("Error code or item index not valid $it")
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
        binding.saveCocktail.setOnClickListener {
            val cocktailNameView = binding.textCocktailName
            val cocktailName = cocktailNameView.text
            if (cocktailName.isNotBlank()) {
                Timber.e("The values are $cocktailName $imageUrl")
                viewModel.validate()
            }
            else {

                Toast.makeText(
                    requireContext(),
                    "You must give the cocktail a name!",
                    Toast.LENGTH_LONG
                ).show()

                animateError(cocktailNameView)
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
        binding.textCocktailName.setText("Gin")
        binding.cocktailDescriptionText.setText("This is very strong")
    }

    private fun animateError(view: View) {
        val animation = ObjectAnimator.ofInt(
            view,
            "backgroundColor",
            0x00000000,
            0x55ff0000,
            0x00000000
        )

        animation.duration = 500
        animation.repeatMode = ObjectAnimator.REVERSE
        animation.repeatCount = 1
        animation.setEvaluator(ArgbEvaluator())
        animation.start()
    }
}