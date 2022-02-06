package com.fl0w3r.hammered.cocktail.createCocktail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fl0w3r.hammered.Constants
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.cocktail.CocktailData
import com.fl0w3r.hammered.databinding.ActivityCreateCocktailBinding
import com.fl0w3r.hammered.dialog.CancelAlertDialog
import com.fl0w3r.hammered.dialog.ImageCaptureDialog
import com.fl0w3r.hammered.entities.Cocktail
import com.fl0w3r.hammered.utils.UiUtils
import com.fl0w3r.hammered.utils.UiUtils.animateError
import com.fl0w3r.hammered.utils.UiUtils.hideKeyboard

class CreateCocktailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateCocktailBinding
    private lateinit var ingredientNameList: MutableList<String>

    private var imageUrl = ""
    private var imageEncoded = ""

    private var isEdit = false

    private val fileResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                imageUrl = it.data?.data.toString()

                val inputStream = contentResolver.openInputStream(it.data?.data!!)

                imageEncoded = UiUtils.encodeToBase64(BitmapFactory.decodeStream(inputStream))

                // Display the selected image in the add image button
                Glide.with(this).load(imageUrl).into(binding.addCocktailImage)
            }
        }

    private val cameraResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val imageData = it.data?.extras?.get("data")
                imageEncoded = UiUtils.encodeToBase64(imageData as Bitmap)

                Glide.with(this).load(imageData)
                    .apply(RequestOptions().error(R.drawable.no_drinks))
                    .into(binding.addCocktailImage)
            }
        }

    private val viewModel: CreateCocktailViewModel by lazy {
        ViewModelProvider(this)[CreateCocktailViewModel::class.java]
    }


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

        // Set adapter for units spinner
        setUnitsAdapter()

        // Array adapter for ingredient name AutoCompleteTextView
        setAutoFillAdapter()

        val adapter = CreateCocktailAdapter(onDeleteListener = ItemOnClickListener { itemNumber ->
            viewModel.removeIngredient(itemNumber)
        },
            onEditListener = ItemOnClickListener {
                // TODO add a edit option.
            })

        val stepsAdapter = StepsRecyclerAdapter(ClickListener {
            viewModel.removeStep(it)
        })

        binding.stepsRecycler.adapter = stepsAdapter
        binding.ingRefRecycler.adapter = adapter

        // Populate fields if you're editing a cocktail
        setFieldValues()

        // Populate the ingredient list.
        setIngredientList(adapter)

        // Populate the steps list.
        setStepsList(stepsAdapter)

        // Observe if the steps are valid.
        observeSteps()

        // Observe if the ingredients are valid.
        observeIngredients(selectedCocktail)

        // Close the activity when everything is ok
        closeActivity()

        // Click listeners
        selectImageDialog()
        addIngredient()
        addStep()
        saveCocktail()
        cancelAndGoBack()
    }

    private fun setFieldValues() {
        viewModel.selectedCocktail.observe(this) {
            if (it != null) {
                populateFields(it)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setIngredientList(adapter: CreateCocktailAdapter) {
        viewModel.ingredientList.observe(this) {
            if (it != null) {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setStepsList(stepsAdapter: StepsRecyclerAdapter) {
        viewModel.stepsList.observe(this) {
            stepsAdapter.submitList(it)
            stepsAdapter.notifyDataSetChanged()
        }
    }

    private fun observeSteps() {
        viewModel.stepsValid.observe(this) {
            when (it) {
                Constants.VALUE_OK -> {
                    viewModel.checkIngredient()
                }
                Constants.NO_VALUES -> {
                    animateError(binding.newStepContainer)
                    Toast.makeText(
                        this,
                        "There are no steps!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun observeIngredients(selectedCocktail: CocktailData?) {
        viewModel.ingredientValid.observe(this)
        {

            when (it) {
                Constants.VALUE_OK -> {
                    saveNewCocktail(selectedCocktail)
                }

                Constants.NO_VALUES -> {
                    Toast.makeText(
                        this,
                        "You need to add an ingredient!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    throw IllegalArgumentException("Error code or item index not valid $it")
                }
            }
        }
    }

    private fun closeActivity(){
        viewModel.finishActivity.observe(this) {
            if (it == true) {
                finish()
            }
        }
    }

    private fun setAutoFillAdapter() {
        viewModel.allIngredientList.observe(this) {
            if (it != null) {
                ingredientNameList = mutableListOf()
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
                binding.RefIngredientName.threshold = 2
                binding.RefIngredientName.setAdapter(mAdapter)
            }
        }
    }

    private fun saveCocktail() {
        binding.saveCocktail.setOnClickListener {
            val cocktailNameView = binding.textCocktailName

            val cocktailName = cocktailNameView.text
            if (cocktailName.isNotBlank()) {
                viewModel.checkSteps()
            } else {
                Toast.makeText(
                    this,
                    "You must give the cocktail a name!",
                    Toast.LENGTH_LONG
                ).show()

                animateError(cocktailNameView)
            }
        }
    }

    private fun saveNewCocktail(selectedCocktail: CocktailData?) {
        val cocktailName = binding.textCocktailName.text.toString()
        val description = binding.cocktailDescriptionText.text.toString()

        if (isEdit) {
            // If is edited is true, selectedCocktail can never be null
            selectedCocktail?.let {
                viewModel.editCocktail(
                    it.cocktail_id,
                    cocktailName,
                    description,
                    imageEncoded
                )

                Toast.makeText(this, "Edited cocktail!", Toast.LENGTH_SHORT).show()
            }

        } else {
            viewModel.saveCocktail(cocktailName, description, imageEncoded)
            Toast.makeText(this, "Cocktail added.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUnitsAdapter() {
        ArrayAdapter.createFromResource(
            this,
            R.array.units_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.unitSpinner.adapter = adapter
        }
    }

    private fun selectImageDialog() {

        binding.addCocktailImage.setOnClickListener { openImageDialog() }
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


    private fun openImageDialog() {
        ImageCaptureDialog({ getCapturedImage() }, { getImageFile() }).show(
            supportFragmentManager,
            "CocktailImageCaptureDialog"
        )

    }

    private fun getPlaceHolderImage() {
        TODO("Add placeholder images")
    }

    private fun getCapturedImage() {
        val captureResult = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        cameraResultLauncher.launch(captureResult)
    }

    private fun getImageFile() {

        val result = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }

        fileResultLauncher.launch(result)
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
        binding.addIngredientButton.setOnClickListener {

            val ingredientName = binding.RefIngredientName.text.toString()
            if (ingredientName !in ingredientNameList) {
                animateError(binding.RefIngredientName)
                if (ingredientName.isBlank()) {
                    Toast.makeText(
                        this,
                        "Ingredient name can't be blank!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Looks like the ingredient does not exist...yet!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@setOnClickListener
            }
            val quantity = when (binding.refQuantity.text.toString().isBlank()) {
                true -> {
                    animateError(binding.refQuantity)
                    return@setOnClickListener
                }
                else -> binding.refQuantity.text.toString().toFloat()
            }
            val quantityUnit = binding.unitSpinner.selectedItem.toString()
            val isOptional = binding.isOptional.isChecked
            val isGarnish = binding.isGarnishCheck.isChecked

            viewModel.addIngredient(
                ingredientName, quantity, isOptional, isGarnish, quantityUnit
            )
            hideKeyboard(this, binding.root)
        }
    }

    private fun addStep() {
        binding.addStep.setOnClickListener {
            if (binding.newStepText.text.toString().isBlank()) {
                animateError(binding.newStepText)
                Toast.makeText(this, "Steps can't be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.addStep(binding.newStepText.text.toString())

            hideKeyboard(this, binding.root)
        }
    }

}