package com.example.hammered

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.hammered.cocktail.createCocktail.CreateCocktailActivity
import com.example.hammered.databinding.ActivityMainBinding
import com.example.hammered.ingredients.createIngredient.CreateIngredientActivity
import timber.log.Timber


// FIXME pressing back on settings navigates to ingredient when setting was navigated from cocktail

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.navHostFragment)
        drawerLayout = binding.mainDrawer

        val toolbar = binding.toolbar

        // Setting up cocktailFragment as top level destination so that it shows the hamburger button-
        // instead of the back button.
        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(R.id.cocktailFragment, R.id.ingredientFragment)
            ,drawerLayout = drawerLayout)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayUseLogoEnabled(true)

        setupActionBarWithNavController(this, navController, appBarConfiguration)
        binding.navigationView.setupWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.createCocktail -> {
                val intent = Intent(this, CreateCocktailActivity::class.java)
                startActivity(intent)
            }

            R.id.createIngredient ->{
                val intent = Intent(this, CreateIngredientActivity::class.java)
                startActivity(intent)
            }

            else ->{
                Timber.e("Options item selected: ${item.title}")
            }

        }

        return super.onOptionsItemSelected(item)
    }

}