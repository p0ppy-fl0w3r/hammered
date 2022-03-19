package com.fl0w3r.hammered.recommendation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.databinding.FragmentHammerMeBinding


class HammerMeFragment : Fragment() {

    private lateinit var binding: FragmentHammerMeBinding

    private val viewModel:HammerViewModel by lazy {
        ViewModelProvider(this)[HammerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // TODO add loading animation to import and export screens
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hammer_me, container, false)

        binding.testButton.setOnClickListener {
            viewModel.getRecommendation()
        }

        return binding.root
    }

}