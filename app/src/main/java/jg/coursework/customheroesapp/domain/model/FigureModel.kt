package jg.coursework.customheroesapp.domain.model

import jg.coursework.customheroesapp.data.dto.CatalogDTO.FigureDTO
import jg.coursework.customheroesapp.data.dto.CatalogDTO.TagDTO

data class FigureModel (
    val figure: FigureDTO,
    val tags: MutableList<TagDTO>
)