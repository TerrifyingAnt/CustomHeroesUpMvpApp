package jg.coursework.customheroesapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import jg.coursework.customheroesapp.domain.model.FigureModel


@Composable
fun FigureCard(figureModel: FigureModel) {
    Surface(shape = RoundedCornerShape(10), elevation = 3.dp) {
        Column(Modifier.fillMaxHeight()) {
            Text(figureModel.figure.title, style = MaterialTheme.typography.h4)
            Row(Modifier.fillMaxWidth(0.5f), horizontalArrangement = Arrangement.Center) {
                for (tag in figureModel.tags) {
                    Text(tag.title + ";", style = MaterialTheme.typography.h4, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }
        }

    }
}