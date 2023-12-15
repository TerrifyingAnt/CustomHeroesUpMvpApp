package jg.coursework.customheroesapp.data.remote

import jg.coursework.customheroesapp.data.dto.OrderDTO.OrderItemDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IOrderClient {
    @POST("/test/createOrder")
    suspend fun createOrder(@Body order: String)

    @POST("/test/getOrders")
    suspend fun getOrders(): Response<List<OrderItemDTO>>
}