package jg.coursework.customheroesapp.data.converters

import jg.coursework.customheroesapp.data.local.entity.BasketItemEntity
import jg.coursework.customheroesapp.domain.model.BasketItemModel

fun List<BasketItemEntity>?.toListOfBasketItemModels(): List<BasketItemModel> {
    val resultList = mutableListOf<BasketItemModel>()
    this?.forEach { entity ->
        resultList.add(BasketItemModel(entity.modelId, entity.count))
    }
    return resultList
}

fun BasketItemEntity?.toBasketItemModel(): BasketItemModel? {
    return if (this != null) {
         BasketItemModel(this.modelId, this.count)
    } else
    null
}

fun BasketItemModel.toBasketItemEntity(): BasketItemEntity {
    return BasketItemEntity(
        id = -1,
        modelId = this.modelId,
        count = this.count
    )
}

// TODO - refactor
fun List<BasketItemModel>.toCringeStringFormat(): String {
    var resultOrder = ""
    this.forEach {
        resultOrder += "f{${it.modelId}}x${it.count}"
        if (it != this.last()) {
            resultOrder += ";"
        }
    }
    return resultOrder
}