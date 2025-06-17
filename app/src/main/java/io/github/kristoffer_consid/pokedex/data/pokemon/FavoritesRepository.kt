package io.github.kristoffer_consid.pokedex.data.pokemon

import androidx.room.Entity
import androidx.room.PrimaryKey

interface FavoritesRepository {
    suspend fun getFavorites(): Result<List<Int>>
    suspend fun toggleFavorite(id: Int): Result<List<Int>>
}

@Entity("Favorites")
data class FavoriteEntity(
    @PrimaryKey val id: Int
)