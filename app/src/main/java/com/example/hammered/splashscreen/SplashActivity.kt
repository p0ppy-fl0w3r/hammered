package com.example.hammered.splashscreen

import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.hammered.MainActivity
import com.example.hammered.R
import com.example.hammered.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        val animatedVector = binding.cocktailGlass.drawable as AnimatedVectorDrawable
        animatedVector.start()

        // TODO start activity only when the database is populated
        ViewModelProvider(this).get(SplashViewModel::class.java)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            }, 2000
        )
    }
}