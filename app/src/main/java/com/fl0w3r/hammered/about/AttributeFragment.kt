package com.fl0w3r.hammered.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.databinding.FragmentAboutBinding
import com.fl0w3r.hammered.databinding.FragmentAttributeBinding
import com.fl0w3r.hammered.databinding.FragmentHammerMeBinding


class AttributeFragment : Fragment() {

    private lateinit var binding: FragmentAttributeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_attribute, container, false)

        binding.attrView.loadUrl("file:///android_asset/attr.html")

        return binding.root

    }

}