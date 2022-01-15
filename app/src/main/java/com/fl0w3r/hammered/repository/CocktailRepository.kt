package com.fl0w3r.hammered.repository

import android.content.res.AssetManager
import com.fl0w3r.hammered.database.CocktailDatabase
import com.fl0w3r.hammered.entities.Cocktail
import com.fl0w3r.hammered.entities.Ingredient
import com.fl0w3r.hammered.entities.relations.CocktailWithIngredient
import com.fl0w3r.hammered.entities.relations.IngredientCocktailRef
import com.fl0w3r.hammered.entities.relations.IngredientWithCocktail
import timber.log.Timber

class CocktailRepository(private val database: CocktailDatabase) {

    suspend fun insertInitialValues(ingredients: List<Ingredient>,cocktails: List<Cocktail>, references: List<IngredientCocktailRef>){
        if (database.cocktailDao.getIngredientCocktailRefCount() < 1){
            ingredients.map {
                database.cocktailDao.insertIngredient(it)
            }

            cocktails.map {
                database.cocktailDao.insertCocktail(it)
            }

            references.map {
                database.cocktailDao.insertIngredientCocktailRef(it)
            }
        }
    }

    suspend fun insertIngredient(ingredient: Ingredient) {
        database.cocktailDao.insertIngredient(ingredient)
    }

    suspend fun insertCocktail(cocktail: Cocktail) {
        database.cocktailDao.insertCocktail(cocktail)
    }

    suspend fun ignoreInsertIngredient(ingredient: Ingredient) {
        database.cocktailDao.ignoreAndInsertIngredient(ingredient)
    }

    suspend fun ignoreInsertCocktail(cocktail: Cocktail) {
        database.cocktailDao.ignoreAndInsertCocktail(cocktail)
    }

    suspend fun insertIngredientCocktailRef(ingredientCocktailRef: IngredientCocktailRef) {
        database.cocktailDao.insertIngredientCocktailRef(ingredientCocktailRef)
    }

    suspend fun ignoreInsertIngredientCocktailRef(ingredientCocktailRef: IngredientCocktailRef) {
        database.cocktailDao.ignoreInsertIngredientCocktailRef(ingredientCocktailRef)
    }

    suspend fun getIngredient(id: Long): Ingredient? {
        return database.cocktailDao.getIngredient(id)
    }

    suspend fun getIngredient(name: String): Ingredient? {
        return database.cocktailDao.getIngredient(name)
    }

    suspend fun getIngredientByName(name: String): List<IngredientWithCocktail> {
        return database.cocktailDao.getIngredientFromName(name)
    }

    suspend fun getALlCocktail(): List<Cocktail> {
        return database.cocktailDao.getAllCocktail()
    }

    suspend fun getAllIngredientCocktailRef(): List<IngredientCocktailRef> {
        return database.cocktailDao.getAllIngredientCocktailRef()
    }

    suspend fun getCocktailByName(name: String): List<CocktailWithIngredient> {
        return database.cocktailDao.getCocktailFromName(name)
    }

    suspend fun getCocktail(id: Long): Cocktail? {
        return database.cocktailDao.getCocktail(id)
    }

    suspend fun getCocktailWithIngredient(id: Long): CocktailWithIngredient {
        return database.cocktailDao.getIngredientFromCocktail(id)
    }

    suspend fun getAllCocktailWithIngredient():List<CocktailWithIngredient> {
        return database.cocktailDao.getIngredientFromCocktail()
    }

    suspend fun getAllIngredient(): List<Ingredient> {
        return database.cocktailDao.getAllIngredient()
    }

    suspend fun getLastCocktailId(): Long {
        return database.cocktailDao.getLastCocktailId()
    }

    suspend fun getLastIngredientId(): Long {
        return database.cocktailDao.getLastIngredientId()
    }

    suspend fun getRefFromCocktail(id: Long): List<IngredientCocktailRef> {
        return database.cocktailDao.getRefFromCocktail(id)
    }

    suspend fun getRefFromIngredient(name: String): List<IngredientCocktailRef> {
        return database.cocktailDao.getRefFromIngredient(name)
    }

    suspend fun getRefFromIngredientId(id: Long): List<IngredientCocktailRef> {
        return database.cocktailDao.getRefFromIngredientId(id)
    }

    suspend fun getRefFromIngredientId(ids: List<Long>): List<IngredientCocktailRef> {
        return database.cocktailDao.getRefFromIngredientId(ids)
    }

    suspend fun updateCocktail(cocktail: Cocktail) {
        database.cocktailDao.updateCocktail(cocktail)
    }

    suspend fun updateIngredient(ingredient: Ingredient) {
        database.cocktailDao.updateIngredient(ingredient)
    }

    suspend fun getAllCocktailsFromIngredient(): List<IngredientWithCocktail> {
        return database.cocktailDao.getAllCocktailsFromIngredient()
    }

    suspend fun getInStockCocktailsFromIngredient(): List<IngredientWithCocktail> {
        return database.cocktailDao.getInStockCocktailsFromIngredient()
    }

    suspend fun getInCartCocktailsFromIngredient(): List<IngredientWithCocktail> {
        return database.cocktailDao.getInCartCocktailsFromIngredient()
    }

    suspend fun deleteAllRefOfCocktail(id: Long) {
        database.cocktailDao.deleteAllRefOfCocktail(id)
    }

    suspend fun deleteAllRefOfIngredient(id: Long) {
        database.cocktailDao.deleteAllRefOfIngredient(id)
    }

    suspend fun deleteCocktail(cocktail: Cocktail) {
        database.cocktailDao.deleteCocktail(cocktail)
    }

    suspend fun deleteIngredient(ingredient: Ingredient) {
        database.cocktailDao.deleteIngredient(ingredient)
    }

    suspend fun deleteAllCocktail() {
        database.cocktailDao.deleteAllCocktail()
    }

    suspend fun deleteAllIngredient() {
        database.cocktailDao.deleteAllIngredient()
    }

    suspend fun deleteAllRef() {
        database.cocktailDao.deleteAllRef()
    }

}