package jg.coursework.customheroesapp.domain.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import jg.coursework.customheroesapp.data.dto.CatalogDTO.FigureDTO
import jg.coursework.customheroesapp.data.dto.CatalogDTO.TagDTO
import java.io.Serializable


data class FigureModel (
    val figure: FigureDTO,
    val tags: MutableList<TagDTO>,
    var preview: Bitmap?
)