package com.example.hammered.mixer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.databinding.DataBindingUtil
import com.example.hammered.R
import com.example.hammered.databinding.FragmentMixerBinding
import com.example.hammered.ui.theme.HammeredComposeTheme


class MixerFragment : Fragment() {

    private lateinit var binding: FragmentMixerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mixer, container, false)

        binding.composeView.setContent {
            HammeredComposeTheme {
                CocktailItem()
            }
        }

        return binding.root
    }

    @Composable
    private fun CocktailItem(){
        Row() {
            Text(text = "Very nice drink!")
        }
    }
    
    @Preview(showBackground = true)
    @Composable
    fun MixerPreview(){
        HammeredComposeTheme {
            CocktailItem()
        }
    }

}