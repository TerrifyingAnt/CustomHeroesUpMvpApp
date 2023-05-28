@file:OptIn(ExperimentalMaterial3Api::class)

package jg.coursework.customheroesapp.util

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jg.coursework.customheroesapp.presentation.CatalogScreen
import jg.coursework.customheroesapp.presentation.LoginScreen
import jg.coursework.customheroesapp.presentation.RegisterScreen
import jg.coursework.customheroesapp.presentation.components.BottomMenuItem
import jg.coursework.customheroesapp.ui.theme.CustomHeroesOrange

import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize().background(CustomHeroesOrange),
        topBar = {
            TopAppBar(
                title = {
                    Text("CustomHeroes",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 28.sp,
                        color = Color.White) },
                contentColor = CustomHeroesOrange,
                backgroundColor = CustomHeroesOrange)
        },
        bottomBar = { MyBottomBar(navController) },
    ) {
        NavHost(
            navController = navController,
            startDestination = MainScreenRoutes.CatalogScreen.route
        ) {
            composable(MainScreenRoutes.CatalogScreen.route) {
                CatalogScreen()
            }
            composable(MainScreenRoutes.BasketScreen.route) {
                // TODO
            }
            composable(MainScreenRoutes.ProfileScreen.route) {
                // TODO
            }
        }
    }
}

sealed class MainScreenRoutes(val route: String) {
    object CatalogScreen: ScreenRoutes("catalog_screen")
    object BasketScreen: ScreenRoutes("basket_screen")
    object ProfileScreen: ScreenRoutes("profile_screen")
}

@Composable
fun MyBottomBar(navController: NavController) {
    val bottomMenuItemsList = prepareBottomMenu()
    var selectedItem by remember {
        mutableStateOf("catalog_screen")
    }

    BottomAppBar( containerColor = CustomHeroesOrange
    ) {
        bottomMenuItemsList.forEach { menuItem ->
            BottomNavigationItem(
                selected = (selectedItem == menuItem.label),
                onClick = {
                    selectedItem = menuItem.label
                    // Navigate to the corresponding destination
                    navController.navigate(menuItem.label.lowercase(Locale.ROOT))
                },
                icon = {
                    if(selectedItem == menuItem.label) {
                        Icon(
                            imageVector = menuItem.icon,
                            contentDescription = menuItem.label,
                            tint = Color.White
                        )
                    }
                    else {
                        Icon(
                            imageVector = menuItem.icon,
                            contentDescription = menuItem.label,
                            tint = Color.Gray
                        )
                    }
                },
                enabled = true,
            )
        }
    }
}

private fun prepareBottomMenu(): List<BottomMenuItem> {
    val bottomMenuItemsList = arrayListOf<BottomMenuItem>()

    // add menu items
    bottomMenuItemsList.add(BottomMenuItem(label = "catalog_screen", icon = Icons.Filled.Home))
    bottomMenuItemsList.add(BottomMenuItem(label = "basket_screen", icon = Icons.Filled.ShoppingCart))
    bottomMenuItemsList.add(BottomMenuItem(label = "profile_screen", icon = Icons.Filled.Person))

    return bottomMenuItemsList
}
