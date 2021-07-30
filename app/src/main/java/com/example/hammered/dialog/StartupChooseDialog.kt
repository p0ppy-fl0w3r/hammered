package com.example.hammered.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import com.example.hammered.R

class StartupChooseDialog(private val currentSelected: Int, val func: (Int) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.AlertTheme)
                .setTitle(getString(R.string.startup_dialog_title))
                .setSingleChoiceItems(R.array.choice_array, currentSelected) { _, item_number ->
                    func(item_number)
                }
                .setPositiveButton("Done") { dialog, _ ->
                    dialog.cancel()
                }

            builder.create()
        } ?: throw IllegalArgumentException("Activity cannot be null.")
    }
}