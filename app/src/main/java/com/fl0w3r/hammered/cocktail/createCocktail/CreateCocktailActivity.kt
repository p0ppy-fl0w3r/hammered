package com.fl0w3r.hammered.cocktail.createCocktail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fl0w3r.hammered.Constants
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.cocktail.CocktailData
import com.fl0w3r.hammered.databinding.ActivityCreateCocktailBinding
import com.fl0w3r.hammered.dialog.CancelAlertDialog
import com.fl0w3r.hammered.entities.Cocktail
import com.fl0w3r.hammered.utils.UiUtils
import com.fl0w3r.hammered.utils.UiUtils.animateError
import com.fl0w3r.hammered.utils.UiUtils.hideKeyboard
import com.fl0w3r.hammered.wrappers.NewCocktailRef
import timber.log.Timber


class CreateCocktailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateCocktailBinding

    private var imageUrl = ""
    private var imageEncoded = ""

    private var isEdit = false

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                imageUrl = it.data?.data.toString()

                val inputStream = contentResolver.openInputStream(it.data?.data!!)

                imageEncoded = UiUtils.encodeToBase64(BitmapFactory.decodeStream(inputStream))

                // Display the selected image in the add image button
                Glide.with(this).load(imageUrl).into(binding.addCocktailImage)
            }
        }

    private val viewModel: CreateCocktailViewModel by lazy {
        ViewModelProvider(this)[CreateCocktailViewModel::class.java]
    }

    // Using specific index of changed set will not work for some reason.
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_create_cocktail)


        val selectedCocktail = intent.getParcelableExtra<CocktailData>(Constants.EDIT_COCKTAIL)
        val selectedCocktailForCopy =
            intent.getParcelableExtra<CocktailData>(Constants.COPY_AND_EDIT)

        if (selectedCocktail != null) {
            isEdit = true
            viewModel.setSelectedCocktail(selectedCocktail.cocktail_id)
        }

        if (selectedCocktailForCopy != null) {
            viewModel.setSelectedCocktail(selectedCocktailForCopy.cocktail_id)
        }


        val nestedScrollView = binding.nestedScrollView
        val ingredientRecycler = binding.ingRefRecycler
        val stepsRecycler = binding.stepsRecycler

        // Array adapter for ingredient name autoCompleteTextView
        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.select_dialog_item, mutableListOf(""))

        val adapter = CreateCocktailAdapter(ItemOnClickListener { itemNumber ->
            viewModel.removeIngredient(itemNumber)
        }, arrayAdapter)

        val stepsAdapter = StepsRecyclerAdapter(ClickListener {
            viewModel.removeStep(it)
        })

        binding.stepsRecycler.adapter = stepsAdapter
        binding.ingRefRecycler.adapter = adapter

        viewModel.selectedCocktail.observe(this){
            if (it != null){
                populateFields(it)
            }
        }

        viewModel.editIngredientList.observe(this) {
            val ingredientList = it.mapIndexed { index, ref ->
                NewCocktailRef(
                    ref_number = index,
                    ingredient_name = ref.ingredient_name,
                    quantityUnitPos = Constants.UNITS.indexOf(ref.quantityUnit),
                    quantity = ref.quantity.toString(),
                    isGarnish = ref.isGarnish,
                    isOptional = ref.isGarnish
                )
            }

            viewModel.setIngredientList(ingredientList)
        }

        viewModel.allIngredientList.observe(this) {
            if (it != null) {
                val ingredientNameList = mutableListOf<String>()
                for (i in it) {
                    ingredientNameList.add(i.ingredient_name)
                }

                // Create an array adapter with all known ingredient name from the database.
                val mAdapter =
                    ArrayAdapter(
                        this,
                        R.layout.autofill_dialog_item,
                        ingredientNameList
                    )
                adapter.arrayAdapter = mAdapter
                ingredientRecycler.adapter = adapter
            }
        }

        viewModel.ingredientList.observe(this) {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }

        viewModel.stepsList.observe(this) {
            stepsAdapter.submitList(it)
            stepsAdapter.notifyDataSetChanged()
        }

        viewModel.stepsValid.observe(this) {
            when (it) {
                Constants.VALUE_OK -> {
                    Timber.i("The the steps are valid")
                }
                Constants.NO_VALUES -> {
                    Toast.makeText(
                        this,
                        "There are no steps!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    stepsRecycler.post {
                        val yPos = stepsRecycler.y + stepsRecycler.getChildAt(it).y
                        nestedScrollView.smoothScrollTo(0, yPos.toInt())
                    }
                    val errorItem =
                        stepsRecycler[it].findViewById<LinearLayout>(R.id.stepsTextContainer)
                    animateError(errorItem)

                    Toast.makeText(
                        this,
                        "Looks like a step is blank!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.ingredientValid.observe(this)
        {

            when (it[0]) {
                Constants.VALUE_OK -> {
                    Timber.i("The ingredient values are ok")
                    viewModel.setCocktailChecked()
                }

                Constants.NO_VALUES -> {
                    Toast.makeText(
                        this,
                        "You need to add an ingredient!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                Constants.INGREDIENT_NAME_EMPTY -> {
                    Toast.makeText(
                        this,
                        "Looks like an ingredient name is blank.",
                        Toast.LENGTH_SHORT
                    ).show()

                    val errorItem =
                        ingredientRecycler[it[1]].findViewById<LinearLayout>(R.id.RefIngredientNameContainer)

                    // Scroll to error item
                    ingredientRecycler.post {
                        val yPos = ingredientRecycler.y + ingredientRecycler.getChildAt(it[1]).y
                        nestedScrollView.smoothScrollTo(0, yPos.toInt())
                    }

                    animateError(errorItem)
                }

                Constants.NO_INGREDIENT_IN_DATABASE -> {
                    Toast.makeText(
                        this,
                        "Invalid ingredient name!",
                        Toast.LENGTH_SHORT
                    ).show()

                    val errorItem =
                        ingredientRecycler[it[1]].findViewById<LinearLayout>(R.id.RefIngredientNameContainer)

                    ingredientRecycler.post {
                        val yPos = ingredientRecycler.y + ingredientRecycler.getChildAt(it[1]).y
                        nestedScrollView.smoothScrollTo(0, yPos.toInt())
                    }

                    animateError(errorItem)
                }

                Constants.QUANTITY_FIELD_EMPTY -> {
                    binding.ingRefRecycler.smoothScrollToPosition(it[1])
                    val errorItem =
                        ingredientRecycler[it[1]].findViewById<LinearLayout>(R.id.refQuantityContainer)

                    ingredientRecycler.post {
                        val yPos = ingredientRecycler.y + ingredientRecycler.getChildAt(it[1]).y
                        nestedScrollView.smoothScrollTo(0, yPos.toInt())
                    }

                    animateError(errorItem)
                    Toast.makeText(
                        this,
                        "Quantity is invalid.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    throw IllegalArgumentException("Error code or item index not valid $it")
                }
            }
        }

        viewModel.saveCocktail.observe(this)
        {
            if (it == true) {
                val cocktailName = binding.textCocktailName.text.toString()
                val description = binding.cocktailDescriptionText.text.toString()

                if (isEdit) {
                    // If is edited is true, selectedCocktail can never be null
                    viewModel.editCocktail(
                        selectedCocktail!!.cocktail_id,
                        cocktailName,
                        description,
                        imageEncoded
                    )
                    Toast.makeText(this, "Edited cocktail!", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.saveCocktail(cocktailName, description, imageEncoded)
                    Toast.makeText(this, "Cocktail added.", Toast.LENGTH_SHORT).show()
                }

            }
        }

        // Only finish the activity when all the database operations are completed.
        // finishActivity will be set true only when new cocktail and all it's associated ingredients
        // are inserted in the database.
        viewModel.finishActivity.observe(this) {
            if (it == true) {
                finish()
            }
        }

        // Click listeners
        getImage()
        addIngredient()
        addStep()
        saveCocktail()
        cancelAndGoBack()
    }

    private fun saveCocktail() {
        binding.saveCocktail.setOnClickListener {
            val cocktailNameView = binding.textCocktailName
            val cocktailNameContainer = binding.textCocktailNameContainer

            val cocktailName = cocktailNameView.text
            if (cocktailName.isNotBlank()) {
                viewModel.validate()
            } else {
                Toast.makeText(
                    this,
                    "You must give the cocktail a name!",
                    Toast.LENGTH_LONG
                ).show()

                animateError(cocktailNameContainer)
            }
        }
    }

    private fun populateFields(selectedCocktail: Cocktail) {
        viewModel.getDataToPopulateFields(selectedCocktail.cocktail_id)

        binding.textCocktailName.setText(selectedCocktail.cocktail_name)
        binding.cocktailDescriptionText.setText(selectedCocktail.cocktail_description)

        imageEncoded = selectedCocktail.cocktail_image

        Glide.with(this)
            .load(selectedCocktail.cocktail_image)
            .apply(RequestOptions().error(R.drawable.no_drinks))
            .into(binding.addCocktailImage)

        viewModel.setStepsFromRawString(selectedCocktail.steps)
    }

    private fun getImage() {
        binding.addCocktailImage.setOnClickListener {
            val result = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }

            resultLauncher.launch(result)
        }
    }

    private fun cancelAndGoBack() {
        binding.createCocktailBack.setOnClickListener {
            val message = when (isEdit) {
                true -> "Cancel editing cocktail and go back?"
                else -> "Cancel creating new cocktail and go back?"
            }

            CancelAlertDialog(message) { finish() }.show(
                supportFragmentManager,
                "CancelAlertDialog"
            )
        }
    }

    private fun addIngredient() {
        binding.addIngredient.setOnClickListener {
            viewModel.addIngredient()
            hideKeyboard(this, binding.root)
        }
    }

    private fun addStep() {
        binding.addStep.setOnClickListener {
            viewModel.addStep()
            hideKeyboard(this, binding.root)
        }
    }

}