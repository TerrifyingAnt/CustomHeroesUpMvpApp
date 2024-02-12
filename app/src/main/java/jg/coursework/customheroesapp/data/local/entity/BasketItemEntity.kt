package jg.coursework.customheroesapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BasketItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val modelId: Int,
    val count: Int
)


