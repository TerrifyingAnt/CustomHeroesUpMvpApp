package jg.coursework.customheroesapp.presentation.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jg.coursework.customheroesapp.data.dto.AuthResponseDTO
import jg.coursework.customheroesapp.data.dto.CatalogDTO.CatalogDTO
import jg.coursework.customheroesapp.data.dto.CatalogDTO.TagDTO
import jg.coursework.customheroesapp.data.dto.LoginDTO.LoginRequestDTO
import jg.coursework.customheroesapp.domain.model.FigureModel
import jg.coursework.customheroesapp.domain.repository.CustomHeroesRepository
import jg.coursework.customheroesapp.domain.use_case.ValidateLoginInputUseCase
import jg.coursework.customheroesapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val customHeroesRepository: CustomHeroesRepository,
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    private val _catalogState = MutableStateFlow<Resource<List<CatalogDTO>>>(Resource.loading(null))
    val catalogState: StateFlow<Resource<List<CatalogDTO>>> = _catalogState
    val figureList = mutableStateOf<List<FigureModel>>(emptyList())


    fun getCatalog() {
        viewModelScope.launch {
            try {
                val catalogResponse =
                    customHeroesRepository.getCatalog("похуй что передавать")
                _catalogState.value = Resource.success(catalogResponse.body())
                println(catalogResponse.body())
                println(catalogResponse.code())
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
                        figureList.value = listOf(FigureModel(item.figure, listOf(item.tag).toMutableList()))
                    }
                }
                println(figureList.value)
            } catch (e: Exception) {
                _catalogState.value = Resource.error(e.message ?: "Возникла ошибка")
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

