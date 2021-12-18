package com.fl0w3r.hammered.ingredients.createIngredient

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.databinding.ActivityCreateIngredientBinding
import com.fl0w3r.hammered.dialog.CancelAlertDialog
import com.fl0w3r.hammered.dialog.WarningDialog
import com.fl0w3r.hammered.entities.Ingredient
import com.fl0w3r.hammered.ingredients.IngredientData
import timber.log.Timber
import java.io.File


class CreateIngredientActivity : AppCompatActivity() {

    private var imageUrl = ""
    private var isEdit = false
    private var initialName = ""
    private lateinit var mIngredient: IngredientData

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                imageUrl = it.data?.data.toString()
                val newCocktailImage = binding.addIngredientImage
                // Display the selected image in the add image button
                Glide.with(this).load(imageUrl).into(newCocktailImage)
            }
        }

    private lateinit var binding: ActivityCreateIngredientBinding

    private val viewModel: CreateIngredientViewModel by lazy {
        ViewModelProvider(this).get(CreateIngredientViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_create_ingredient)

        getImage()
        saveIngredient()
        cancelAndGoBack()

        if (intent?.extras?.get("ingredient") != null) {
            mIngredient = intent?.extras?.get("ingredient") as IngredientData
            imageUrl = mIngredient.ingredient_image

            binding.textIngredientName.setText(mIngredient.ingredient_name)
            binding.ingredientDescriptionText.setText(mIngredient.ingredient_description)
            binding.createInStock.isChecked = mIngredient.inStock

            isEdit = true
            initialName = mIngredient.ingredient_name

            if (imageUrl.isNotBlank()) {
                val mFile = File(this.filesDir, imageUrl)
                if (mFile.exists()) {
                    Timber.e("Got from file")
                    Glide.with(this).load(mFile).into(binding.addIngredientImage)
                } else {
                    Timber.e("Got from URI")
                    Glide.with(this).load(imageUrl).into(binding.addIngredientImage)
                }
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
                        ingredient_image = imageUrl
                    )

                    viewModel.checkIngredient(mIngredient)
                } else {
                    WarningDialog(R.layout.warning_dialog_layout).show(
                        supportFragmentManager,
                        "WarningDialog"
                    )
                }
            } else {
                mIngredient.ingredient_name = ingredientName
                mIngredient.ingredient_description = ingredientDescription
                mIngredient.inStock = isInStock
                mIngredient.ingredient_image = imageUrl
                viewModel.checkIngredient(mIngredient.asIngredient(), initialName)
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