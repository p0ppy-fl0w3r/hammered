package com.example.hammered.utils

import android.view.View
import android.widget.AdapterView
import androidx.databinding.InverseBindingListener

class SpinnerItemChangeListener(private val inverseBindingListener: InverseBindingListener) :
    AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        inverseBindingListener.onChange()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}