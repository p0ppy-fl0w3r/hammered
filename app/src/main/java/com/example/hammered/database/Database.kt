package com.example.hammered.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hammered.dao.CocktailDao
import com.example.hammered.entities.Cocktail
import com.example.hammered.entities.Ingredient
import com.example.hammered.entities.relations.IngredientCocktailRef

@Database(
    entities = [Cocktail::class, Ingredient::class, IngredientCocktailRef::class],
    exportSchema = false,
    version = 2
)
abstract class CocktailDatabase : RoomDatabase() {

    abstract val cocktailDao: CocktailDao

    companion object {

        private var INSTANCE: CocktailDatabase? = null

        fun getDatabase(application: Application): CocktailDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        application,
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