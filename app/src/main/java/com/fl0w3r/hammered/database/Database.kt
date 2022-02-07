package com.fl0w3r.hammered.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fl0w3r.hammered.dao.CocktailDao
import com.fl0w3r.hammered.entities.Cocktail
import com.fl0w3r.hammered.entities.Ingredient
import com.fl0w3r.hammered.entities.relations.IngredientCocktailRef

@Database(
    entities = [Cocktail::class, Ingredient::class, IngredientCocktailRef::class],
    exportSchema = false,
    version = 10
)
abstract class CocktailDatabase : RoomDatabase() {

    abstract val cocktailDao: CocktailDao

    companion object {

        private var INSTANCE: CocktailDatabase? = null

        fun getDatabase(context:Context): CocktailDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CocktailDatabase::class.java,
                        "cocktail_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}