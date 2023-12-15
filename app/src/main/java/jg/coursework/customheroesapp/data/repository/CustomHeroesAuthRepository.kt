package jg.coursework.customheroesapp.data.repository

import jg.coursework.customheroesapp.data.dto.AuthResponseDTO
import jg.coursework.customheroesapp.data.dto.LoginDTO.LoginRequestDTO
import jg.coursework.customheroesapp.data.dto.RegisterDTO.RegisterRequestDTO
import jg.coursework.customheroesapp.data.remote.CustomHeroesAuthClient
import jg.coursework.customheroesapp.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomHeroesAuthRepository @Inject constructor(
    private val customHeroesAuthClient: CustomHeroesAuthClient
) : BaseDataSource() {

    suspend fun login(loginRequestDTO: LoginRequestDTO): Resource<AuthResponseDTO> {
        return safeApiCall {customHeroesAuthClient.login(loginRequestDTO) }
    }

    suspend fun register(registerRequestDTO: RegisterRequestDTO): Resource<AuthResponseDTO> {
        return safeApiCall {customHeroesAuthClient.register(registerRequestDTO)}
    }
}