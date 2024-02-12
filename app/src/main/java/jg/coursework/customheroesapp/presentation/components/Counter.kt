package jg.coursework.customheroesapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import jg.coursework.customheroesapp.presentation.screen.catalog.CatalogViewModel
import jg.coursework.customheroesapp.ui.theme.CustomHeroesOrange

@Composable
fun Counter(id: Int, price: Float, viewModel: CatalogViewModel = hiltViewModel()) {
    var count by remember { mutableStateOf(viewModel.basketCount) }

    viewModel.getCountInBasket(id)

    Column(Modifier.fillMaxHeight()) {
        Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.RemoveCircleOutline,
                "remove figure",
                modifier = Modifier.clickable {
                    if (count.value > 0) {
                        count.value--
                        viewModel.removeFromBasket(id, count.value)
                    }
                }.size(30.dp),
                tint = CustomHeroesOrange
            )

            Text(
                text = count.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 30.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Icon(
                Icons.Default.AddCircleOutline,
                "add figure",
                modifier = Modifier.clickable {
                    count.value++
                    viewModel.addToBasket(id, count.value)
                }.size(30.dp),
                tint = CustomHeroesOrange
            )
        }
        Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Итоговая цена: ${price * count.value}",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 25.sp
            )
        }
    }
}

