package jg.coursework.customheroesapp.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import jg.coursework.customheroesapp.AuthActivity
import jg.coursework.customheroesapp.data.dto.OrderDTO.OrderItemDTO
import jg.coursework.customheroesapp.data.dto.User
import jg.coursework.customheroesapp.domain.repository.CustomHeroesRepository
import jg.coursework.customheroesapp.domain.use_case.ValidateLoginInputUseCase
import jg.coursework.customheroesapp.presentation.components.AuthButton
import jg.coursework.customheroesapp.presentation.viewmodel.LoginViewModel
import jg.coursework.customheroesapp.presentation.viewmodel.UserViewModel
import jg.coursework.customheroesapp.ui.theme.CustomHeroesOrange
import jg.coursework.customheroesapp.util.MainScreenRoutes
import jg.coursework.customheroesapp.util.ScreenRoutes
import javax.inject.Inject

@Composable
fun ProfileScreen(viewModel: UserViewModel = hiltViewModel())
{
    val context = LocalContext.current
    val activity = LocalContext.current as Activity

    LaunchedEffect(key1 = Unit) {
        viewModel.getOrders()
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 60.dp, bottom = 80.dp)) {
        val user = viewModel.getMe()
        if(user != null) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = user.login,
                    style = MaterialTheme.typography.h5)
            }
        }
        Divider()
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = "Мои заказы",
                style = MaterialTheme.typography.h5)
        }
        LazyColumn(Modifier.fillMaxHeight(0.85f)) {
            items(viewModel.coolOrderList.reversed()) {
                item ->
                    for(simpleOrder in item) {
                        OrderCard(simpleOrder)
                    }
                }
        }

        Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                AuthButton(
                    text = "Выйти",
                    backgroundColor = CustomHeroesOrange,
                    contentColor = Color.White,
                    onButtonClick = {
                        viewModel.logout()
                        activity.startActivity(Intent(context, AuthActivity::class.java))
                        activity.finish()
                    },
                    isLoading = false
                )
            }
        }
    }
}

@Composable
fun OrderCard(order: List<OrderItemDTO>) {
    var price = 0f
    Surface(shape = RoundedCornerShape(10.dp), elevation = 10.dp, modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)) {
        Column(Modifier.padding(5.dp)){
            Text(
                text = "Номер заказа: " + order[0].order.id.toString() + " от " + order[0].order.date,
                style = MaterialTheme.typography.body1
            )
            Divider()
            Text(
                text = "Статус заказа: " + order[0].order.state,
                style = MaterialTheme.typography.body1
            )
            Column(verticalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "Состав заказа",
                    style = MaterialTheme.typography.body1
                )
                for(i in order) {
                    Text(
                        text = "\t\t" + i.figure.title + " x" + i.count,
                        style = MaterialTheme.typography.body2
                    )
                    price += i.figure.price * i.count
                }
            }
            Text(
                text = "Итого: $price",
                style = MaterialTheme.typography.body2
            )
        }
    }
}