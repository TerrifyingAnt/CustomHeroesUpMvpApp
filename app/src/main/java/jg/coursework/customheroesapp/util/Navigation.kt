package jg.coursework.customheroesapp.util

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jg.coursework.customheroesapp.MainActivity
import jg.coursework.customheroesapp.presentation.CatalogScreen
import jg.coursework.customheroesapp.presentation.LoginScreen
import jg.coursework.customheroesapp.presentation.RegisterScreen

@Composable
fun Navigation() {

    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.LoginScreen.route
    ) {
        composable(ScreenRoutes.LoginScreen.route) {
            LoginScreen(
                onLoginSuccessNavigation = {
                    activity.startActivity(Intent(context, MainActivity::class.java))
                    activity.finish()
                },
                onNavigationToRegisterScreen = {
                    navController.navigate(ScreenRoutes.RegisterScreen.route) {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(ScreenRoutes.RegisterScreen.route) {
            RegisterScreen(
                onRegisterSuccessNavigation = {
                    activity.startActivity(Intent(context, MainActivity::class.java))
                    activity.finish()
                },
                onNavigationToLoginScreen = {
                    navController.navigate(ScreenRoutes.LoginScreen.route) {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}

sealed class ScreenRoutes(val route: String) {
    object LoginScreen: ScreenRoutes("login_screen")
    object RegisterScreen: ScreenRoutes("register_screen")
    //object CatalogScreen: ScreenRoutes("catalog_screen")
}