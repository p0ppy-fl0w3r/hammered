package com.fl0w3r.hammered.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.fl0w3r.hammered.Constants
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.entities.relations.IngredientCocktailRef
import java.lang.IllegalStateException

class EditIngredientDialog(
    private val autoCompleteAdapter: ArrayAdapter<String>,
    private val unitAdapter: ArrayAdapter<CharSequence>,
    private val ingredientRef: IngredientCocktailRef,
    private val updateIngredient: (IngredientCocktailRef) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflater = requireActivity().layoutInflater
        val dialogView = layoutInflater.inflate(R.layout.edit_ingredient_dialog, null)

        val ingredientName =
            dialogView.findViewById<AutoCompleteTextView>(R.id.updateIngredientName)
        ingredientName.setAdapter(autoCompleteAdapter)
        ingredientName.setText(ingredientRef.ingredient_name)

        val quantity = dialogView.findViewById<EditText>(R.id.updateQuantity)
        quantity.setText(ingredientRef.quantity.toString())

        val quantityUnits = dialogView.findViewById<Spinner>(R.id.unitSpinnerUpdate)
        quantityUnits.adapter = unitAdapter
        quantityUnits.setSelection(Constants.UNITS.indexOf(ingredientRef.quantityUnit))

        val isOptional = dialogView.findViewById<CheckBox>(R.id.optionalUpdate)
        isOptional.isChecked = ingredientRef.isOptional

        val isGarnish = dialogView.findViewById<CheckBox>(R.id.garnishCheck)
        isGarnish.isChecked = ingredientRef.isGarnish

        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.DarkButtonDialog)
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton("Save") { _, _ ->
                    ingredientRef.apply {
                        this.ingredient_name = ingredientName.text.toString()
                        this.quantity = quantity.text.toString().toFloatOrNull()
                        this.quantityUnit = quantityUnits.selectedItem as String
                        this.isGarnish = isGarnish.isChecked
                        this.isOptional = isOptional.isChecked
                    }
                    updateIngredient(ingredientRef)
                }
                .setView(dialogView)
            builder.create()
        } ?: throw IllegalStateException("Activity can't be null")
    }



}