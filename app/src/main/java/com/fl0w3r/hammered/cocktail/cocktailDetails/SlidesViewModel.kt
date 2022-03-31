package com.fl0w3r.hammered.cocktail.cocktailDetails

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.fl0w3r.hammered.cocktail.CocktailData
import com.fl0w3r.hammered.database.CocktailDatabase
import com.fl0w3r.hammered.datastore.SettingsPreferences
import com.fl0w3r.hammered.entities.Ingredient
import com.fl0w3r.hammered.entities.relations.IngredientCocktailRef
import com.fl0w3r.hammered.repository.CocktailRepository
import com.fl0w3r.hammered.wrappers.RefItemWrapper
import com.fl0w3r.hammered.wrappers.SlideWrapper
import kotlinx.coroutines.launch
import org.vosk.android.StorageService
import timber.log.Timber
import org.vosk.Model

class SlidesViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = SettingsPreferences(application)
    private val repository = CocktailRepository(CocktailDatabase.getDatabase(application))

    private val _stepIngredients = MutableLiveData<List<SlideWrapper>>()
    val stepIngredient: LiveData<List<SlideWrapper>>
        get() = _stepIngredients


    private val _model = MutableLiveData<Model>();
    val model: LiveData<Model>
        get() = _model

    val asrStatus = preferences.asrStatus.asLiveData()


    fun getIngredients(cocktailData: CocktailData) {
        viewModelScope.launch {
            val mCocktailRef: List<IngredientCocktailRef> =
                repository.getRefFromCocktail(cocktailData.cocktail_id)

            val steps = cocktailData.steps.split("\n")

            val slideIngredients = mutableListOf<SlideWrapper>()

            for (step in steps) {
                val matchedIngredient = getIngInStep(step, mCocktailRef)
                val wrapperList = mutableListOf<RefItemWrapper<Ingredient>>()
                for (ing in matchedIngredient) {
                    wrapperList.add(
                        RefItemWrapper(
                            repository.getIngredient(ing.ingredient_id)!!,
                            ing
                        )
                    )
                }
                slideIngredients.add(SlideWrapper(step, wrapperList))
            }

            _stepIngredients.value = slideIngredients
        }

    }

    private fun getIngInStep(
        step: String,
        ingredients: List<IngredientCocktailRef>
    ): List<IngredientCocktailRef> {

        val matchIngredient = filterIngredient(step, ingredients)
        var replaceStep = step

        matchIngredient.sortByDescending { it.ingredient_name.length }
        val mIngredientList = mutableListOf<IngredientCocktailRef>()

        var holder = replaceStep
        for (i in matchIngredient) {
            replaceStep = replaceStep.replace(i.ingredient_name, "", ignoreCase = true)
            // The ingredient was a false positive if the ingredient was not removed from the step.
            if (holder != replaceStep) {
                holder = replaceStep
                mIngredientList.add(i)
            }
        }

        return mIngredientList

    }

    fun setModel(context: Context) {

        StorageService.unpack(context, "model-en-us", "model", {
            _model.value = it
        }, {
            Timber.e("Failed to load model: $it")
        })
    }

    private fun filterIngredient(
        step: String,
        ingredients: List<IngredientCocktailRef>
    ): MutableList<IngredientCocktailRef> {
        val foundIngredient = mutableListOf<IngredientCocktailRef>()
        for (ingredient in ingredients) {
            if (Regex("(?i)${ingredient.ingredient_name}").containsMatchIn(
                    step
                )
            ) {
                foundIngredient.add(ingredient)
            }
        }
        return foundIngredient
    }

    fun updateScore(cocktailId: Long) {
        viewModelScope.launch {
            repository.updateCocktailScore(cocktailId)
        }
    }

}