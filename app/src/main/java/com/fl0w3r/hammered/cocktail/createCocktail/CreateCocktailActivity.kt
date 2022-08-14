package com.fl0w3r.hammered.cocktail.createCocktail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fl0w3r.hammered.Constants
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.cocktail.CocktailData
import com.fl0w3r.hammered.databinding.ActivityCreateCocktailBinding
import com.fl0w3r.hammered.dialog.*
import com.fl0w3r.hammered.entities.Cocktail
import com.fl0w3r.hammered.entities.relations.IngredientCocktailRef
import com.fl0w3r.hammered.utils.IOUtils
import com.fl0w3r.hammered.utils.UiUtils
import com.fl0w3r.hammered.utils.UiUtils.animateError
import com.fl0w3r.hammered.utils.UiUtils.hideKeyboard
import com.fl0w3r.hammered.wrappers.StepsWrapper
import timber.log.Timber
import java.io.File

// FIXME Image bug
// TODO save image on folder only when save is clicked.
// TODO see create ingredients
// TRIAL temporary patch for image bug.
class CreateCocktailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateCocktailBinding
    private lateinit var ingredientNameList: MutableList<String>
    private lateinit var autoFillAdapter: ArrayAdapter<String>
    private lateinit var unitsAdapter: ArrayAdapter<CharSequence>

    // TODO remove image encoded
    private var imageUrl = ""
    private var imageEncoded = ""


    private var isEdit = false

    private val fileResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            if (it.resultCode == Activity.RESULT_OK) {

                val cocktailImageDir = File(filesDir.path + "/${Constants.COCKTAIL_DIR}")

                if (!cocktailImageDir.exists()) {
                    cocktailImageDir.mkdir()
                }

                val outputFile = File(cocktailImageDir, "cocktail_${System.currentTimeMillis()}")
                imageUrl = outputFile.absolutePath
                imageEncoded = imageUrl

                it.data?.let { imageIntent ->
                    imageIntent.data?.let { imageUri ->
                        IOUtils.writeToFilesDir(
                            uri = imageUri,
                            outputFile = outputFile,
                            contentResolver = contentResolver
                        )

                        // Display the selected image in the add image button
                        Glide.with(this).load(imageUrl).into(binding.addCocktailImage)
                    }

                }
            }
        }

    // Placeholder Uri
    var capturedImageUri: Uri? = null

    // ActivityResultContract for capturing an image
    val takePicture =
        registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { imageCaptured ->

            if (imageCaptured) {

                val cocktailImageDir = File(filesDir.path + "/${Constants.COCKTAIL_DIR}")

                if (!cocktailImageDir.exists()) {
                    cocktailImageDir.mkdir()
                }

                capturedImageUri?.let {

                    val outputFile =
                        File(cocktailImageDir, "cocktail_${System.currentTimeMillis()}")
                    imageUrl = outputFile.absolutePath
                    imageEncoded = imageUrl


                    IOUtils.writeToFilesDir(
                        uri = it,
                        outputFile = outputFile,
                        contentResolver = contentResolver
                    )
                    // Display the selected image in the add image button
                    Glide.with(this).load(imageUrl).into(binding.addCocktailImage)
                }

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

        val adapter = CocktailIngredientAdapter(
            onDeleteListener = ItemOnClickListener { itemNumber, _ ->
                viewModel.removeIngredient(itemNumber)
            },
            onEditListener = ItemOnClickListener { itemNumber, ingredient ->
                showIngredientEdit(itemNumber, ingredient)
            }
        )

        val stepsAdapter = StepsRecyclerAdapter(onDeleteListenerSteps = StepsClickListener {
            viewModel.removeStep(it)
        },
            onEditListenerSteps = StepsClickListener {
                showStepsEdit(it)
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

    private fun showStepsEdit(step: StepsWrapper) {
        EditStepDialog(initialStep = step.steps) {
            if (it.isBlank()) {
                Toast.makeText(this, "Step can't be empty.", Toast.LENGTH_SHORT).show()
            } else {
                step.steps = it
                viewModel.editStep(step)
            }

        }.show(supportFragmentManager, "EditStepDialog")
    }

    private fun showIngredientEdit(itemNumber: Int, ingredient: IngredientCocktailRef) {
        EditIngredientDialog(autoFillAdapter, unitsAdapter, ingredient) {
            updateIngredient(itemNumber, it)
        }.show(
            supportFragmentManager,
            "EditIngredientDialog"
        )
    }

    private fun setFieldValues() {
        viewModel.selectedCocktail.observe(this) {
            if (it != null) {
                populateFields(it)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setIngredientList(ingredientAdapter: CocktailIngredientAdapter) {
        viewModel.ingredientList.observe(this) {
            if (it != null) {
                ingredientAdapter.submitList(it)
                ingredientAdapter.notifyDataSetChanged()
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

    private fun closeActivity() {
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
                autoFillAdapter =
                    ArrayAdapter(
                        this,
                        R.layout.autofill_dialog_item,
                        ingredientNameList
                    )
                binding.newIngredientName.threshold = 2
                binding.newIngredientName.setAdapter(autoFillAdapter)
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
        unitsAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.units_array,
            android.R.layout.simple_spinner_item
        )
        unitsAdapter.also { adapter ->
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
        ImageCaptureDialog(
            { getCapturedImage() },
            { getImageFile() },
            { getPlaceHolderImage() }).show(
            supportFragmentManager,
            "CocktailImageCaptureDialog"
        )

    }

    private fun getPlaceHolderImage() {
        PlaceholderDialog(
            imageList = listOf(
                R.drawable.placeholder_1,
                R.drawable.placeholder_2,
                R.drawable.placeholder_3,
                R.drawable.placeholder_4,
                R.drawable.placeholder_5,
                R.drawable.placeholder_6,
                R.drawable.placeholder_7,
                R.drawable.placeholder_8,
                R.drawable.placeholder_9
            )
        ) {
            setPlaceholderImage(it)
        }.show(supportFragmentManager, "PlaceholderDialog")
    }

    private fun setPlaceholderImage(layoutId: Int) {
        val drawable = when (layoutId) {
            R.id.placeholder_1 -> R.drawable.placeholder_1
            R.id.vendor_cheers -> R.drawable.placeholder_2
            R.id.placeholder_3 -> R.drawable.placeholder_3
            R.id.placeholder_4 -> R.drawable.placeholder_4
            R.id.placeholder_5 -> R.drawable.placeholder_5
            R.id.placeholder_6 -> R.drawable.placeholder_6
            R.id.placeholder_7 -> R.drawable.placeholder_7
            R.id.placeholder_8 -> R.drawable.placeholder_8
            else -> R.drawable.placeholder_9
        }

        imageEncoded =
            UiUtils.encodeToBase64(ContextCompat.getDrawable(this, drawable)!!.toBitmap())

        Glide.with(this).load(drawable)
            .apply(RequestOptions().error(R.drawable.no_drinks))
            .into(binding.addCocktailImage)
    }

    private fun getCapturedImage() {

        // Get the correct media Uri for specific Android build version
        val imageUri =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL_PRIMARY
                )
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

        // Temporary name for the file.
        val imageDetails = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, "")
        }


        contentResolver.insert(imageUri, imageDetails).let {
            // Save the generated Uri using our placeholder
            capturedImageUri = it

            // Capture your image
            takePicture.launch(capturedImageUri)
        }
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

    private fun resetIngredientField() {
        binding.newIngredientName.text.clear()
        binding.newQuantity.text.clear()
        binding.isOptional.isChecked = false
        binding.isGarnishCheck.isChecked = false
    }

    private fun addIngredient() {
        binding.addIngredientButton.setOnClickListener {

            val ingredientName = binding.newIngredientName.text.toString()
            if (ingredientName !in ingredientNameList) {
                animateError(binding.newIngredientName)
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
            val quantity = when (binding.newQuantity.text.toString().isBlank()) {
                true -> {
                    animateError(binding.newQuantity)
                    return@setOnClickListener
                }
                else -> {
                    if (binding.newQuantity.text.toString().toFloatOrNull() == null) {
                        animateError(binding.newQuantity)
                        return@setOnClickListener
                    } else {
                        binding.newQuantity.text.toString().toFloat()
                    }
                }
            }
            val quantityUnit = binding.unitSpinner.selectedItem.toString()
            val isOptional = binding.isOptional.isChecked
            val isGarnish = binding.isGarnishCheck.isChecked

            viewModel.addIngredient(
                ingredientName, quantity, isOptional, isGarnish, quantityUnit
            )

            resetIngredientField()

            hideKeyboard(this, binding.root)
        }
    }

    private fun updateIngredient(position: Int, ingredientRef: IngredientCocktailRef) {
        if (ingredientRef.ingredient_name !in ingredientNameList) {
            if (ingredientRef.ingredient_name.isBlank()) {
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
            return
        }
        if (ingredientRef.quantity == null) {
            Toast.makeText(
                this,
                "Quantity not valid!",
                Toast.LENGTH_SHORT
            ).show()
            return
        } else if (ingredientRef.quantity!! <= 0) {
            Toast.makeText(
                this,
                "Quantity can not be 0 or less!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        viewModel.updateIngredient(position, ingredientRef)
    }

    private fun addStep() {
        binding.addStep.setOnClickListener {
            if (binding.newStepText.text.toString().isBlank()) {
                animateError(binding.newStepText)
                Toast.makeText(this, "Steps can't be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.addStep(binding.newStepText.text.toString())

            binding.newStepText.text.clear()
            hideKeyboard(this, binding.root)
        }
    }

}