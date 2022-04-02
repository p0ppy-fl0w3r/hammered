package com.fl0w3r.hammered.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.filters.SmallTest
import com.fl0w3r.hammered.database.CocktailDatabase
import com.fl0w3r.hammered.entities.Cocktail
import com.fl0w3r.hammered.entities.Ingredient
import com.fl0w3r.hammered.entities.relations.IngredientCocktailRef
import kotlinx.coroutines.runBlocking
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class CocktailDaoTest {

    private lateinit var database: CocktailDatabase
    private lateinit var dao: CocktailDao

    private val mCocktail =
        Cocktail(1, "Red drink", "Very good drink", "1. Drink it", true, 2, "Good image")

    private val mIngredient =
        Ingredient(1, "Egg", "Round egg", "Egg image", inStock = true, inCart = false)

    private val mCocktailRef = IngredientCocktailRef(
        mCocktail.cocktail_id,
        mIngredient.ingredient_id,
        mIngredient.ingredient_name,
        1f,
        "ML",
        isGarnish = false,
        isOptional = true
    )

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CocktailDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.cocktailDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertCocktail() {
        runBlocking {
            dao.insertCocktail(mCocktail)

            val selectedCocktail = dao.getCocktail(1)

            assertThat(selectedCocktail).isEqualTo(mCocktail)
        }
    }

    @Test
    fun insertIngredient() = runBlocking {
        dao.insertIngredient(mIngredient)
        val selectedIngredient = dao.getIngredient(1)
        assertThat(selectedIngredient).isEqualTo(mIngredient)
    }


    @Test
    fun ignoreInsertCocktail() = runBlocking {
        dao.insertCocktail(mCocktail)
        val newCocktail = mCocktail.copy(cocktail_name = "Apple Cocktail")
        dao.ignoreAndInsertCocktail(newCocktail)

        val selectedCocktail = dao.getCocktail(1)

        assertThat(selectedCocktail?.cocktail_name).isEqualTo(mCocktail.cocktail_name)

    }

    @Test
    fun ignoreInsertIngredient() = runBlocking {
        dao.insertIngredient(mIngredient)
        val newIngredient = mIngredient.copy(ingredient_name = "Banana")
        dao.ignoreAndInsertIngredient(newIngredient)

        val selectedIngredient = dao.getIngredient(1)

        assertThat(selectedIngredient?.ingredient_name).isEqualTo(mIngredient.ingredient_name)
    }

    @Test
    fun insertIngredientCocktailRef() = runBlocking {
        dao.insertIngredientCocktailRef(mCocktailRef)
        val selectedRefs = dao.getAllIngredientCocktailRef()
        assertThat(selectedRefs).contains(mCocktailRef)
    }

    @Test
    fun ignoreInsertCocktailRef() = runBlocking {
        dao.insertIngredientCocktailRef(mCocktailRef)
        val newIngredientCocktailRef = mCocktailRef.copy(quantity = 666f)
        dao.ignoreInsertIngredientCocktailRef(newIngredientCocktailRef)

        val selectedIngredientCocktailRef = dao.getAllIngredientCocktailRef()
            .first {
                it.ingredient_id == newIngredientCocktailRef.ingredient_id
                        && it.cocktail_id == newIngredientCocktailRef.cocktail_id
            }

        assertThat(selectedIngredientCocktailRef.quantity).isEqualTo(
            mCocktailRef.quantity
        )
    }

    @Test
    fun updateIngredient() = runBlocking {
        dao.insertIngredient(mIngredient)

        val updatedIngredient = mIngredient.copy(ingredient_name = "Apple")
        dao.updateIngredient(updatedIngredient)

        val selectedIngredient = dao.getIngredient(1)
        assertThat(selectedIngredient?.ingredient_name).isEqualTo("Apple")
    }

}