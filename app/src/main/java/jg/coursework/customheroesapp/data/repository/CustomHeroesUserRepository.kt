package jg.coursework.customheroesapp.data.repository

import jg.coursework.customheroesapp.data.dto.User
import jg.coursework.customheroesapp.data.remote.CustomHeroesUserClient
import jg.coursework.customheroesapp.util.Resource
import javax.inject.Inject

class CustomHeroesUserRepository @Inject constructor(
    private val customHeroesUserRepository: CustomHeroesUserClient
): BaseDataSource() {

    suspend fun getMe(): Resource<User> {
        return safeApiCall {customHeroesUserRepository.getMe() }
    }
}