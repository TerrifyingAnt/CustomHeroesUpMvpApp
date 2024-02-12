package jg.coursework.customheroesapp.data.repository.remote

import jg.coursework.customheroesapp.data.dto.User
import jg.coursework.customheroesapp.data.remote.IUserClient
import jg.coursework.customheroesapp.data.repository.BaseDataSource
import jg.coursework.customheroesapp.domain.repository.remote.IUserRepository
import jg.coursework.customheroesapp.util.Resource
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val customHeroesUserRepository: IUserClient
): BaseDataSource(), IUserRepository {

    override suspend fun getMe(): Resource<User> {
        return safeApiCall {customHeroesUserRepository.getMe() }
    }
}