package com.example.hammered.settings

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.hammered.Constants
import com.example.hammered.R
import com.example.hammered.databinding.SettingsFragmentBinding
import com.example.hammered.dialog.StartupChooseDialog
import com.google.android.material.snackbar.Snackbar


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

        val progressDialog =
            AlertDialog.Builder(requireActivity()).setView(R.layout.progress_dialog_layout).create()

        viewModel.currentStartupScreen.observe(viewLifecycleOwner) {
            binding.textCurrentScreen.text = Constants.ALL_ITEM_LIST[it]
            currentSelected = it
        }

        binding.startUpScreenChange.setOnClickListener {

            StartupChooseDialog(currentSelected) { index ->
                viewModel.changeStartupScreen(index)

            }.show(childFragmentManager, "Radio_choose_dialog")
        }

        binding.exportToJsonCard.setOnClickListener {
            viewModel.saveToJson()
        }

        viewModel.startJsonSave.observe(viewLifecycleOwner) {
            if (it == true) {
                progressDialog.show()
            }
        }

        viewModel.jsonSaveStatus.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.doneSave()
                viewModel.doneShowingMessages()
                progressDialog.cancel()

                val toastMessage = when (it) {
                    Constants.DATA_SAVE_SUCCESS -> "Success! Saved data in downloads folder."
                    else -> "Failed to save data!"
                }

                Snackbar.make(binding.exportToJsonCard, toastMessage, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.secondaryLightColor))
                    .setTextColor(Color.BLACK)
                    .show()
            }
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