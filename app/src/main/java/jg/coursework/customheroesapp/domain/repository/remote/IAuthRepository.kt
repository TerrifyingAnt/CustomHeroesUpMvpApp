package jg.coursework.customheroesapp.domain.repository.remote

import jg.coursework.customheroesapp.data.dto.AuthResponseDTO
import jg.coursework.customheroesapp.data.dto.LoginDTO.LoginRequestDTO
import jg.coursework.customheroesapp.data.dto.RegisterDTO.RegisterRequestDTO
import jg.coursework.customheroesapp.util.Resource

interface IAuthRepository {
    suspend fun login(loginRequestDTO: LoginRequestDTO): Resource<AuthResponseDTO>
    suspend fun register(registerRequestDTO: RegisterRequestDTO): Resource<AuthResponseDTO>
}