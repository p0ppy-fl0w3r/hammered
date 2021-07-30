package com.example.hammered.splashscreen

import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.hammered.Constants
import com.example.hammered.Constants.STARTUP_SCREEN_ID
import com.example.hammered.MainActivity
import com.example.hammered.R
import com.example.hammered.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {

    private lateinit var viewModel: SplashViewModel
    private var defaultStartupScreen = Constants.NORMAL_COCKTAIL_ITEM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        val animatedVector = binding.cocktailGlass.drawable as AnimatedVectorDrawable
        animatedVector.start()

        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        viewModel.currentSelectedStartupScreen.observe(this) {

            // Get the startup screen selected in settings so that the app will show the correct
            // startup screen.
            defaultStartupScreen = it

            // Start inserting data in database after you've gotten the startup screen.
            viewModel.insertValuesInDatabase()
        }

        // Only start the main activity when the database is populated
        viewModel.populateDatabase.observe(this){
            if (it == true){

                viewModel.donePopulating()

                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra(STARTUP_SCREEN_ID, defaultStartupScreen)
                        startActivity(intent)
                        finish()
                    }, 2000
                )
            }
        }
    }
}