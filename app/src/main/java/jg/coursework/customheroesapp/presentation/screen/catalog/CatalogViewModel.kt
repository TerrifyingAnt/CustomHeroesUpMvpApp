package jg.coursework.customheroesapp.presentation.screen.catalog

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jg.coursework.customheroesapp.data.dto.CatalogDTO.CatalogDTO
import jg.coursework.customheroesapp.data.local.DataStoreManager
import jg.coursework.customheroesapp.data.network_converters.toDomainListFiguresModel
import jg.coursework.customheroesapp.domain.model.FigureModel
import jg.coursework.customheroesapp.domain.model.TagModel
import jg.coursework.customheroesapp.domain.repository.ICatalogRepository
import jg.coursework.customheroesapp.domain.repository.IOrderRepository
import jg.coursework.customheroesapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val customHeroesCatalogRepository: ICatalogRepository,
    private val customHeroesOrderRepository: IOrderRepository,
    private val dataStoreManager: DataStoreManager

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
        for (item in mainListFigures) {
            if (containFigure(item)) {
                for (figure in figureList.value) {
                    if (figure.id == item.id) {
                        if (!containTag(item.tags[0], figure.tags))
                            figure.tags.add(item.tags[0])
                    }
                }
            } else {
                if (figureList.value.isEmpty()) {
                    figureList.value = listOf(item)
                } else {
                    figureList.value = figureList.value + item
                }
            }
        }
        println(figureList.value)
    }


    fun getFigureById(id: Int) = viewModelScope.launch {
        val catalogResponse =
            customHeroesCatalogRepository.getFigureById(id)
        _figureDetailState.value = catalogResponse
        val figureByIdDTO = catalogResponse.data?: return@launch
        val figureDomainModelById = figureByIdDTO.toDomainListFiguresModel()
        for(item in figureDomainModelById) {
            if (containFigure(item)) {
                for(figure in figureDetailList.value) {
                    if(figure.id == item.id) {
                        if(!containTag(item.tags[0], figure.tags))
                            figure.tags.add(item.tags[0])
                    }
                }
            }
            else {
                if(figureDetailList.value.isEmpty()) {
                    figureDetailList.value = listOf(item)
                }
                else {
                    figureDetailList.value = figureDetailList.value + item
                }
            }
        }
        // TODO - удалить логи
        Log.d("FIGURE_LIST", figureDetailList.value.toString())

    }

    fun checkIfInBasket(id: Int) = viewModelScope.launch {
        var tempLogic = false
        val basket = dataStoreManager.getBasket().first()
        var number = ""

        if(basket != null) {
            // концепт такой, что сначала идет f{id}x{count};
            if(basket.indexOf("f{$id}") != -1) {
                for(i in basket?.indexOf("f{$id}")?.rangeTo(basket.length)!!) {
                    if(i < basket.length && basket[i] != ';' ) {
                        if (tempLogic) {
                            number += basket[i]
                        }
                        if (basket[i] == 'x') {
                            tempLogic = true
                        }
                    }
                    else
                        break
                }
                basketCount.value = number.toInt()
            }
        }
        basketCount.value = 0
    }

    fun addToBasket(id: Int, count: Int) = viewModelScope.launch {
        var basket = dataStoreManager.getBasket().first()
        if(basket != null) {
            if(basket.indexOf("f{$id}x${count-1}") != -1) {
                basket = basket.replace("f{$id}x${count-1}", "f{$id}x${count}")
                dataStoreManager.setBasket(basket)
            }
            else {
                if(basket == "") {
                    dataStoreManager.setBasket("f{$id}x${count}")
                }
                else {
                    dataStoreManager.setBasket("$basket;f{$id}x${count}")
                }
            }
        }
        else {
            dataStoreManager.setBasket("f{$id}x${count}")
        }
    }

    fun removeFromBasket(id: Int, count: Int) = viewModelScope.launch {
        var basket = dataStoreManager.getBasket().first()
        if(basket != null) {
            if(basket.indexOf("f{$id}x${count+1}") != -1) {
                if(count == 0) {
                    if(basket.indexOf(";f{$id}x${count+1}") != -1)
                        basket = basket.replace(";f{$id}x${count+1}", "")
                    else {
                        basket = basket.replace("f{$id}x${count+1};", "")
                        basket = basket.replace("f{$id}x${count+1}", "")
                    }
                }
                else
                    basket = basket.replace("f{$id}x${count+1}", "f{$id}x${count}")
                dataStoreManager.setBasket(basket)
            }
            else {
                if(basket == "") {
                    dataStoreManager.setBasket("f{$id}x${count}")
                }
                else {
                    dataStoreManager.setBasket("$basket;f{$id}x${count}")
                }
            }
        }
        else {
            dataStoreManager.setBasket("f{$id}x${count}")
        }
    }

    fun getBasket() = viewModelScope.launch {
        val basket = dataStoreManager.getBasket().first()
        val catalogList = mutableListOf<CatalogDTO>()
        if(basket != "") {
            val splitBasket = basket?.split(";") ?: return@launch
            for(item in splitBasket) {
                val id = item.substring(item.indexOf("{") + 1, item.indexOf("}")).toInt()
                val response = customHeroesCatalogRepository.getFigureById(id)
                val figure = response.data?.get(0) ?: return@launch
                catalogList.add(figure)
            }
            _basketState.value = Resource.success(catalogList)
            basketList.value = catalogList
        }
    }

    fun createOrder() = viewModelScope.launch {
        customHeroesOrderRepository.createOrder()
        dataStoreManager.setBasket("")
    }


    private fun containFigure(figureModel: FigureModel): Boolean {
        for(item in figureList.value) {
            if (item.id == figureModel.id)
                return true
            }
        return false
    }

    private fun containTag(tagModel: TagModel, tags: MutableList<TagModel>): Boolean {
        for(tag in tags) {
            if (tagModel == tag)
                return true
        }
        return false
    }
}
