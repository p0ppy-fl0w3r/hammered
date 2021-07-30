package com.example.hammered.settings

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.hammered.Constants
import com.example.hammered.R
import com.example.hammered.databinding.SettingsFragmentBinding
import com.example.hammered.dialog.StartupChooseDialog
import timber.log.Timber
import kotlin.properties.Delegates


class SettingsFragment : Fragment() {

    val viewModel: SettingsViewModel by lazy {
        ViewModelProvider(requireActivity()).get(SettingsViewModel::class.java)
    }

    private var currentSelected: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<SettingsFragmentBinding>(
            inflater,
            R.layout.settings_fragment,
            container,
            false
        )

        viewModel.currentStartupScreen.observe(viewLifecycleOwner){
            binding.textCurrentScreen.text = Constants.ALL_ITEM_LIST[it]
            currentSelected = it
        }

        binding.startUpScreenChange.setOnClickListener {

            StartupChooseDialog(currentSelected){index ->
                viewModel.changeStartupScreen(index)

            }.show(childFragmentManager,"Radio_choose_dialog")
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }
}