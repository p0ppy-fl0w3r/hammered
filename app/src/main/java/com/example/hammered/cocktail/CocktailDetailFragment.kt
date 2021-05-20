package com.example.hammered.cocktail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.hammered.R
import com.example.hammered.databinding.FragmentCocktailDetailBinding


class CocktailDetailFragment : Fragment() {

    lateinit var binding: FragmentCocktailDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_cocktail_detail, container, false)

        val selectedCocktail = CocktailDetailFragmentArgs.fromBundle(requireArguments()).cocktail
        binding.cocktailData = selectedCocktail

        return binding.root
    }

}