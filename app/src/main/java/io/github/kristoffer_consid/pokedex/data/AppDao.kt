package io.github.kristoffer_consid.pokedex.data

import androidx.room.Dao
import androidx.room.Query


@Dao
abstract class AppDao {
    @Query("SELECT id FROM Favorites")
    abstract suspend fun getFavorites(): List<Int> // Returns list of ids

    @Query("SELECT id FROM Favorites WHERE Id = :id")
    abstract suspend fun getFavorite(id: Int): Int? // Returns id if found

    @Query("INSERT INTO Favorites (id) VALUES (:id)")
    abstract suspend fun addFavorite(id: Int): Long // The row id of the affected row

    @Query("DELETE FROM Favorites WHERE Id = :id")
    abstract suspend fun removeFavorite(id: Int): Int // Returns amount of rows affected
}