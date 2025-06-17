package io.github.kristoffer_consid.pokedex.data.pokemon.impl

import androidx.room.Dao
import io.github.kristoffer_consid.pokedex.data.AppDao
import io.github.kristoffer_consid.pokedex.data.pokemon.FavoritesRepository
import javax.inject.Inject

@Dao
class LocalFavoritesRepository @Inject constructor(private val appDao: AppDao) : FavoritesRepository {
    override suspend fun getFavorites() = Result.success(appDao.getFavorites())

    override suspend fun toggleFavorite(id: Int): Result<List<Int>> {
        var rowsAffected = if (appDao.getFavorite(id) != null) {
            appDao.removeFavorite(id)
        } else {
            appDao.addFavorite(id).toInt()
        }

        if (rowsAffected > 0) {
            return getFavorites()
        }

        return Result.failure(Exception("Favorite could not be toggled with id: $id"))
    }

}