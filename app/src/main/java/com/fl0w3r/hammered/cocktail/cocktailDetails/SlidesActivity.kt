package com.fl0w3r.hammered.cocktail.cocktailDetails

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.fl0w3r.hammered.Constants
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.cocktail.CocktailData
import com.fl0w3r.hammered.databinding.ActivitySlidesBinding

import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.RecognitionListener
import org.vosk.android.SpeechService
import org.vosk.android.StorageService
import timber.log.Timber
import java.io.IOException
import java.lang.Exception

class SlidesActivity : AppCompatActivity(), RecognitionListener {

    private lateinit var binding: ActivitySlidesBinding

    private lateinit var viewPager: ViewPager2

    private val viewModel by lazy {
        ViewModelProvider(this)[SlidesViewModel::class.java]
    }

    private lateinit var speechService: SpeechService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_slides)

        val cocktailData = intent.getParcelableExtra<CocktailData>(Constants.COCKTAIL_DATA)!!

        val adapter = SlidesAdapter(SlidesAdapter.CloseClickListener {
            viewModel.updateScore(cocktailData.cocktail_id)
            finish()
        })

        viewModel.stepIngredient.observe(this) {
            if (it != null) {
                adapter.submitList(it)
                binding.slideProgress.visibility = View.GONE
            }
        }

        viewModel.model.observe(this) {
            recognizeCommands(it)
            viewModel.getIngredients(cocktailData)
        }

        viewPager = binding.slidesPager
        viewPager.adapter = adapter

        requestAudioPermissions()

    }

    // FIXME the model is too big and takes quite a while to load.
    private fun requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.RECORD_AUDIO),
                Constants.AUDIO_PERMISSION
            )
        } else {
            viewModel.setModel(this)
        }
    }


    private fun recognizeCommands(model: Model) {

        try {
            val recognizer = Recognizer(model, 16_000.0f)
            speechService = SpeechService(recognizer, 16_000.0f)
            speechService.startListening(this)

        } catch (e: IOException) {
            Timber.e("Failed to start speech service $e")
        }

    }

    private fun previousSlide() {
        viewPager.currentItem -= 1
        Toast.makeText(this, "Previous Step!", Toast.LENGTH_SHORT).show()
    }

    private fun nextSlide() {
        viewPager.currentItem += 1
        Toast.makeText(this, "Next Step!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::speechService.isInitialized) {
            speechService.stop()
            speechService.shutdown()
            Timber.e("The speech service was destroyed")
        }
    }

    // TODO add fallback for denied permission.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.AUDIO_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.setModel(this)
        }
    }

    private fun pauseSpeechService(paused: Boolean) {
        if (::speechService.isInitialized) {
            speechService.setPause(paused)
        }
    }

    override fun onPause() {
        super.onPause()

        // Pause the speech service to reduce battery usage.
        pauseSpeechService(true)
    }

    override fun onStart() {
        super.onStart()
        pauseSpeechService(false)
    }

    override fun onResume() {
        super.onResume()
        pauseSpeechService(false)
    }

    override fun onPartialResult(hypothesis: String?) {

    }

    override fun onResult(hypothesis: String?) {
        if (hypothesis?.contains("next") == true) {
            nextSlide()
        } else if (hypothesis?.contains("previous") == true) {
            previousSlide()
        }
        Timber.e("On Result: $hypothesis")
    }

    override fun onFinalResult(hypothesis: String?) {
        Timber.e("Final Result: $hypothesis")
    }

    override fun onError(exception: Exception?) {
        Timber.e("Got an error: $exception")
    }

    override fun onTimeout() {
        Timber.e("Timed Out!!")
    }
}