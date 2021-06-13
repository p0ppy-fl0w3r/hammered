package com.example.hammered.cocktail.createCocktail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import timber.log.Timber

class CreateCocktailViewModel(application: Application) : AndroidViewModel(application) {

    private val _ingredientList = MutableLiveData<MutableList<NewCocktailRef>>()

    val ingredientList: LiveData<MutableList<NewCocktailRef>>
        get() = _ingredientList

    init {

        val newList = mutableListOf(NewCocktailRef())

        _ingredientList.value = newList
    }

    fun addIngredient() {
        try {
            val lastId = _ingredientList.value!!.last().ref_number
            val newIngredient = NewCocktailRef(ref_number = lastId.plus(1))
            val newList = _ingredientList.value!!
            newList.add(newIngredient)
            _ingredientList.value = newList

        }
        catch (e: NullPointerException) {
            _ingredientList.value = mutableListOf(
                NewCocktailRef()
            )
        }
    }

    fun editItem(number: Int, newString: String) {
        try {
            val currentItem = getItemByNumber(number)
            Timber.e("The item is $currentItem")
            val position = _ingredientList.value!!.indexOf(currentItem)
            currentItem!!.ingredient_name = newString
            _ingredientList.value!![position] = currentItem
        }
        catch (npe: NullPointerException) {
            Timber.e("No element in the list.")
        }
    }

    private fun getItemByNumber(ref_number: Int): NewCocktailRef? {
        val allValues = _ingredientList.value
        if (allValues != null) {
            for (items in allValues) {
                if (items.ref_number == ref_number) {
                    return items
                }
            }
        }
        return null
    }

//    fun changeUnitPosition(number: Int, pos: Int) {
//        try {
//            val currentItem = getItemByNumber(number)
//            Timber.e("The item is $currentItem")
//            val position = _ingredientList.value!!.indexOf(currentItem)
//            currentItem!!.ingredient_name = newString
//            _ingredientList.value!![position] = currentItem
//        }
//        catch (npe: NullPointerException) {
//            Timber.e("No element in the list.")
//        }
//    }

}