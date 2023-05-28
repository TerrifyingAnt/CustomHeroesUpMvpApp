package jg.coursework.customheroesapp.domain.repository

import jg.coursework.customheroesapp.data.dto.LoginDTO.LoginRequestDTO
import jg.coursework.customheroesapp.data.dto.AuthResponseDTO
import jg.coursework.customheroesapp.data.dto.CatalogDTO.CatalogDTO
import jg.coursework.customheroesapp.data.dto.RegisterDTO.RegisterRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface CustomHeroesRepository {

    @POST("/api/auth/login")
    suspend fun login(@Body loginRequestDTO: LoginRequestDTO): Response<AuthResponseDTO>

    @POST("/api/auth/register")
    suspend fun register(@Body registerRequestDTO: RegisterRequestDTO): Response<AuthResponseDTO>

    @GET("/test/catalog")
    suspend fun getCatalog(@Header("Authorization") token: String): Response<List<CatalogDTO>>
}