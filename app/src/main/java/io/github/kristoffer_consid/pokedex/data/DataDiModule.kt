package io.github.kristoffer_consid.pokedex.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.kristoffer_consid.pokedex.data.pokemon.FavoritesRepository
import io.github.kristoffer_consid.pokedex.data.pokemon.impl.LocalFavoritesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataDiModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_db.sqlite").build()

    @Provides
    @Singleton
    fun provideDao(database: AppDatabase): AppDao = database.appDao()

    @Provides
    @Singleton
    fun provideFavoritesRepository(appDao: AppDao): FavoritesRepository =
        LocalFavoritesRepository(appDao)
}