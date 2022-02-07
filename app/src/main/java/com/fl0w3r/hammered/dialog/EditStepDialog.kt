package com.fl0w3r.hammered.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.fl0w3r.hammered.R
import java.lang.IllegalStateException

class EditStepDialog(private val initialStep: String, private val onSave: (String) -> Unit) :
    DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflater = requireActivity().layoutInflater
        val dialogView = layoutInflater.inflate(R.layout.edit_step_dialog, null)

        val stepTextArea = dialogView.findViewById<EditText>(R.id.dialogStep)
        stepTextArea.setText(initialStep)
        stepTextArea.requestFocus()

        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.DarkButtonDialog)
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton("Save") { _, _ ->
                    onSave(stepTextArea.text.toString())
                }
                .setView(dialogView)
            builder.create()
        } ?: throw IllegalStateException("Activity can't be null")
    }
}