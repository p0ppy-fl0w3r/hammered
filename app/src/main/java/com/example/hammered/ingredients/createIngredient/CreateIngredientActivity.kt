package com.example.hammered.ingredients.createIngredient

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.hammered.R
import com.example.hammered.databinding.ActivityCreateIngredientBinding
import com.example.hammered.dialog.CancelAlertDialog
import com.example.hammered.dialog.WarningDialog
import com.example.hammered.entities.Ingredient

class CreateIngredientActivity : AppCompatActivity(), CancelAlertDialog.NoticeDialogListener {
    private var imageUrl = ""

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

            if (ingredientName.isNotBlank()) {
                val mIngredient = Ingredient(
                    ingredient_name = ingredientName,
                    ingredient_description = ingredientDescription,
                    inStock = isInStock,
                    inCart = false,
                    ingredient_image = imageUrl
                )

                viewModel.checkIngredient(mIngredient)
            }
            else {
                WarningDialog(R.layout.warning_dialog_layout).show(
                    supportFragmentManager,
                    "WarningDialog"
                )
            }
        }
    }

    private fun cancelAndGoBack() {
        binding.createIngredientBack.setOnClickListener {
            CancelAlertDialog("ingredient").show(supportFragmentManager, "CancelAlertDialog")
        }
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        finish()
    }

}