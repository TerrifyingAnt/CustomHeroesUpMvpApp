package jg.coursework.customheroesapp.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.google.common.base.Utf8
import com.google.gson.Gson


import jg.coursework.customheroesapp.presentation.components.FigureCard
import jg.coursework.customheroesapp.presentation.viewmodel.CatalogViewModel
import jg.coursework.customheroesapp.util.MainNavigation
import jg.coursework.customheroesapp.util.MainScreenRoutes
import jg.coursework.customheroesapp.util.Resource
import okio.ByteString.Companion.encode

@Composable
fun CatalogScreen(navController: NavController, catalogViewModel: CatalogViewModel = hiltViewModel()) {

    val catalogState by catalogViewModel.catalogState.collectAsState()
    val context = LocalContext.current
    var loading = false

    LaunchedEffect(catalogState) {
        catalogViewModel.getCatalog()

        when (catalogState.status) {
            Resource.Status.SUCCESS -> {
                println("xd")
                loading = true
            }
            Resource.Status.ERROR ->
                Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show()

            Resource.Status.LOADING -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {

        if(catalogState.status == Resource.Status.SUCCESS) {
            println(catalogViewModel.figureList.value.size)
            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(top = 60.dp, bottom = 80.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(200.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(10.dp)
                ) {
                    items(catalogViewModel.figureList.value.size) { item ->
                        FigureCard(
                            catalogViewModel.figureList.value[item]
                        ) {
                            val figureModel = catalogViewModel.figureList.value[item]
                            navController.navigate(MainScreenRoutes.FigureDetailScreen.route
                                .replace("{FigureModel}", "${figureModel.figure.id}")
                            )
                        }
                    }
                }
            }
        }
        else {
            Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

