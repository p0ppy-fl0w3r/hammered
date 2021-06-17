package com.example.hammered.ingredients.createIngredient
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.hammered.R
import com.example.hammered.databinding.ActivityCreateIngredientBinding
import com.example.hammered.entities.Ingredient

class CreateIngredientActivity : AppCompatActivity() {
    private var imageUrl = ""

    private val result = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUrl = it.toString()
    }

    lateinit var binding: ActivityCreateIngredientBinding

    val viewModel: CreateIngredientViewModel by lazy {
        ViewModelProvider(this).get(CreateIngredientViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_create_ingredient)

        getImage()

        saveIngredient()

        viewModel.newIngredient.observe(this) {
            if (it != null) {
                viewModel.doneAddingIngredient()
                Toast.makeText(this, "Ingredient added successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        
    }

    private fun getImage() {
        binding.addIngredientImage.setOnClickListener {

            result.launch("image/*")
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

                viewModel.addIngredient(mIngredient)
            }
            else {
                // TODO change to a custom dialog
                AlertDialog.Builder(this)
                    .setMessage("Ingredient name is empty!!")
                    .setTitle("Warning")
                    .setCancelable(true)
                    .setNegativeButton("Ok") { dialog, _ ->
                        dialog.cancel()
                    }
                    .create()
                    .show()
            }
        }
    }

}