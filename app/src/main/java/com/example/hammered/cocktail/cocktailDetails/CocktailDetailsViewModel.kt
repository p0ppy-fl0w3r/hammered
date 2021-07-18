package com.example.hammered.cocktail.cocktailDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hammered.database.CocktailDatabase
import com.example.hammered.entities.Cocktail
import com.example.hammered.ingredients.IngredientData
import com.example.hammered.repository.CocktailRepository
import com.example.hammered.wrappers.RefItemWrapper
import kotlinx.coroutines.launch
import timber.log.Timber

class CocktailDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CocktailRepository(CocktailDatabase.getDatabase(application))

    private val _ingredientRefLiveData = MutableLiveData<List<RefItemWrapper<IngredientData>>>()

    val ingredientRefLiveData: LiveData<List<RefItemWrapper<IngredientData>>>
        get() = _ingredientRefLiveData

    private val _currentCocktail = MutableLiveData<Cocktail?>()

    val currentCocktail: LiveData<Cocktail?>
        get() = _currentCocktail

    private val _cocktailDeleted = MutableLiveData<Boolean?>()

    val cocktailDeleted: LiveData<Boolean?>
        get() = _cocktailDeleted

    private val _copyCocktail = MutableLiveData<Cocktail?>()

    val copyCocktail: LiveData<Cocktail?>
        get() = _copyCocktail


    private val _editCocktail = MutableLiveData<Cocktail?>()

    val editCocktail: LiveData<Cocktail?>
        get() = _editCocktail

    fun setCocktail(cocktail: Cocktail) {

        _currentCocktail.value = cocktail

        viewModelScope.launch {
            _currentCocktail.value = repository.getCocktail(cocktail.cocktail_id)
        }
    }

    fun setIngredient(id: Long) {
        viewModelScope.launch {
            val allIngredientRef = mutableListOf<RefItemWrapper<IngredientData>>()
            val allRefFromCocktail = repository.getRefFromCocktail(id)

            for (ref in allRefFromCocktail) {
                val ingredient = repository.getIngredient(ref.ingredient_id)
                val ingredientRef = RefItemWrapper(ingredient!!.asIngredientData(), ref)
                allIngredientRef.add(ingredientRef)
            }
            _ingredientRefLiveData.value = allIngredientRef
        }
    }

    fun changeFavourite() {
        viewModelScope.launch {
            val cocktail = _currentCocktail.value
            cocktail!!.isFavorite = !cocktail.isFavorite
            repository.updateCocktail(cocktail)
            _currentCocktail.value = cocktail
        }
    }

    fun deleteCurrentCocktail() {

        viewModelScope.launch {
            currentCocktail.value?.let {
                repository.deleteCocktail(it)
                repository.deleteAllRefOfCocktail(it.cocktail_id)
                _cocktailDeleted.value = true
            }
        }

    }

    fun doneDeleting() {
        _cocktailDeleted.value = null
    }

    fun copyAndEdit(id: Long) {
        viewModelScope.launch {
            // The value might have been edited/changed by now, it's best if the latest value from the
            // database is used as a copy rather than using the value from _currentCocktail.
            _copyCocktail.value = repository.getCocktail(id)
        }
    }

    fun editCurrent(id:Long){
        viewModelScope.launch {
            _editCocktail.value  = repository.getCocktail(id)
        }
    }

    fun doneEdit(){
        _editCocktail.value = null
    }

    fun doneCopy() {
        _copyCocktail.value = null
    }
}