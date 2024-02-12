package jg.coursework.customheroesapp.domain.repository.remote

import jg.coursework.customheroesapp.data.dto.OrderDTO.OrderItemDTO
import jg.coursework.customheroesapp.domain.model.BasketItemModel
import jg.coursework.customheroesapp.util.Resource

interface IOrderRepository {

    suspend fun createOrder(currentBasket: List<BasketItemModel>)

    suspend fun getOrders(): Resource<List<OrderItemDTO>>
}