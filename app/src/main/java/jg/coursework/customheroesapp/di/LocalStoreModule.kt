package jg.coursework.customheroesapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jg.coursework.customheroesapp.data.local.AppDatabase
import jg.coursework.customheroesapp.data.local.DataStoreManager
import jg.coursework.customheroesapp.data.local.TokenManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalStoreModule {
    @Singleton
    @Provides
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager =
        DataStoreManager(context)

    @Singleton
    @Provides
    fun provideTokenStoreManager(@ApplicationContext context: Context): TokenManager =
        TokenManager(context)

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "custom_heroes_app_db"
    ).build() // The reason we can construct a database for the repo

    @Singleton
    @Provides
    fun provideYourDao(db: AppDatabase) = db.basketDao() // The reason we can implement a Dao for the database

}