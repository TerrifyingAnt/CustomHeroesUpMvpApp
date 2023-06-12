package jg.coursework.customheroesapp.data.dto.CatalogDTO

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class FigureDTO (
    val id: Int,
    val title: String,
    val description: String,
    val price: Float,
    val rating: Float,
    val isMovable: Boolean,
    val timeOfMaking: String,
    val sourcePath: String
)