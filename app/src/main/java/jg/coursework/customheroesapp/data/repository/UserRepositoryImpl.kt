package jg.coursework.customheroesapp.data.repository

import jg.coursework.customheroesapp.data.dto.User
import jg.coursework.customheroesapp.data.remote.IUserClient
import jg.coursework.customheroesapp.domain.repository.IUserRepository
import jg.coursework.customheroesapp.util.Resource
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val customHeroesUserRepository: IUserClient
): BaseDataSource(), IUserRepository {

    override suspend fun getMe(): Resource<User> {
        return safeApiCall {customHeroesUserRepository.getMe() }
    }
}