package com.example.hammered.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.hammered.R
import com.example.hammered.databinding.FragmentIngredientDetailsBinding


class IngredientDetailsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val ingredient = IngredientDetailsFragmentArgs.fromBundle(requireArguments()).ingredient

        val binding = DataBindingUtil.inflate<FragmentIngredientDetailsBinding>(
            inflater,
            R.layout.fragment_ingredient_details,
            container,
            false
        )

        binding.ingredient = ingredient

        // Inflate the layout for this fragment
        return binding.root
    }

}