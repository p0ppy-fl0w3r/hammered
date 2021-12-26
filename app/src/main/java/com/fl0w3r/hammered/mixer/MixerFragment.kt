package com.fl0w3r.hammered.mixer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.databinding.FragmentMixerBinding


class MixerFragment : Fragment() {

    private lateinit var binding: FragmentMixerBinding

    private val viewModel: MixerViewModel by lazy { ViewModelProvider(this)[MixerViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mixer, container, false)

        val ingredientAdapter = MixerIngredientAdapter(
            MixerIngredientClickListener {
               viewModel.setIngredientState(it)
            }
        )

        binding.ingredientSelectRecycler.adapter = ingredientAdapter

        viewModel.ingredientList.observe(viewLifecycleOwner){
            if (it != null){
                ingredientAdapter.submitList(it)
            }
        }



        return binding.root
    }

}