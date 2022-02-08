package com.fl0w3r.hammered.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.fl0w3r.hammered.R
import java.lang.IllegalStateException

class ImageCaptureDialog(val cameraButton: () -> Unit, val fileButton: () -> Unit, val placeholderButton: () -> Unit) :
    DialogFragment() {
    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflater = requireActivity().layoutInflater
        val dialogView = layoutInflater.inflate(R.layout.image_capture_layout, null)


        dialogView.findViewById<Button>(R.id.cameraButton).setOnClickListener {
            cameraButton()
            this.dismiss()
        }
        dialogView.findViewById<Button>(R.id.fileButton).setOnClickListener {
            fileButton()
            this.dismiss()
        }
        dialogView.findViewById<Button>(R.id.tempButton).setOnClickListener {
            placeholderButton()
            this.dismiss()
        }

        return activity?.let {
            val builder = AlertDialog.Builder(it)
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                .setView(dialogView)
            builder.create()
        } ?: throw IllegalStateException("Activity can't be null")
    }
}