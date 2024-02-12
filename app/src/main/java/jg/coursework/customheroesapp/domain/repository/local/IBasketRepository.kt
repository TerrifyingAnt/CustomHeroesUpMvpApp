package jg.coursework.customheroesapp.domain.repository.local

import jg.coursework.customheroesapp.data.dto.CatalogDTO.CatalogDTO
import jg.coursework.customheroesapp.domain.model.BasketItemModel
import jg.coursework.customheroesapp.util.Resource

interface IBasketRepository {

    suspend fun getAllFromBasket(): List<BasketItemModel>

    suspend fun getItemFromBasketByModelId(modelId: Int): BasketItemModel?

    suspend fun updateCountInBasketByModelId(modelId: Int, count: Int)

    suspend fun insertBasketItemEntity(basketItemModel: BasketItemModel)

    suspend fun deleteBasketItemEntityByModelId(modelId: Int)

    suspend fun clearBasket()
}