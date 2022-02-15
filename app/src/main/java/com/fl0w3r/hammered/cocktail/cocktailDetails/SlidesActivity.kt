package com.fl0w3r.hammered.cocktail.cocktailDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fl0w3r.hammered.Constants
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.cocktail.CocktailData
import com.fl0w3r.hammered.databinding.ActivitySlidesBinding

class SlidesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySlidesBinding
    private val viewModel by lazy {
        ViewModelProvider(this)[SlidesViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_slides)

        val cocktailData = intent.getParcelableExtra<CocktailData>(Constants.COCKTAIL_DATA)!!

        viewModel.getIngredients(cocktailData)

        val adapter = SlidesAdapter()

        viewModel.stepIngredient.observe(this){
            if (it != null){
                adapter.submitList(it)
            }
        }

        binding.slidesPager.adapter = adapter

    }
}