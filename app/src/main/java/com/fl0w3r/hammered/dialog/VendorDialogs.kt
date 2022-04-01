package com.fl0w3r.hammered.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.fl0w3r.hammered.R

class VendorDialogs(private val onVendorSelection : (Int) -> Unit): DialogFragment() {
    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val layoutInflater = requireActivity().layoutInflater
        val dialogView = layoutInflater.inflate(R.layout.vendor_dialog, null)

        for (id in listOf(
            R.id.vendor_amazon,
            R.id.vendor_daraz,
            R.id.vendor_cheers
        )){
            dialogView.findViewById<ImageView>(id).setOnClickListener {
                onVendorSelection(id)
                dialog?.cancel()
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