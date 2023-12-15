package jg.coursework.customheroesapp.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jg.coursework.customheroesapp.data.remote.AuthInterceptor
import jg.coursework.customheroesapp.data.remote.IAuthClient
import jg.coursework.customheroesapp.data.remote.ICatalogClient
import jg.coursework.customheroesapp.data.remote.IOrderClient
import jg.coursework.customheroesapp.data.remote.IUserClient
import jg.coursework.customheroesapp.util.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor) =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    @Provides
    @Singleton
    fun provideCustomHeroesClient(retrofit: Retrofit): IAuthClient =
        retrofit.create()

    @Provides
    @Singleton
    fun provideCustomHeroesCatalogClient(retrofit: Retrofit): ICatalogClient =
        retrofit.create()

    @Provides
    @Singleton
    fun provideCustomHeroesOrderClient(retrofit: Retrofit): IOrderClient =
        retrofit.create()

    @Provides
    @Singleton
    fun provideCustomHeroesUserClient(retrofit: Retrofit): IUserClient =
        retrofit.create()


}