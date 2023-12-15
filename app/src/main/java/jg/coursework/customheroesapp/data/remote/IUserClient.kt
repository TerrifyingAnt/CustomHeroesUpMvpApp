package jg.coursework.customheroesapp.data.remote

import jg.coursework.customheroesapp.data.dto.User
import retrofit2.Response
import retrofit2.http.POST

interface IUserClient {
    @POST("/api/auth/users/me")
    suspend fun getMe(): Response<User>
}