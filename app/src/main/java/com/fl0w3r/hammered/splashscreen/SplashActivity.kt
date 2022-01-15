package com.fl0w3r.hammered.splashscreen

import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fl0w3r.hammered.Constants
import com.fl0w3r.hammered.Constants.STARTUP_SCREEN_ID
import com.fl0w3r.hammered.MainActivity
import com.fl0w3r.hammered.R
import com.fl0w3r.hammered.databinding.ActivitySplashBinding
import com.fl0w3r.hammered.entities.Cocktail
import com.fl0w3r.hammered.entities.Ingredient
import com.fl0w3r.hammered.entities.relations.IngredientCocktailRef
import com.fl0w3r.hammered.utils.JsonUtils
import org.apache.commons.io.IOUtils
import timber.log.Timber


class SplashActivity : AppCompatActivity() {

    private lateinit var viewModel: SplashViewModel

    private var defaultStartupScreen = Constants.NORMAL_COCKTAIL_ITEM
    private var isDataInserted = true

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

        }

        viewModel.isDataInserted.observe(this) {
            if (it == false) {

                val ingredientList = resources.openRawResource(R.raw.ingredient).use { ingRaw ->
                    JsonUtils.getClassFromJson<Ingredient>(IOUtils.toString(ingRaw))
                }
                val cocktailList = resources.openRawResource(R.raw.cocktail).use { cocktailRaw ->
                    JsonUtils.getClassFromJson<Cocktail>(IOUtils.toString(cocktailRaw))
                }
                val referenceList = resources.openRawResource(R.raw.ref).use { refRaw ->
                    JsonUtils.getClassFromJson<IngredientCocktailRef>(IOUtils.toString(refRaw))
                }

                // Only insert data in database after you've confirmed that the database was not populated before.

                if (ingredientList != null && cocktailList != null && referenceList != null) {
                    viewModel.insertValuesInDatabase(ingredientList, cocktailList, referenceList)
                }
                else{
                    Timber.e("Some of the values were null. Check the deserializer or Json files.")
                }

                viewModel.setDataInserted()

                isDataInserted = false
            } else {
                // Don't call endSplash if it has already been called in this observation.
                if (isDataInserted) {
                    endSplash()
                }
            }
        }

        // Only start the main activity when the database is populated
        viewModel.populateDatabase.observe(this) {
            if (it == true) {

                viewModel.donePopulating()
                endSplash()
            }
        }
    }

    private fun endSplash() {
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