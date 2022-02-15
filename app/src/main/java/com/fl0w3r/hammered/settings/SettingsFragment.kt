package com.fl0w3r.hammered.settings

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fl0w3r.hammered.Constants
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.databinding.SettingsFragmentBinding
import com.fl0w3r.hammered.dialog.CancelAlertDialog
import com.fl0w3r.hammered.dialog.StartupChooseDialog
import com.fl0w3r.hammered.dialog.WarningDialog
import com.fl0w3r.hammered.entities.Cocktail
import com.fl0w3r.hammered.entities.Ingredient
import com.fl0w3r.hammered.entities.relations.IngredientCocktailRef
import com.fl0w3r.hammered.utils.JsonUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.settings_fragment.*
import org.apache.commons.io.IOUtils
import timber.log.Timber

class SettingsFragment : Fragment() {

    val viewModel: SettingsViewModel by lazy {
        ViewModelProvider(requireActivity())[SettingsViewModel::class.java]
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.saveToJson()
            } else {
                WarningDialog(R.layout.permission_warning_layout).show(
                    childFragmentManager,
                    "permission_denied_warning_dialog"
                )
            }
        }

    private lateinit var binding: SettingsFragmentBinding

    private var currentSelected: Int = 0

    private var activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val ignorePrevious = binding.replaceExistingSwitch.isChecked
                it.data?.data?.let { dirPath -> viewModel.getFromJson(dirPath, ignorePrevious) }
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

        val importProgressDialog =
            AlertDialog.Builder(requireActivity()).setView(R.layout.import_progress_dialog).create()


        // Click listeners
        binding.startUpScreenChange.setOnClickListener {

            StartupChooseDialog(currentSelected) { index ->
                viewModel.changeStartupScreen(index)

            }.show(childFragmentManager, "Radio_choose_dialog")
        }

        binding.exportToJsonCard.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                viewModel.saveToJson()
            } else {
                when {
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        viewModel.saveToJson()
                    }

                    shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                        CancelAlertDialog(
                            getString(R.string.required_permission_msg)
                        ) { requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE) }.show(
                            childFragmentManager,
                            "permission_required_dialog"
                        )
                    }
                    else -> {
                        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }
            }
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

        binding.mixerSettingLayout.setOnClickListener {
            binding.mixerOptionsRadio.visibility = when (binding.mixerOptionsRadio.visibility) {
                View.GONE -> View.VISIBLE
                else -> View.GONE
            }
        }

        binding.mixerOptionsRadio.setOnCheckedChangeListener { _, i ->
            when(i){
                binding.showCombinationRadio.id -> viewModel.changeMixerOption(Constants.SELECT_COMBINATION)
                else -> viewModel.changeMixerOption(Constants.SELECT_ALL)
            }
        }

        // LiveData observers

        viewModel.currentStartupScreen.observe(viewLifecycleOwner) {
            binding.textCurrentScreen.text = Constants.ALL_ITEM_LIST[it]
            currentSelected = it
        }

        viewModel.currentMixerOption.observe(viewLifecycleOwner){
            when(it){
                Constants.SELECT_ALL -> showAllRadio.isChecked = true
                else -> showCombinationRadio.isChecked = true
            }
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

                val snackMessage = when (it) {
                    Constants.DATA_SAVE_SUCCESS -> "Success! Saved data in downloads folder."
                    else -> "Failed to save data!"
                }
                showSnack(snackMessage)
            }
        }

        viewModel.startImport.observe(viewLifecycleOwner) {
            if (it == true) {
                importProgressDialog.show()
            }
        }

        viewModel.importStatus.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.doneImport()
                viewModel.doneImportMessages()
                importProgressDialog.cancel()

                val snackMessage = when (it) {
                    Constants.IMPORT_SUCCESS -> "Success! Imported everything."
                    Constants.FOLDER_INVALID -> "Looks like the import data was invalid!"
                    else -> "Failed to import data!"
                }
                showSnack(snackMessage)
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
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        if(intent.resolveActivity(requireContext().packageManager) != null){
            activityResultLauncher.launch(intent)
        }
        else{
            showSnack("Look like this feature is unavailable in your device!")
        }
    }

    private fun showSnack(msg: String){
        Snackbar.make(requireContext(), binding.root, msg, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.secondaryLightColor
                )
            )
            .setTextColor(Color.BLACK)
            .show()
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

    private fun reset() {
        CancelAlertDialog("Are you sure you want to reset the app into it's initial state?") {

            val ingredientList = resources.openRawResource(R.raw.ingredient).use { ingRaw ->
                JsonUtils.getClassFromJson<Ingredient>(IOUtils.toString(ingRaw))
            }
            val cocktailList = resources.openRawResource(R.raw.cocktail).use { cocktailRaw ->
                JsonUtils.getClassFromJson<Cocktail>(IOUtils.toString(cocktailRaw))
            }
            val referenceList = resources.openRawResource(R.raw.ref).use { refRaw ->
                JsonUtils.getClassFromJson<IngredientCocktailRef>(IOUtils.toString(refRaw))
            }

            if (ingredientList != null && cocktailList != null && referenceList != null) {
                viewModel.resetApp(ingredientList, cocktailList,referenceList )
                Toast.makeText(requireContext(), "Reset completed!!", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(), "Something went wrong. Please re-install the app to use this feature.", Toast.LENGTH_SHORT).show()
                Timber.e("Some of the values were null. Check the deserializer or Json files.")
            }

        }.show(
            childFragmentManager,
            "reset_app_dialog"
        )
    }
}