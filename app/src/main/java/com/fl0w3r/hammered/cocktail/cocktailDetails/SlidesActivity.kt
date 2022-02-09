package com.fl0w3r.hammered.cocktail.cocktailDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.fl0w3r.hammered.Constants
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.cocktail.CocktailData
import com.fl0w3r.hammered.databinding.ActivitySlidesBinding

class SlidesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySlidesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_slides)

        val cocktailData = intent.getParcelableExtra<CocktailData>(Constants.COCKTAIL_DATA)!!

        val adapter = SlidesAdapter(cocktailData.steps.split("\n"))
        binding.slidesPager.adapter = adapter

    }
}