package jg.coursework.customheroesapp.data.remote

import android.content.SharedPreferences
import jg.coursework.customheroesapp.data.dto.LoginDTO.LoginRequestDTO
import jg.coursework.customheroesapp.data.dto.AuthResponseDTO
import jg.coursework.customheroesapp.data.dto.CatalogDTO.CatalogDTO
import jg.coursework.customheroesapp.data.dto.OrderDTO.OrderDTO
import jg.coursework.customheroesapp.data.dto.OrderDTO.OrderItemDTO
import jg.coursework.customheroesapp.data.dto.RegisterDTO.RegisterRequestDTO
import jg.coursework.customheroesapp.data.dto.User
import jg.coursework.customheroesapp.domain.repository.CustomHeroesRepository
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class CustomHeroesRepositoryImpl @Inject constructor(
    private val retrofit: Retrofit,
    private val sharedPreferences: SharedPreferences) : CustomHeroesRepository,
    BaseDataSource() {

    private val customHeroesRepository: CustomHeroesRepository = retrofit.create(CustomHeroesRepository::class.java)

    override suspend fun login(loginRequestDTO: LoginRequestDTO): Response<AuthResponseDTO> {
        return customHeroesRepository.login(loginRequestDTO)
    }

    override suspend fun register(registerRequestDTO: RegisterRequestDTO): Response<AuthResponseDTO> {
        return customHeroesRepository.register(registerRequestDTO)
    }

    override suspend fun getCatalog(token: String): Response<List<CatalogDTO>> {
        println(sharedPreferences.getString("token", ""))
        return customHeroesRepository.getCatalog("Bearer " + sharedPreferences.getString("token", "")!!)
    }

    override suspend fun getFigureById(id: Int, token: String): Response<List<CatalogDTO>> {
        return customHeroesRepository.getFigureById(id, "Bearer " + sharedPreferences.getString("token", "")!!)
    }

    override suspend fun getMe(token: String): Response<User> {
       return customHeroesRepository.getMe("Bearer " + sharedPreferences.getString("token", "")!!)
    }

    override suspend fun createOrder(order: String, token: String) {
        return customHeroesRepository.createOrder(sharedPreferences.getString("basket", "")!!, "Bearer " + sharedPreferences.getString("token", "")!!)
    }

    override suspend fun getOrders(token: String): Response<List<OrderItemDTO>> {
        return customHeroesRepository.getOrders("Bearer " + sharedPreferences.getString("token", "")!!)
    }
}