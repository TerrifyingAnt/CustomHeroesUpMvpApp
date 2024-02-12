package jg.coursework.customheroesapp.data.converters

import jg.coursework.customheroesapp.data.dto.CatalogDTO.CatalogDTO
import jg.coursework.customheroesapp.domain.model.FigureModel

fun List<CatalogDTO>.toDomainListFiguresModel(): List<FigureModel> {
    return this.map { it.toFigureDomainModel()
    }
}

fun List<CatalogDTO>.toFigureDomainModel(): FigureModel {
    return this[0].toFigureDomainModel()
}

fun CatalogDTO.toFigureDomainModel(): FigureModel {
    return FigureModel(
        figure.id,
        figure.title,
        figure.description,
        figure.price,
        figure.rating,
        figure.isMovable,
        figure.timeOfMaking,
        figure.sourcePath,
        mutableListOf(),
    null
    )
}
