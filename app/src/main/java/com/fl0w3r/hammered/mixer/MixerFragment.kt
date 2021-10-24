package com.fl0w3r.hammered.mixer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.databinding.DataBindingUtil
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.databinding.FragmentMixerBinding
import com.fl0w3r.hammered.ui.theme.HammeredComposeTheme


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