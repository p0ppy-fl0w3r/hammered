package com.example.hammered

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.get
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.hammered.Constants.AVAILABLE_COCKTAIL_ITEM
import com.example.hammered.Constants.BUNDLE_STARTUP_INT
import com.example.hammered.Constants.FAVORITE_COCKTAIL_ITEM
import com.example.hammered.Constants.NORMAL_COCKTAIL_ITEM
import com.example.hammered.cocktail.createCocktail.CreateCocktailActivity
import com.example.hammered.databinding.ActivityMainBinding
import com.example.hammered.entities.relations.CocktailWithIngredient
import com.example.hammered.entities.relations.IngredientWithCocktail
import com.example.hammered.ingredients.createIngredient.CreateIngredientActivity
import com.example.hammered.search.SearchAdapter
import com.example.hammered.search.SearchItemClickListener
import com.example.hammered.search.SearchViewModel
import timber.log.Timber


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener,
    View.OnFocusChangeListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchView: SearchView

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startUpScreenId = intent.getIntExtra(Constants.STARTUP_SCREEN_ID, 0)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.navHostFragment)
        drawerLayout = binding.mainDrawer

        val toolbar = binding.toolbar


        // Setting up cocktailFragment as top level destination so that it shows the hamburger button-
        // instead of the back button.

        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(R.id.cocktailFragment, R.id.ingredientFragment),
            drawerLayout = drawerLayout
        )

        navController.addOnDestinationChangedListener() { controller, destination, _ ->
            if (destination.id == controller.graph[R.id.ingredientFragment].id
                || destination.id == controller.graph[R.id.cocktailFragment].id
            ) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

        }

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)

        setupActionBarWithNavController(this, navController, appBarConfiguration)
        binding.navigationView.setupWithNavController(navController)

        searchAdapter = SearchAdapter(SearchItemClickListener { selectedItem ->

            val mBundle = Bundle()
            when (selectedItem) {
                is IngredientWithCocktail -> {
                    mBundle.putParcelable("ingredient", selectedItem.ingredient.asIngredientData())
                    searchAdapter.submitList(null)
                    searchView.setQuery("", false)
                    navController.navigate(R.id.ingredientDetailsFragment, mBundle)
                }
                is CocktailWithIngredient -> {
                    mBundle.putParcelable("cocktail", selectedItem.cocktail.asData())
                    searchAdapter.submitList(null)
                    searchView.setQuery("", false)
                    navController.navigate(R.id.cocktailDetailFragment, mBundle)
                }
                else -> {
                    throw IllegalArgumentException("The selected item is invalid :$selectedItem")
                }
            }
        })
        binding.searchRecycler.adapter = searchAdapter

        viewModel.foundItems.observe(this) {
            searchAdapter.addFilterAndSubmitList(it)
        }

        navigateToStartup(startUpScreenId)

    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        searchView = menu.findItem(R.id.searchItem).actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
        searchView.setOnQueryTextFocusChangeListener(this)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.createCocktail -> {
                val intent = Intent(this, CreateCocktailActivity::class.java)
                startActivity(intent)
            }

            R.id.createIngredient -> {
                val intent = Intent(this, CreateIngredientActivity::class.java)
                startActivity(intent)
            }

            R.id.settingsOption -> {
                navController.navigate(R.id.settingsFragment)
            }

            R.id.aboutOption -> {
                navController.navigate(R.id.aboutFragment)
            }

            else -> {
                Timber.i("Options item selected: ${item.title}")
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchAdapter.submitList(null)
        if (!newText.isNullOrBlank()) {
            viewModel.searchItems(newText)
        }

        return true
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (v is SearchView && !hasFocus) {
            searchAdapter.submitList(null)
            v.setQuery("", false)
        }
    }

    private fun navigateToStartup(id: Int) {

        val mBundle = Bundle()
        mBundle.putInt(BUNDLE_STARTUP_INT, id)

        navController.navigate(
            when (id) {
                NORMAL_COCKTAIL_ITEM,
                AVAILABLE_COCKTAIL_ITEM,
                FAVORITE_COCKTAIL_ITEM -> R.id.cocktailFragment

                else -> R.id.ingredientFragment
            }, mBundle
        )
    }

}