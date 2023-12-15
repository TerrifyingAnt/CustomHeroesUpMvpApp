package jg.coursework.customheroesapp.data.repository

import jg.coursework.customheroesapp.data.dto.OrderDTO.OrderItemDTO
import jg.coursework.customheroesapp.data.local.DataStoreManager
import jg.coursework.customheroesapp.data.remote.CustomHeroesOrderClient
import jg.coursework.customheroesapp.util.Resource
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomHeroesOrderRepository @Inject constructor(
    private val customHeroesOrderRepository: CustomHeroesOrderClient,
    private val dataStoreManager: DataStoreManager
) : BaseDataSource() {

    suspend fun createOrder()  {
        val order = dataStoreManager.getBasket().first()
        if(order != null) {
            return customHeroesOrderRepository.createOrder(order)
        }
    }

    suspend fun getOrders(): Resource<List<OrderItemDTO>> {
        return safeApiCall {customHeroesOrderRepository.getOrders()}
    }
}