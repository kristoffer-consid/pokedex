package io.github.kristoffer_consid.pokedex.data

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.kristoffer_consid.pokedex.data.pokemon.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}