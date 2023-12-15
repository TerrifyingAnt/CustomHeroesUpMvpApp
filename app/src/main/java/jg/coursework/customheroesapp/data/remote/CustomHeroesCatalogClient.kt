package jg.coursework.customheroesapp.data.remote

import jg.coursework.customheroesapp.data.dto.CatalogDTO.CatalogDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CustomHeroesCatalogClient {
    @GET("/test/catalog")
    suspend fun getCatalog(): Response<List<CatalogDTO>>

    @POST("/test/getFigure")
    suspend fun getFigureById(@Body id: Int): Response<List<CatalogDTO>>
}