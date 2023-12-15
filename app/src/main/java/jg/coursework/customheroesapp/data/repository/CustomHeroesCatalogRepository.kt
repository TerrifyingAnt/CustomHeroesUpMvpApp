package jg.coursework.customheroesapp.data.repository

import jg.coursework.customheroesapp.data.dto.CatalogDTO.CatalogDTO
import jg.coursework.customheroesapp.data.remote.CustomHeroesCatalogClient
import jg.coursework.customheroesapp.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomHeroesCatalogRepository @Inject constructor(
    private val customHeroesCatalogClient: CustomHeroesCatalogClient,
) : BaseDataSource() {

    suspend fun getCatalog(): Resource<List<CatalogDTO>> {
        return safeApiCall {customHeroesCatalogClient.getCatalog()}
    }

    suspend fun getFigureById(id: Int): Resource<List<CatalogDTO>> {
        return safeApiCall{customHeroesCatalogClient.getFigureById(id)}
    }
}