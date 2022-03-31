package com.fl0w3r.hammered

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
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
import com.bumptech.glide.Glide
import com.fl0w3r.hammered.Constants.AVAILABLE_COCKTAIL_ITEM
import com.fl0w3r.hammered.Constants.BUNDLE_STARTUP_INT
import com.fl0w3r.hammered.Constants.FAVORITE_COCKTAIL_ITEM
import com.fl0w3r.hammered.Constants.NORMAL_COCKTAIL_ITEM
import com.fl0w3r.hammered.cocktail.createCocktail.CreateCocktailActivity
import com.fl0w3r.hammered.databinding.ActivityMainBinding
import com.fl0w3r.hammered.entities.relations.CocktailWithIngredient
import com.fl0w3r.hammered.entities.relations.IngredientWithCocktail
import com.fl0w3r.hammered.ingredients.createIngredient.CreateIngredientActivity
import com.fl0w3r.hammered.search.SearchAdapter
import com.fl0w3r.hammered.search.SearchItemClickListener
import com.fl0w3r.hammered.search.SearchViewModel
import timber.log.Timber


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener,
    View.OnFocusChangeListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchView: SearchView

    private var hasStarted = false

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this)[SearchViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 1
        )

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

        // Provides controls for MainActivity views for each fragments
        // TODO control the search icon visibility from here.
        // FIXME the toolbar is rendered before the fragment is visible.
        navController.addOnDestinationChangedListener() { controller, destination, _ ->

            when (destination.id) {

                controller.graph[R.id.ingredientFragment].id -> {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    toolbar.visibility = View.VISIBLE
                }

                controller.graph[R.id.cocktailFragment].id -> {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    toolbar.visibility = View.VISIBLE
                }
                controller.graph[R.id.settingsFragment].id -> {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    toolbar.visibility = View.VISIBLE
                }
                controller.graph[R.id.aboutFragment].id -> {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    toolbar.visibility = View.GONE
                }
                else -> {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    toolbar.visibility = View.VISIBLE
                }

            }

        }

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)

        setupActionBarWithNavController(this, navController, appBarConfiguration)
        binding.navigationView.setupWithNavController(navController)



       val headerImage = binding.navigationView.getHeaderView(0).findViewById<ImageView>(R.id.nav_header_image)

        Glide.with(this).asGif().load(R.drawable.toasting).into(headerImage)

        searchAdapter = SearchAdapter(SearchItemClickListener { selectedItem ->

            // TODO see if this can be improved.
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

        if (savedInstanceState != null) {
            hasStarted = savedInstanceState.getBoolean("hasStarted", false)
            Timber.e("Has Started: ${savedInstanceState.getBoolean("hasStarted")}")
        }

        binding.searchRecycler.adapter = searchAdapter

        viewModel.foundItems.observe(this) {
            searchAdapter.addFilterAndSubmitList(it)
        }

        if (!hasStarted) {
            navigateToStartup(startUpScreenId)
        }

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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("hasStarted", hasStarted)
        super.onSaveInstanceState(outState)
    }


    private fun navigateToStartup(id: Int) {

        hasStarted = true

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