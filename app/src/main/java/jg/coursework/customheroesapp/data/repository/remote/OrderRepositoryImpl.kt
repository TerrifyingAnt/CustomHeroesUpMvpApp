package jg.coursework.customheroesapp.data.repository.remote

import jg.coursework.customheroesapp.data.converters.toCringeStringFormat
import jg.coursework.customheroesapp.data.dto.OrderDTO.OrderItemDTO
import jg.coursework.customheroesapp.data.remote.IOrderClient
import jg.coursework.customheroesapp.data.repository.BaseDataSource
import jg.coursework.customheroesapp.domain.model.BasketItemModel
import jg.coursework.customheroesapp.domain.repository.remote.IOrderRepository
import jg.coursework.customheroesapp.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val customHeroesOrderRepository: IOrderClient,
) : BaseDataSource(), IOrderRepository {

    // TODO - переделать, просто передавать сюда заказ объектом адекватным
    override suspend fun createOrder(currentBasket: List<BasketItemModel>)  {
        val order = currentBasket.toCringeStringFormat()
        return customHeroesOrderRepository.createOrder(order)
    }

    override suspend fun getOrders(): Resource<List<OrderItemDTO>> {
        return safeApiCall {customHeroesOrderRepository.getOrders()}
    }
}