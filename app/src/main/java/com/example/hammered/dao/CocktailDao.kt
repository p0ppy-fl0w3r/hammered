package com.example.hammered.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hammered.entities.Cocktail
import com.example.hammered.entities.Ingredient
import com.example.hammered.entities.relations.CocktailWithIngredient
import com.example.hammered.entities.relations.IngredientCocktailRef
import com.example.hammered.entities.relations.IngredientWithCocktail

@Dao
interface CocktailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCocktail(cocktail: Cocktail)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: Ingredient)

    @Update
    suspend fun updateIngredient(ingredient: Ingredient)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredientCocktailRef(ingredientCocktailRef: IngredientCocktailRef)

    @Query("SELECT * FROM Cocktail")
    suspend fun getAllCocktails(): List<Cocktail>

    @Query("SELECT * FROM Cocktail WHERE isFavorite=1")
    suspend fun getFavouriteCocktails(): List<Cocktail>

    @Query("SELECT COUNT(*) FROM IngredientCocktailRef")
    suspend fun getIngredientCocktailRefCount(): Int

    @Transaction
    @Query("SELECT * FROM IngredientCocktailRef")
    suspend fun getAllIngredientCocktailDetails(): List<IngredientCocktailRef>

    @Transaction
    @Query("SELECT * FROM cocktail WHERE cocktail_id = :cocktail_id")
    suspend fun getIngredientFromCocktail(cocktail_id: Long): List<CocktailWithIngredient>

    @Transaction
    @Query("SELECT * FROM cocktail")
    suspend fun getAllIngredientFromCocktail(): List<CocktailWithIngredient>

    @Transaction
    @Query("SELECT * FROM cocktail")
    fun getLiveIngredientFromCocktail(): LiveData<List<CocktailWithIngredient>?>

    @Transaction
    @Query("SELECT * FROM cocktail where isFavorite=1")
    suspend fun getFavouriteIngredientFromCocktail(): List<CocktailWithIngredient>

    @Transaction
    @Query("SELECT * FROM ingredient WHERE ingredient_name = :ingredient_name")
    suspend fun getCocktailsFromIngredient(ingredient_name: String): List<IngredientWithCocktail>

    @Transaction
    @Query("SELECT * FROM ingredient")
    suspend fun getAllCocktailsFromIngredient(): List<IngredientWithCocktail>

    @Transaction
    @Query("SELECT * FROM ingredient WHERE inStock=1")
    suspend fun getInStockCocktailsFromIngredient(): List<IngredientWithCocktail>

    @Transaction
    @Query("SELECT * FROM ingredient WHERE inCart=1")
    suspend fun getInCartCocktailsFromIngredient(): List<IngredientWithCocktail>

    @Transaction
    @Query("SELECT * FROM ingredient")
    fun getLiveCocktailsFromIngredients(): LiveData<List<IngredientWithCocktail>>

}