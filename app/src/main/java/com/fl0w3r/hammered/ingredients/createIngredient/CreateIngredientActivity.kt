package com.fl0w3r.hammered.ingredients.createIngredient

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fl0w3r.hammered.Constants
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.databinding.ActivityCreateIngredientBinding
import com.fl0w3r.hammered.dialog.CancelAlertDialog
import com.fl0w3r.hammered.dialog.ImageCaptureDialog
import com.fl0w3r.hammered.dialog.PlaceholderDialog
import com.fl0w3r.hammered.dialog.WarningDialog
import com.fl0w3r.hammered.entities.Ingredient
import com.fl0w3r.hammered.utils.IOUtils
import  com.fl0w3r.hammered.utils.UiUtils
import java.io.File

// TRIAL temporary patch for image bug.
class CreateIngredientActivity : AppCompatActivity() {

    private var imageUrl = ""
    private var isEdit = false
    private var initialName = ""
    private var imageEncoded = ""

    private lateinit var currentIngredient: Ingredient

    // Placeholder Uri
    var capturedImageUri: Uri? = null

    // ActivityResultContract for capturing an image
    val takePicture =
        registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { imageCaptured ->

            if (imageCaptured) {

                val cocktailImageDir = File(filesDir.path + "/${Constants.INGREDIENT_DIR}")

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
                    Glide.with(this).load(imageUrl).into(binding.addIngredientImage)
                }

            }
        }


    private val fileResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {

                val cocktailImageDir = File(filesDir.path + "/${Constants.INGREDIENT_DIR}")

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
                        Glide.with(this).load(imageUrl).into(binding.addIngredientImage)
                    }
                }
            }

        }


    private lateinit var binding: ActivityCreateIngredientBinding

    private val viewModel: CreateIngredientViewModel by lazy {
        ViewModelProvider(this)[CreateIngredientViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_create_ingredient)

        // Click listeners
        binding.addIngredientImage.setOnClickListener { openImageDialog() }

        saveIngredient()
        cancelAndGoBack()

        val ingredientId = intent?.extras?.getLong("ingredient")

        if (ingredientId != null) {

            viewModel.getIngredient(ingredientId)

        }

        viewModel.currentIngredient.observe(this) { mIngredient ->
            if (mIngredient != null) {
                currentIngredient = mIngredient

                imageEncoded = mIngredient.ingredient_image

                binding.textIngredientName.setText(mIngredient.ingredient_name)
                binding.ingredientDescriptionText.setText(mIngredient.ingredient_description)
                binding.createInStock.isChecked = mIngredient.inStock
                Glide.with(this).load(mIngredient.ingredient_image)
                    .apply(RequestOptions().error(R.drawable.no_drinks))
                    .into(binding.addIngredientImage)

                isEdit = true
                initialName = mIngredient.ingredient_name
            }
        }

        viewModel.newIngredient.observe(this) {
            if (it != null) {
                viewModel.doneAddingIngredient()
                Toast.makeText(this, "Ingredient added successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        viewModel.ingredientExists.observe(this) {
            if (it == true) {
                WarningDialog(R.layout.ingredient_exists_dialog).show(
                    supportFragmentManager,
                    "IngredientExistsWarning"
                )
                viewModel.checkedIngredient()
            }
        }
    }

    private fun openImageDialog() {
        ImageCaptureDialog(
            { getCapturedImage() },
            { getImageFile() },
            { getPlaceHolderImage() }).show(supportFragmentManager, "ImageCaptureDialog")

    }

    private fun getPlaceHolderImage() {
        PlaceholderDialog(
            imageList = listOf(
                R.drawable.black_bottle,
                R.drawable.pink_bottle,
                R.drawable.vodka_bottle,
                R.drawable.whiskey_bottle,
                R.drawable.fruit_1,
                R.drawable.fruit_2,
                R.drawable.fruit_3,
                R.drawable.fruit_4,
                R.drawable.spices
            )
        ) {
            setPlaceholderImage(it)
        }.show(supportFragmentManager, "PlaceholderDialog")
    }

    private fun setPlaceholderImage(layoutId: Int) {
        val drawable = when (layoutId) {
            R.id.placeholder_1 -> R.drawable.black_bottle
            R.id.vendor_cheers -> R.drawable.pink_bottle
            R.id.placeholder_3 -> R.drawable.vodka_bottle
            R.id.placeholder_4 -> R.drawable.whiskey_bottle
            R.id.placeholder_5 -> R.drawable.fruit_1
            R.id.placeholder_6 -> R.drawable.fruit_2
            R.id.placeholder_7 -> R.drawable.fruit_3
            R.id.placeholder_8 -> R.drawable.fruit_4
            else -> R.drawable.spices
        }

        imageEncoded =
            UiUtils.encodeToBase64(ContextCompat.getDrawable(this, drawable)!!.toBitmap())

        Glide.with(this).load(drawable)
            .apply(RequestOptions().error(R.drawable.no_drinks))
            .into(binding.addIngredientImage)
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

    private fun saveIngredient() {
        binding.createSave.setOnClickListener {
            val ingredientName = binding.textIngredientName.text.toString()
            val ingredientDescription = binding.ingredientDescriptionText.text.toString()
            val isInStock = binding.createInStock.isChecked
            if (!isEdit) {
                if (ingredientName.isNotBlank()) {
                    val mIngredient = Ingredient(
                        ingredient_name = ingredientName,
                        ingredient_description = ingredientDescription,
                        inStock = isInStock,
                        inCart = false,
                        ingredient_image = imageEncoded
                    )

                    viewModel.checkIngredient(mIngredient)
                } else {
                    WarningDialog(R.layout.warning_dialog_layout).show(
                        supportFragmentManager,
                        "WarningDialog"
                    )
                }
            } else {
                currentIngredient.ingredient_name = ingredientName
                currentIngredient.ingredient_description = ingredientDescription
                currentIngredient.inStock = isInStock
                currentIngredient.ingredient_image = imageEncoded
                viewModel.checkIngredient(currentIngredient, initialName)
            }
        }
    }

    private fun cancelAndGoBack() {
        binding.createIngredientBack.setOnClickListener {

            val message = when (isEdit) {
                true -> "Cancel editing ingredient and go back?"
                else -> "Cancel creating new ingredient and go back?"
            }

            CancelAlertDialog(message) { finish() }.show(
                supportFragmentManager,
                "CancelAlertDialog"
            )
        }
    }
}