package jg.coursework.customheroesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import jg.coursework.customheroesapp.data.local.dao.BasketDao
import jg.coursework.customheroesapp.data.local.entity.BasketItemEntity

@Database(entities = [
    BasketItemEntity::class
                     ],
    version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun basketDao(): BasketDao
}