package jg.coursework.customheroesapp.data.dto.CatalogDTO

data class FigureDTO (
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val rating: Int,
    val isMovable: Boolean,
    val timeOfMaking: String,
    val sourcePath: String
)