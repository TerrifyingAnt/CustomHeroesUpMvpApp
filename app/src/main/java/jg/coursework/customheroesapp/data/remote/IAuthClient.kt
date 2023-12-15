package jg.coursework.customheroesapp.data.remote

import jg.coursework.customheroesapp.data.dto.AuthResponseDTO
import jg.coursework.customheroesapp.data.dto.LoginDTO.LoginRequestDTO
import jg.coursework.customheroesapp.data.dto.RegisterDTO.RegisterRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IAuthClient {

    @POST("/api/auth/login")
    suspend fun login(@Body loginRequestDTO: LoginRequestDTO): Response<AuthResponseDTO>

    @POST("/api/auth/register")
    suspend fun register(@Body registerRequestDTO: RegisterRequestDTO): Response<AuthResponseDTO>
}