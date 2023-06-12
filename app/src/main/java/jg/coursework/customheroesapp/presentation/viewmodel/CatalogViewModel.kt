package jg.coursework.customheroesapp.presentation.viewmodel

import android.content.SharedPreferences
import android.graphics.BitmapFactory
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.minio.GetObjectArgs
import jg.coursework.customheroesapp.data.dto.CatalogDTO.CatalogDTO
import jg.coursework.customheroesapp.data.dto.CatalogDTO.TagDTO
import jg.coursework.customheroesapp.domain.model.FigureModel
import jg.coursework.customheroesapp.domain.repository.CustomHeroesRepository
import jg.coursework.customheroesapp.util.Constant
import jg.coursework.customheroesapp.util.Constant.BUCKET_NAME
import jg.coursework.customheroesapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.stream.IntStream.range
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val customHeroesRepository: CustomHeroesRepository,
    private val sharedPreferences: SharedPreferences
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

    fun getCatalog() {
        viewModelScope.launch {
            try {
                val catalogResponse =
                    customHeroesRepository.getCatalog("пофиг что передавать")
                _catalogState.value = Resource.success(catalogResponse.body())
                println(catalogResponse.body())
                println(catalogResponse.code())
                println(catalogResponse.message())
                for(item in catalogResponse.body()!!) {
                    if (containFigure(item)) {
                        for(figure in figureList.value) {
                            if(figure.figure == item.figure) {
                                if(!containTag(item.tag, figure.tags))
                                    figure.tags.add(item.tag)
                            }
                        }
                    }
                    else {
                        if(figureList.value.isEmpty()) {
                            figureList.value = listOf(
                                FigureModel(
                                    item.figure,
                                    listOf(item.tag).toMutableList(),
                                    null
                                )
                            )
                        }
                        else {
                            figureList.value = figureList.value + FigureModel(
                                item.figure,
                                listOf(item.tag).toMutableList(),
                                null)
                        }
                    }
                }
                println(figureList.value)
            } catch (e: Exception) {
                _catalogState.value = Resource.error(e.message ?: "Возникла ошибка")
                println(e)
            }
        }
    }

    fun getFigureById(id: Int) {
        viewModelScope.launch {
            try {
                val catalogResponse =
                    customHeroesRepository.getFigureById(id, "пофиг что передавать")
                _figureDetailState.value = Resource.success(catalogResponse.body())
                println(catalogResponse.body())
                println(catalogResponse.code())
                for(item in catalogResponse.body()!!) {
                    if (containFigure(item)) {
                        for(figure in figureDetailList.value) {
                            if(figure.figure == item.figure) {
                                if(!containTag(item.tag, figure.tags))
                                    figure.tags.add(item.tag)
                            }
                        }
                    }
                    else {
                        if(figureDetailList.value.isEmpty()) {
                            figureDetailList.value = listOf(
                                FigureModel(
                                    item.figure,
                                    listOf(item.tag).toMutableList(),
                                    null
                                )
                            )
                        }
                        else {
                            figureDetailList.value = figureDetailList.value + FigureModel(
                                item.figure,
                                listOf(item.tag).toMutableList(),
                                null)
                        }
                    }
                }
                println(figureDetailList.value)
            } catch (e: Exception) {
                _figureDetailState.value = Resource.error(e.message ?: "Возникла ошибка")
                println(e.message)
            }
        }
    }

    fun checkIfInBasket(id: Int): Int {
        var tempLogic = false
        val basket = sharedPreferences.getString("basket","")
        var number = ""
        println(basket)
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
                return number.toInt()
            }
        }
        return 0
    }

    fun addToBasket(id: Int, count: Int) {
        var basket = sharedPreferences.getString("basket","")
        if(basket != null) {
            if(basket.indexOf("f{$id}x${count-1}") != -1) {
                basket = basket.replace("f{$id}x${count-1}", "f{$id}x${count}")
                sharedPreferences.edit().putString("basket", basket).apply()
            }
            else {
                if(basket == "") {
                    sharedPreferences.edit().putString("basket", "f{$id}x${count}").apply()
                }
                else {
                    sharedPreferences.edit().putString("basket", "$basket;f{$id}x${count}").apply()
                }
            }
        }
        else {
            sharedPreferences.edit().putString("basket", "f{$id}x${count}").apply()
        }
        println(sharedPreferences.getString("basket", "8===D"))
    }

    fun removeFromBasket(id: Int, count: Int) {
        var basket = sharedPreferences.getString("basket","")
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
                sharedPreferences.edit().putString("basket", basket).apply()
            }
            else {
                if(basket == "") {
                    sharedPreferences.edit().putString("basket", "f{$id}x${count}").apply()
                }
                else {
                    sharedPreferences.edit().putString("basket", "$basket;f{$id}x${count}").apply()
                }
            }
        }
        else {
            sharedPreferences.edit().putString("basket", "f{$id}x${count}").apply()
        }
        println(sharedPreferences.getString("basket", "8===D"))
    }

    fun getBasket(){
        val basket = sharedPreferences.getString("basket", "")
        val catalogList = mutableListOf<CatalogDTO>()
        if(basket != "") {
            viewModelScope.launch {
                try{
                    for(item in basket?.split(";")!!) {
                        val id = item.substring(item.indexOf("{") + 1, item.indexOf("}")).toInt()
                        val response = customHeroesRepository.getFigureById(id, "xd")
                        catalogList.add(response.body()?.get(0)!!)
                    }
                    _basketState.value = Resource.success(catalogList)
                    basketList.value = catalogList
                }  catch (e: Exception) {
                    _basketState.value = Resource.error(e.message ?: "Возникла ошибка")
                    println(e)
                }
            }

        }
    }

    fun createOrder(){
        viewModelScope.launch {
            try{
                customHeroesRepository.createOrder("какой-то заказ", "мне не нравится архитектура")
                sharedPreferences.edit().putString("basket", "").apply()
            }
            catch (e: Exception) {
                println(e)
            }
        }
    }


    private fun containFigure(catalog: CatalogDTO): Boolean {
        for(item in figureList.value) {
            if (item.figure == catalog.figure)
                return true
            }
        return false
    }

    private fun containTag(tagDTO: TagDTO, tags: MutableList<TagDTO>): Boolean {
        for(tag in tags) {
            if (tagDTO == tag)
                return true
        }
        return false
    }
}

