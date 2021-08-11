package com.example.hammered.settings

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.hammered.Constants
import com.example.hammered.R
import com.example.hammered.databinding.SettingsFragmentBinding
import com.example.hammered.dialog.CancelAlertDialog
import com.example.hammered.dialog.StartupChooseDialog
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.io.File

class SettingsFragment : Fragment() {

    val viewModel: SettingsViewModel by lazy {
        ViewModelProvider(requireActivity()).get(SettingsViewModel::class.java)
    }

    private lateinit var binding: SettingsFragmentBinding

    private var currentSelected: Int = 0
    private var importDataDir = ""

    private var activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                // TODO export file
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
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

        // Click listeners
        binding.startUpScreenChange.setOnClickListener {

            StartupChooseDialog(currentSelected) { index ->
                viewModel.changeStartupScreen(index)

            }.show(childFragmentManager, "Radio_choose_dialog")
        }

        binding.exportToJsonCard.setOnClickListener {
            viewModel.saveToJson()
        }

        binding.importFromJson.setOnClickListener {
            binding.switchLayout.visibility = when (binding.switchLayout.visibility) {
                View.GONE -> View.VISIBLE
                else -> View.GONE
            }
        }

        binding.importButton.setOnClickListener {
            importFromJson()
        }

        binding.deleteAllCard.setOnClickListener {
            deleteAll()
        }

        binding.resetCard.setOnClickListener {
            reset()
        }

        // LiveData observers
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
                    .setBackgroundTint(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.secondaryLightColor
                        )
                    )
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

    private fun importFromJson() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        activityResultLauncher.launch(intent)
    }

    private fun deleteAll() {
        CancelAlertDialog("Are you sure you want to delete everything?") {
            viewModel.deleteEverything()
            Toast.makeText(requireContext(), "Deleted everything!!", Toast.LENGTH_SHORT).show()
        }.show(
            childFragmentManager,
            "delete_all_dialog"
        )
    }

    private fun reset(){

        CancelAlertDialog("Are you sure you want to reset the app into it's initial state?") {
            viewModel.resetApp()
            Toast.makeText(requireContext(), "Reset completed!!", Toast.LENGTH_SHORT).show()
        }.show(
            childFragmentManager,
            "reset_app_dialog"
        )
    }
}