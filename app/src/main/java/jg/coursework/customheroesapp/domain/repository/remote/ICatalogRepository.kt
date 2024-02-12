package jg.coursework.customheroesapp.domain.repository.remote

import jg.coursework.customheroesapp.data.dto.CatalogDTO.CatalogDTO
import jg.coursework.customheroesapp.util.Resource

interface ICatalogRepository {
    suspend fun getCatalog(): Resource<List<CatalogDTO>>

    suspend fun getFigureById(id: Int): Resource<List<CatalogDTO>>
}