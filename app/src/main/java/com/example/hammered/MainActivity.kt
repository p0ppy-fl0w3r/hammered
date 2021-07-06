package com.example.hammered

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodInfo
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.hammered.cocktail.createCocktail.CreateCocktailActivity
import com.example.hammered.databinding.ActivityMainBinding
import com.example.hammered.entities.Ingredient
import com.example.hammered.entities.relations.CocktailWithIngredient
import com.example.hammered.entities.relations.IngredientWithCocktail
import com.example.hammered.ingredients.createIngredient.CreateIngredientActivity
import com.example.hammered.search.SearchAdapter
import com.example.hammered.search.SearchItemClickListener
import com.example.hammered.search.SearchViewModel
import timber.log.Timber


// FIXME pressing back on settings navigates to ingredient when setting was navigated from cocktail
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


        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)

//        supportActionBar!!.setDisplayUseLogoEnabled(true)
//        supportActionBar!!.setLogo(R.drawable.hammered)

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

            else -> {
                Timber.e("Options item selected: ${item.title}")
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

}