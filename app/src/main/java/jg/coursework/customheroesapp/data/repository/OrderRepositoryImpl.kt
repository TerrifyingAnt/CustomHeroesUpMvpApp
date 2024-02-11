package jg.coursework.customheroesapp.data.repository

import jg.coursework.customheroesapp.data.dto.OrderDTO.OrderItemDTO
import jg.coursework.customheroesapp.data.local.DataStoreManager
import jg.coursework.customheroesapp.data.remote.IOrderClient
import jg.coursework.customheroesapp.domain.repository.IOrderRepository
import jg.coursework.customheroesapp.util.Resource
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val customHeroesOrderRepository: IOrderClient,
    private val dataStoreManager: DataStoreManager
) : BaseDataSource(), IOrderRepository {

    // TODO - переделать, просто передавать сюда заказ объектом адекватным
    override suspend fun createOrder()  {
        val order = dataStoreManager.getBasket().first()
        if(order != null) {
            return customHeroesOrderRepository.createOrder(order)
        }
    }

    override suspend fun getOrders(): Resource<List<OrderItemDTO>> {
        return safeApiCall {customHeroesOrderRepository.getOrders()}
    }
}