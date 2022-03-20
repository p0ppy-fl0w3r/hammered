package com.fl0w3r.hammered.recommendation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fl0w3r.hammered.database.CocktailDatabase
import com.fl0w3r.hammered.entities.Cocktail
import com.fl0w3r.hammered.entities.relations.CocktailWithIngredient
import com.fl0w3r.hammered.repository.CocktailRepository
import kotlinx.coroutines.launch
import timber.log.Timber


class HammerViewModel(application: Application) : AndroidViewModel(application) {

    private val _recommendationScore = MutableLiveData<MutableMap<Int, Float>>()
    val recommendationScore: LiveData<MutableMap<Int, Float>>
        get() = _recommendationScore

    private val _cocktailList = MutableLiveData<List<CocktailWithIngredient>>()
    val cocktailList: LiveData<List<CocktailWithIngredient>>
        get() = _cocktailList

    private val _isEmpty = MutableLiveData<Boolean?>()
    val isEmpty : LiveData<Boolean?>
        get() = _isEmpty

    private val repository = CocktailRepository(CocktailDatabase.getDatabase(application))

    fun getRecommendation() {
        viewModelScope.launch {
            val cocktailList = repository.getCocktailByCount()
            if (!cocktailList.isNullOrEmpty()) {
                val ingredientIdList =
                    repository.getDistinctIngredient(cocktailList.map { it.cocktail_id })

                val ratingIngredientMatrix = getRatingList(cocktailList, ingredientIdList, true)

                val sumScore = getSumScore(ratingIngredientMatrix)
                val sumRating: Float = if (sumScore.sum() == 0) 1f else sumScore.sum().toFloat()

                val weightedIngredientMap = mutableMapOf<Int, Float>()
                ingredientIdList.zip(sumScore) { ingId, score ->
                    weightedIngredientMap[ingId] = score / sumRating
                }

                val sampleData = repository.getALlCocktail()
                val rawUserProfile = getRatingList(sampleData, ingredientIdList, false)

                val userProfile = mutableMapOf<Int, Float>()
                rawUserProfile.zip(sampleData) { ingMatrix, cocktail ->
                    userProfile[cocktail.cocktail_id.toInt()] =
                        getWeightedRating(ingMatrix, weightedIngredientMap)
                }

                _recommendationScore.value = userProfile

            }
            else{
                _isEmpty.value = true
            }
        }
    }

    fun showRandomRecommendation() {
        viewModelScope.launch {
            _cocktailList.value = listOf(repository.getRandomCocktail())
        }
    }

    fun showRecommendation(cocktailId: MutableSet<Int>) {
        viewModelScope.launch {
            _cocktailList.value = repository.getCocktailWithIngredient(cocktailId.toList())
        }
    }

    private fun getSumScore(scoreList: List<List<Int>>): List<Int> {

        val sumList = mutableListOf<Int>()

        // Calculate the sum of elements in the y-axis of the matrix
        for (i in 0 until scoreList[0].size) {
            var sum = 0
            for (j in scoreList) {
                sum += j[i]
            }
            sumList.add(sum)
        }

        return sumList

    }

    private suspend fun getRatingList(
        sampleList: List<Cocktail>,
        ingredientList: List<Int>,
        isWeighted: Boolean
    ): List<List<Int>> {
        val ratingIngredientList = mutableListOf<List<Int>>()

        for (i in sampleList) {
            // Create a list of zeros('0') for all cocktails in sample data
            val containsIngredient: MutableList<Int> =
                (0..ingredientList.size).map { _ -> 0 }.toMutableList()

            ingredientList.mapIndexed { index, id ->

                // Check if the cocktail in question has the ingredient
                if (repository.hasIngredient(i.cocktail_id, id)) {
                    containsIngredient[index] = when (isWeighted) {
                        true -> i.makeCount
                        else -> 1
                    }
                }
            }
            ratingIngredientList.add(containsIngredient)
        }

        return ratingIngredientList
    }

    private fun getWeightedRating(
        ingredientMatrix: List<Int>,
        weightedIngredientMatrix: MutableMap<Int, Float>
    ): Float {
        var totalWeightedRating = 0f

        ingredientMatrix.zip(weightedIngredientMatrix.values) { ingredientValue, weightedValue ->
            if (ingredientValue > 0) {
                totalWeightedRating += weightedValue
            }
        }

        return totalWeightedRating
    }
}