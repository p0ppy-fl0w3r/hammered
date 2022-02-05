package com.fl0w3r.hammered.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fl0w3r.hammered.entities.Cocktail
import com.fl0w3r.hammered.entities.Ingredient
import com.fl0w3r.hammered.entities.relations.CocktailWithIngredient
import com.fl0w3r.hammered.entities.relations.IngredientCocktailRef
import com.fl0w3r.hammered.entities.relations.IngredientWithCocktail


@Dao
interface CocktailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCocktail(cocktail: Cocktail):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: Ingredient)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun ignoreAndInsertIngredient(ingredient: Ingredient)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun ignoreAndInsertCocktail(cocktail: Cocktail)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredientCocktailRef(ingredientCocktailRef: IngredientCocktailRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun ignoreInsertIngredientCocktailRef(ingredientCocktailRef: IngredientCocktailRef)

    @Update
    suspend fun updateIngredient(ingredient: Ingredient)

    @Update
    suspend fun updateCocktail(cocktail: Cocktail)

    @Delete
    suspend fun deleteCocktail(cocktail: Cocktail)

    @Delete
    suspend fun deleteIngredient(ingredient: Ingredient)

    @Query("DELETE FROM Cocktail")
    suspend fun deleteAllCocktail()

    @Query("DELETE FROM Ingredient")
    suspend fun deleteAllIngredient()

    @Query("DELETE FROM ingredientcocktailref")
    suspend fun deleteAllRef()

    @Query("SELECT * FROM Cocktail WHERE cocktail_id=:id")
    suspend fun getCocktail(id: Long): Cocktail?

    @Query("SELECT * FROM Cocktail")
    suspend fun getAllCocktail(): List<Cocktail>

    @Query("SElECT cocktail_id FROM Cocktail ORDER BY cocktail_id DESC LIMIT 1")
    suspend fun getLastCocktailId(): Long?

    @Query("SElECT ingredient_id FROM Ingredient ORDER BY ingredient_id DESC LIMIT 1")
    suspend fun getLastIngredientId(): Long?

    @Query("SELECT * FROM Cocktail WHERE cocktail_name LIKE :name")
    suspend fun getCocktailFromName(name: String): List<CocktailWithIngredient>

    @Query("SElECT * FROM Ingredient WHERE ingredient_name LIKE :name")
    suspend fun getIngredientFromName(name: String): List<IngredientWithCocktail>

    @Query("SELECT * FROM IngredientCocktailRef")
    suspend fun getAllIngredientCocktailRef(): List<IngredientCocktailRef>

    @Query("SELECT * FROM Ingredient")
    suspend fun getAllIngredient(): List<Ingredient>

    @Query("SELECT COUNT(*) FROM IngredientCocktailRef")
    suspend fun getIngredientCocktailRefCount(): Int

    @Query("SELECT * FROM Ingredient WHERE ingredient_id=:id")
    suspend fun getIngredient(id: Long): Ingredient?

    @Query("SELECT * FROM Ingredient WHERE ingredient_name=:name")
    suspend fun getIngredient(name: String): Ingredient?

    @Transaction
    @Query("SELECT * FROM IngredientCocktailRef WHERE cocktail_id=:cocktail_id")
    suspend fun getRefFromCocktail(cocktail_id: Long): List<IngredientCocktailRef>

    @Transaction
    @Query("SELECT * FROM IngredientCocktailRef WHERE ingredient_name=:name")
    suspend fun getRefFromIngredient(name: String): List<IngredientCocktailRef>

    @Transaction
    @Query("SELECT * FROM IngredientCocktailRef WHERE ingredient_id=:id")
    suspend fun getRefFromIngredientId(id: Long): List<IngredientCocktailRef>

    @Transaction
    @Query("SELECT * FROM IngredientCocktailRef WHERE ingredient_id IN (:id)")
    suspend fun getRefFromIngredientId(id: List<Long>): List<IngredientCocktailRef>

    @Transaction
    @Query("SELECT * FROM cocktail WHERE cocktail_id = :cocktail_id")
    suspend fun getIngredientFromCocktail(cocktail_id: Long): CocktailWithIngredient

    @Transaction
    @Query("SELECT * FROM cocktail")
    suspend fun getAllIngredientFromCocktail(): List<CocktailWithIngredient>

    @Transaction
    @Query("SELECT * FROM cocktail")
    fun getLiveIngredientFromCocktail(): LiveData<List<CocktailWithIngredient>?>

    @Transaction
    @Query("SELECT * FROM cocktail")
    suspend fun getIngredientFromCocktail(): List<CocktailWithIngredient>

    @Transaction
    @Query("SELECT * FROM cocktail where isFavorite=1")
    suspend fun getFavouriteIngredientWithCocktail(): List<CocktailWithIngredient>

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

    @Query("DELETE FROM ingredientcocktailref WHERE cocktail_id = :id")
    suspend fun deleteAllRefOfCocktail(id: Long)

    @Query("DELETE FROM ingredientcocktailref WHERE ingredient_id = :id")
    suspend fun deleteAllRefOfIngredient(id: Long)

}