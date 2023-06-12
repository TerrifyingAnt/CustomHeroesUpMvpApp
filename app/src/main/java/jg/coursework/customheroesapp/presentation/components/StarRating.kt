package jg.coursework.customheroesapp.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jg.coursework.customheroesapp.ui.theme.CustomHeroesYellow

@Composable
fun StarRating(rating: Float, maxRating: Int = 5) {
    Row {
        repeat(maxRating) { index ->
            val icon = if (index < rating) {
                Icons.Default.Star
            } else {
                Icons.Default.StarBorder
            }
            Icon(
                icon,
                contentDescription = null,
                tint = CustomHeroesYellow,
                modifier = Modifier.size(50.dp)
            )
        }
    }
}