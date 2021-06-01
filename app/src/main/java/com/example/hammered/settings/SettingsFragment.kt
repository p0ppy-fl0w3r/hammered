package com.example.hammered.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.hammered.R
import com.example.hammered.databinding.SettingsFragmentBinding

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<SettingsFragmentBinding>(
            inflater,
            R.layout.settings_fragment,
            container,
            false
        )

        return binding.root
    }

}