package jg.coursework.customheroesapp.domain.repository

import jg.coursework.customheroesapp.data.dto.User
import jg.coursework.customheroesapp.util.Resource

interface IUserRepository {
    suspend fun getMe(): Resource<User>
}