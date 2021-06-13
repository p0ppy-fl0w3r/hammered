package com.example.hammered.cocktail.createCocktail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.hammered.R
import com.example.hammered.databinding.FragmentCreateCocktailBinding
import timber.log.Timber


class CreateCocktailFragment : Fragment() {

    private lateinit var binding: FragmentCreateCocktailBinding

    private val viewModel: CreateCocktailViewModel by lazy {
        ViewModelProvider(this).get(CreateCocktailViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_create_cocktail,
                container,
                false
            )

        val adapter = CreateCocktailAdapter(

            SpinnerItemSelectListener {
                Timber.e("Spinner item at $it was selected.")
            },
            ItemOnClickListener { itemNumber, _ ->
                Timber.e("The item number is $itemNumber")
            }
        )
        binding.ingRefRecycler.adapter = adapter

        binding.addIngredient.setOnClickListener {
            viewModel.addIngredient()
        }

        viewModel.ingredientList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            adapter.notifyItemInserted(it.size-1)

        }

        binding.addStep.setOnClickListener {
            Timber.e("The list is ${viewModel.ingredientList.value}")
        }

        return binding.root
    }


}