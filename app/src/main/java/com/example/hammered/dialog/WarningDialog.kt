package com.example.hammered.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.hammered.R

class WarningDialog(private val dialogLayout: Int) : DialogFragment() {

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val layoutInflater = requireActivity().layoutInflater

        return activity?.let {
            val builder = AlertDialog.Builder(it)
                .setView(layoutInflater.inflate(dialogLayout, null))
                .setNegativeButton("Ok") { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}