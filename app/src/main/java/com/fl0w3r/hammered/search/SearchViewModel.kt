package com.fl0w3r.hammered.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fl0w3r.hammered.database.CocktailDatabase
import com.fl0w3r.hammered.repository.CocktailRepository
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val _foundItems = MutableLiveData<List<Any>>()

    val foundItems : LiveData<List<Any>>
        get() = _foundItems

    private val repository = CocktailRepository(CocktailDatabase.getDatabase(application))


    fun searchItems(query: String){
        viewModelScope.launch {
            val foundList = mutableListOf<Any>()
            repository.getCocktailByName("%$query%").map {
                foundList.add(it)
            }
            repository.getIngredientByName("%$query%").map{
                foundList.add(it)
            }

            _foundItems.value = foundList
        }
    }

}