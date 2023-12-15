package jg.coursework.customheroesapp.presentation.screen.basket

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import jg.coursework.customheroesapp.data.dto.CatalogDTO.CatalogDTO
import jg.coursework.customheroesapp.presentation.components.AuthButton
import jg.coursework.customheroesapp.presentation.components.Counter
import jg.coursework.customheroesapp.presentation.screen.catalog.CatalogViewModel
import jg.coursework.customheroesapp.ui.theme.CustomHeroesOrange
import jg.coursework.customheroesapp.util.MainScreenRoutes
import jg.coursework.customheroesapp.util.Resource

@Composable
fun BasketScreen(navController: NavController, viewModel: CatalogViewModel = hiltViewModel()) {

    val basketState by viewModel.basketState.collectAsState()
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    var basketList by remember {
        mutableStateOf(viewModel.basketList.value)
    }

    LaunchedEffect(basketState) {
        viewModel.getBasket()


        when (basketState.status) {
            Resource.Status.SUCCESS -> {
                println("xd")
                basketList = viewModel.basketList.value
            }
            Resource.Status.ERROR ->
                Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show()

            Resource.Status.LOADING -> {}
        }
    }
    Column() {
        if(basketList.isNotEmpty()) {
            LazyColumn(
                Modifier
                    .fillMaxHeight(0.8f)
                    .padding(top = 60.dp, bottom = 80.dp)
            ) {
                items(viewModel.basketList.value) { item ->
                    BasketCard(navController, item)
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                AuthButton(
                    text = "Заказать",
                    backgroundColor = CustomHeroesOrange,
                    contentColor = Color.White,
                    onButtonClick = {
                        viewModel.createOrder()
                        Toast.makeText(
                            activity,
                            "Заказ создан, скоро с Вами свяжется мастер",
                            Toast.LENGTH_LONG
                        ).show()
                        basketList = emptyList()

                    },
                    isLoading = false
                )
            }
        }
        else {
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(text = "В корзине пока еще пусто...(")
                }}
            viewModel.getBasket()
        }
    }
}

@Composable
fun BasketCard(navController: NavController, figureModel: CatalogDTO, viewModel: CatalogViewModel = hiltViewModel()){


    Surface(shape = RoundedCornerShape(10.dp), elevation = 10.dp, modifier = Modifier
        .padding(5.dp)
        .clickable {
            navController.navigate(
                MainScreenRoutes.FigureDetailScreen.route
                    .replace("{FigureModel}", "${figureModel.figure.id}")
            )
        }) {
        Column(verticalArrangement = Arrangement.Center) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = figureModel.figure.title,
                    style = MaterialTheme.typography.h5
                )
            }
            Counter(figureModel.figure.id, figureModel.figure.price)
        }
    }
}

