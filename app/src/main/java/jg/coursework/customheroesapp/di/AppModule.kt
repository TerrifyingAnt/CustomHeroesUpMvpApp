package jg.coursework.customheroesapp.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jg.coursework.customheroesapp.data.remote.CustomHeroesRepositoryImpl
import jg.coursework.customheroesapp.domain.repository.CustomHeroesRepository
import jg.coursework.customheroesapp.domain.use_case.ValidateLoginInputUseCase
import jg.coursework.customheroesapp.domain.use_case.ValidateRegisterInputUseCase
import jg.coursework.customheroesapp.util.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.Charset
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideValidateLoginInputUseCase(): ValidateLoginInputUseCase {
        return ValidateLoginInputUseCase()
    }

    @Provides
    @Singleton
    fun provideValidateRegisterInputUseCase(): ValidateRegisterInputUseCase {
        return ValidateRegisterInputUseCase()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .disableHtmlEscaping()
        .create()

    @Provides
    @Singleton
    fun provideCustomHeroesRepository(retrofit: Retrofit, sharedPreferences: SharedPreferences): CustomHeroesRepository {
        return CustomHeroesRepositoryImpl(retrofit, sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("custom_heroes_prefs", Context.MODE_PRIVATE)
    }

}