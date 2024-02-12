package jg.coursework.customheroesapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import jg.coursework.customheroesapp.data.local.entity.BasketItemEntity

@Dao
interface BasketDao {
    @Query("SELECT * FROM BasketItemEntity")
    suspend fun getAllFromBasket(): List<BasketItemEntity>?

    @Query("SELECT * FROM BasketItemEntity WHERE modelId=:modelId")
    suspend fun getBasketItemEntityByModelId(modelId: Int): BasketItemEntity?

    @Query("UPDATE basketitementity SET count=:count WHERE modelId=:modelId")
    suspend fun updateBasketItemEntityCountByModelId(modelId: Int, count: Int)

    @Insert
    suspend fun insertBasketItemEntity(item: BasketItemEntity)

    @Query("DELETE FROM basketitementity WHERE modelId=:modelId")
    suspend fun deleteBasketItemEntityByModelId(modelId: Int)

    @Query("DELETE FROM basketitementity")
    suspend fun clearBasket()

}