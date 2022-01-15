package com.fl0w3r.hammered.ingredients.createIngredient

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.databinding.ActivityCreateIngredientBinding
import com.fl0w3r.hammered.dialog.CancelAlertDialog
import com.fl0w3r.hammered.dialog.WarningDialog
import com.fl0w3r.hammered.entities.Ingredient
import  com.fl0w3r.hammered.utils.UiUtils
import timber.log.Timber


class CreateIngredientActivity : AppCompatActivity() {

    private var imageUrl = ""
    private var isEdit = false
    private var initialName = ""
    private var imageEncoded = ""

    private lateinit var currentIngredient: Ingredient

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                imageUrl = it.data?.data.toString()

                val inputStream = contentResolver.openInputStream(it.data?.data!!)

                imageEncoded = UiUtils.encodeToBase64(BitmapFactory.decodeStream(inputStream))

                val newCocktailImage = binding.addIngredientImage
                // Display the selected image in the add image button
                Glide.with(this).load(imageUrl)
                    .apply(RequestOptions().error(R.drawable.no_drinks)).into(newCocktailImage)
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

        getImage()
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

    private fun getImage() {
        binding.addIngredientImage.setOnClickListener {
            val result = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }

            resultLauncher.launch(result)

        }
    }

    private fun saveIngredient() {
        binding.createSave.setOnClickListener {
            val ingredientName = binding.textIngredientName.text.toString()
            val ingredientDescription = binding.ingredientDescriptionText.text.toString()
            val isInStock = binding.createInStock.isChecked
            if (!isEdit) {
                if (ingredientName.isNotBlank()) {
                    val mIngredient = Ingredient(
                        ingredient_id = Long.MIN_VALUE,
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