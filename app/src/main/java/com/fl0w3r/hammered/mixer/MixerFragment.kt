package com.fl0w3r.hammered.mixer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.databinding.DataBindingUtil
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.databinding.FragmentMixerBinding
import com.fl0w3r.hammered.ui.theme.HammeredComposeTheme


class MixerFragment : Fragment() {

    private lateinit var binding: FragmentMixerBinding

    @ExperimentalFoundationApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mixer, container, false)

        binding.composeView.setContent {
            HammeredComposeTheme {
                IngredientList()
            }
        }

        return binding.root
    }

    // TRIAL
    // TODO remove index
    @Composable
    private fun IngredientItems(index: Int) {
        // TODO pass ingredient item as parameter
        Row(
            modifier = Modifier
                .border(1.dp, Color(0xFF000000), shape = RectangleShape)
                .padding(4.dp)
        ) {
            Image(
                // TODO change to actual image
                // TRIAL placeholder image
                painter = painterResource(id = R.drawable.no_drinks),
                contentDescription = "Place holder image",
                modifier = Modifier.size(50.dp)
            )
            Text(
                text = "Ingredient #$index",
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterVertically),
                textAlign = TextAlign.Center,
            )
        }
    }

    @ExperimentalFoundationApi
    @Composable
    private fun IngredientList() {

        LazyVerticalGrid(cells = GridCells.Fixed(2)) {
            items(40) { index ->
                IngredientItems(index = index)
            }
        }
    }

    @ExperimentalFoundationApi
    @Preview(showBackground = true)
    @Composable
    fun MixerPreview() {
        HammeredComposeTheme {
            IngredientList()
        }
    }

}