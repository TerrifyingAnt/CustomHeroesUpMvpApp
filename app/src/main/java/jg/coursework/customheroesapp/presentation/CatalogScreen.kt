package jg.coursework.customheroesapp.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import jg.coursework.customheroesapp.presentation.components.FigureCard
import jg.coursework.customheroesapp.presentation.viewmodel.CatalogViewModel
import jg.coursework.customheroesapp.presentation.viewmodel.LoginViewModel
import jg.coursework.customheroesapp.util.Resource

@Composable
fun CatalogScreen(catalogViewModel: CatalogViewModel = hiltViewModel()) {

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
            Column(Modifier.fillMaxHeight().padding(top = 60.dp, start = 5.dp)) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(catalogViewModel.figureList.value) { item ->
                        FigureCard(item)
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