package jg.coursework.customheroesapp.data.repository

import jg.coursework.customheroesapp.data.dto.CatalogDTO.CatalogDTO
import jg.coursework.customheroesapp.data.remote.ICatalogClient
import jg.coursework.customheroesapp.domain.repository.ICatalogRepository
import jg.coursework.customheroesapp.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogRepositoryImpl @Inject constructor(
    private val ICatalogClient: ICatalogClient,
) : BaseDataSource(), ICatalogRepository {

    override suspend fun getCatalog(): Resource<List<CatalogDTO>> {
        return safeApiCall {ICatalogClient.getCatalog()}
    }

    override suspend fun getFigureById(id: Int): Resource<List<CatalogDTO>> {
        return safeApiCall{ICatalogClient.getFigureById(id)}
    }
}