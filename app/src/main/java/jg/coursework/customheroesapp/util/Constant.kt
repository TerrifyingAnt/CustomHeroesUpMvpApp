package jg.coursework.customheroesapp.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

object Constant {
    const val BASE_URL = "http://10.0.2.2:8081/"


}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route:String,
)

object NavigationItems {
    val BottomNavItems = listOf(
        BottomNavItem(
            label = "Home",
            icon = Icons.Default.Home,
            route = "home"
        ),
        BottomNavItem(
            label = "Search",
            icon = Icons.Default.ShoppingCart,
            route = "search"
        ),
        BottomNavItem(
            label = "Profile",
            icon = Icons.Default.PersonOutline,
            route = "profile"
        )
    )
}