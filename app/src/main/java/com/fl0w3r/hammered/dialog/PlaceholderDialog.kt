package com.fl0w3r.hammered.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.fl0w3r.hammered.R

class PlaceholderDialog(private val onImageSelect: (Int) -> Unit) : DialogFragment() {
    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val layoutInflater = requireActivity().layoutInflater
        val dialogView = layoutInflater.inflate(R.layout.placeholder_dialog_layout, null)

        for (id in listOf(
            R.id.placeholder_1,
            R.id.placeholder_2,
            R.id.placeholder_3,
            R.id.placeholder_4,
            R.id.placeholder_5,
            R.id.placeholder_6,
            R.id.placeholder_7,
            R.id.placeholder_8,
            R.id.placeholder_9,
        )) {
            dialogView.findViewById<ImageView>(id).setOnClickListener {
                onImageSelect(it.id)
                this.dismiss()
            }
        }


        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.DarkButtonDialog)
                .setView(dialogView)
                .setNegativeButton("Cancel") { d, _ ->
                    d.cancel()
                }

            builder.create()
        } ?: throw IllegalArgumentException("Activity cannot be null.")
    }
}