package jg.coursework.customheroesapp.presentation.screen.catalog

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jg.coursework.customheroesapp.data.dto.CatalogDTO.CatalogDTO
import jg.coursework.customheroesapp.data.local.DataStoreManager
import jg.coursework.customheroesapp.data.converters.toDomainListFiguresModel
import jg.coursework.customheroesapp.domain.model.BasketItemModel
import jg.coursework.customheroesapp.domain.model.FigureModel
import jg.coursework.customheroesapp.domain.model.TagModel
import jg.coursework.customheroesapp.domain.repository.local.IBasketRepository
import jg.coursework.customheroesapp.domain.repository.remote.ICatalogRepository
import jg.coursework.customheroesapp.domain.repository.remote.IOrderRepository
import jg.coursework.customheroesapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val customHeroesCatalogRepository: ICatalogRepository,
    private val customHeroesOrderRepository: IOrderRepository,
    private val dataStoreManager: DataStoreManager,
    private val iBasketRepository: IBasketRepository

): ViewModel() {

    private val _catalogState = MutableStateFlow<Resource<List<CatalogDTO>>>(Resource.loading(null))
    val catalogState: StateFlow<Resource<List<CatalogDTO>>> = _catalogState

    private val _basketState = MutableStateFlow<Resource<List<CatalogDTO>>>(Resource.loading(null))
    val basketState: StateFlow<Resource<List<CatalogDTO>>> = _basketState
    val basketList = mutableStateOf<List<CatalogDTO>>(emptyList())

    private val _figureDetailState = MutableStateFlow<Resource<List<CatalogDTO>>>(Resource.loading(null))
    val figureDetailState: StateFlow<Resource<List<CatalogDTO>>> = _figureDetailState

    val figureList = mutableStateOf<List<FigureModel>>(emptyList())
    val figureDetailList = mutableStateOf<List<FigureModel>>(emptyList())

    val basketCount = mutableStateOf<Int>(0)

    fun getCatalog() = viewModelScope.launch {
        val catalogResponse =
            customHeroesCatalogRepository.getCatalog()
        _catalogState.value = catalogResponse
        val listFiguresDTO = catalogResponse.data ?: return@launch
        val mainListFigures = listFiguresDTO.toDomainListFiguresModel()

        updateFigureTags(mainListFigures, figureList)

        // TODO - убрать логи
        Log.d("FIGURE_LIST", figureList.value.toString())
    }


    fun getFigureById(id: Int) = viewModelScope.launch {
        val catalogResponse =
            customHeroesCatalogRepository.getFigureById(id)
        _figureDetailState.value = catalogResponse
        val figureByIdDTO = catalogResponse.data?: return@launch
        val figureDomainModelById = figureByIdDTO.toDomainListFiguresModel()

        updateFigureTags(figureDomainModelById, figureDetailList)

        // TODO - убрать логи
        Log.d("FIGURE_LIST", figureDetailList.value.toString())

    }

    fun getCountInBasket(id: Int) = viewModelScope.launch {
        val model = iBasketRepository.getItemFromBasketByModelId(id)
        if (model != null) {
            basketCount.value = model.count
        }
        else {
            basketCount.value = 0
        }
    }

    fun addToBasket(id: Int, count: Int) = viewModelScope.launch {
        iBasketRepository.insertBasketItemEntity(BasketItemModel(id, count))
    }

    fun removeFromBasket(id: Int, count: Int) = viewModelScope.launch {
        iBasketRepository.deleteBasketItemEntityByModelId(id)
    }

    fun getBasket() = viewModelScope.launch {
        val currentBasketList = iBasketRepository.getAllFromBasket()
        val catalogList = mutableListOf<CatalogDTO>()
        if(currentBasketList.isNotEmpty()) {
            currentBasketList.forEach {item ->
                val id = item.modelId
                val response = customHeroesCatalogRepository.getFigureById(id)
                val figure = response.data?.get(0) ?: return@launch
                catalogList.add(figure)
            }
            _basketState.value = Resource.success(catalogList)
            basketList.value = catalogList
        }
    }

    fun createOrder() = viewModelScope.launch {
        val currentBasket = iBasketRepository.getAllFromBasket()
        customHeroesOrderRepository.createOrder(currentBasket)
        iBasketRepository.clearBasket()
    }

    private fun updateFigureTags(figureList: List<FigureModel>, figureListState: MutableState<List<FigureModel>>) {
        figureList.forEach { item ->
            if (figureListState.value.map {it.id}.contains(item.id)) {
                figureListState.value.forEach { figure ->
                    if (figure.id == item.id) {
                        if (!figure.tags.contains(item.tags[0]))
                            figure.tags.add(item.tags[0])
                    }
                }
            } else {
                if (figureListState.value.isEmpty()) {
                    figureListState.value = listOf(item)
                } else {
                    figureListState.value = figureListState.value + item
                }
            }
        }
    }
}
