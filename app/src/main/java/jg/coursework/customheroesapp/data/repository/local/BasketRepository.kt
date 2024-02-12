package jg.coursework.customheroesapp.data.repository.local

import jg.coursework.customheroesapp.data.converters.toBasketItemEntity
import jg.coursework.customheroesapp.data.converters.toBasketItemModel
import jg.coursework.customheroesapp.data.local.dao.BasketDao
import jg.coursework.customheroesapp.data.converters.toListOfBasketItemModels
import jg.coursework.customheroesapp.domain.model.BasketItemModel
import jg.coursework.customheroesapp.domain.repository.local.IBasketRepository
import javax.inject.Inject

class BasketRepository @Inject constructor(
    private val basketDao: BasketDao
): IBasketRepository {
    override suspend fun getAllFromBasket(): List<BasketItemModel> {
        return basketDao.getAllFromBasket().toListOfBasketItemModels()
    }

    override suspend fun getItemFromBasketByModelId(modelId: Int): BasketItemModel? {
        return basketDao.getBasketItemEntityByModelId(modelId).toBasketItemModel()
    }

    override suspend fun updateCountInBasketByModelId(modelId: Int, count: Int) {
        basketDao.updateBasketItemEntityCountByModelId(modelId, count)
    }

    override suspend fun insertBasketItemEntity(basketItemModel: BasketItemModel) {
        basketDao.insertBasketItemEntity(basketItemModel.toBasketItemEntity())
    }

    override suspend fun deleteBasketItemEntityByModelId(modelId: Int) {
        basketDao.deleteBasketItemEntityByModelId(modelId)
    }

    override suspend fun clearBasket() {
        basketDao.clearBasket()
    }


}