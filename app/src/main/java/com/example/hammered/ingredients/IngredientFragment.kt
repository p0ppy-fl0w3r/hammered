package com.example.hammered.ingredients

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hammered.R
import com.example.hammered.databinding.IngredientFragmentBinding
import com.example.hammered.entities.Ingredient


class IngredientFragment : Fragment() {

    private lateinit var viewModel: IngredientViewModel
    private lateinit var binding: IngredientFragmentBinding

    val ing1 = Ingredient("Lemon", "Nice", "lemon.png",false)
    val ing2 = Ingredient("Salt", "Very nice", "salt.png",false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.ingredient_fragment, container, false)

        val adapter = IngredientAdapter(IngredientClickListener {
            Toast.makeText(requireContext(), it.ingredient_description, Toast.LENGTH_SHORT).show()
        })
        adapter.submitList(listOf(ing1,ing2))

        binding.ingredientRecycler.adapter = adapter

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(IngredientViewModel::class.java)
        // TODO: Use the ViewModel
    }

}