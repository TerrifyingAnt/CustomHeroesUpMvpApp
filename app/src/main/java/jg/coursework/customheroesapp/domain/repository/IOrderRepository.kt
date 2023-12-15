package jg.coursework.customheroesapp.domain.repository

import jg.coursework.customheroesapp.data.dto.OrderDTO.OrderItemDTO
import jg.coursework.customheroesapp.util.Resource

interface IOrderRepository {

    suspend fun createOrder()

    suspend fun getOrders(): Resource<List<OrderItemDTO>>
}