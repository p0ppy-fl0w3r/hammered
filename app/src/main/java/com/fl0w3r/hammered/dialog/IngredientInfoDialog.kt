package com.fl0w3r.hammered.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.mixer.IngredientMixerItem
import kotlinx.android.synthetic.main.ingredient_info_dialog.view.*
import java.lang.IllegalArgumentException

class IngredientInfoDialog(val ingredient: IngredientMixerItem): DialogFragment() {

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

       return activity?.let {
            val infoView = it.layoutInflater.inflate(R.layout.ingredient_info_dialog, null)

            infoView.ingredientName.text = ingredient.ingredientName
            infoView.ingredientDescription.text = ingredient.ingredientDescription

            val builder = AlertDialog.Builder(it)
                .setCancelable(true)
                .setView(infoView)

           builder.create()

        } ?: throw IllegalArgumentException("Activity can not be null")
    }

}