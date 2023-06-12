package jg.coursework.customheroesapp.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import io.minio.MinioClient

object Constant {

    
    const val BASE_URL = "http://10.0.2.2:8081/"
    const val BUCKET_NAME = "customheroesbacket"
    const val BASE_MINIO_URL = "http://10.0.0.2:9001/customheroesbacket/preview.jpg"
    val minioClient = MinioClient.builder()
        .endpoint("http://10.0.2.2:9000/") // Replace with your MinIO server's URL
        .credentials("<USERNAME>", "<PASSWORD>") // Replace with the root user's credentials
        .build()
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