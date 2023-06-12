package jg.coursework.customheroesapp.data.dto.OrderDTO

import jg.coursework.customheroesapp.data.dto.CatalogDTO.FigureDTO

data class OrderItemDTO(
    val count: Int,
    val order: OrderDTO,
    val figure: FigureDTO
)
