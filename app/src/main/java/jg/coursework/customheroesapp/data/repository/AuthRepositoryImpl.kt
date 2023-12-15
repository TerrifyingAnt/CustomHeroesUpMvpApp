package jg.coursework.customheroesapp.data.repository

import jg.coursework.customheroesapp.data.dto.AuthResponseDTO
import jg.coursework.customheroesapp.data.dto.LoginDTO.LoginRequestDTO
import jg.coursework.customheroesapp.data.dto.RegisterDTO.RegisterRequestDTO
import jg.coursework.customheroesapp.data.remote.IAuthClient
import jg.coursework.customheroesapp.domain.repository.IAuthRepository
import jg.coursework.customheroesapp.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val IAuthClient: IAuthClient
) : BaseDataSource(), IAuthRepository {

    override suspend fun login(loginRequestDTO: LoginRequestDTO): Resource<AuthResponseDTO> {
        return safeApiCall {IAuthClient.login(loginRequestDTO) }
    }

    override suspend fun register(registerRequestDTO: RegisterRequestDTO): Resource<AuthResponseDTO> {
        return safeApiCall {IAuthClient.register(registerRequestDTO)}
    }
}