package com.example.hammered.cocktail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.hammered.MainActivity
import com.example.hammered.R
import com.example.hammered.databinding.CocktailFragmentBinding
import com.example.hammered.entities.Cocktail

class CocktailFragment : Fragment() {

    private lateinit var viewModel: CocktailViewModel
    private lateinit var binding: CocktailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.cocktail_fragment, container, false)

        val cok1 = Cocktail(1, "Tonic", "Strong", "1. make it", false, "vodka.webp")
        val cok2 = Cocktail(2, "Bionic", "Light", "1. make it", true, "gin.png")

        val adapter = CocktailAdapter(CocktailClickListener { })
        adapter.submitList(listOf(cok1, cok2))

        binding.cocktailRecycler.adapter = adapter

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CocktailViewModel::class.java)
        // TODO: Use the ViewModel


    }

}