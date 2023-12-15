package jg.coursework.customheroesapp.domain.model

import android.graphics.Bitmap


data class FigureModel (
    val id: Int,
    val title: String,
    val description: String,
    val price: Float,
    val rating: Float,
    val isMovable: Boolean,
    val timeOfMaking: String,
    val sourcePath: String,
    val tags: MutableList<TagModel>,
    var preview: Bitmap?
)