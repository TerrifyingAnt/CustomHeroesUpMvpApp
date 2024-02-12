package jg.coursework.customheroesapp.presentation.screen.figure_detail

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.minio.GetObjectArgs
import jg.coursework.customheroesapp.presentation.components.CustomHeroesButton
import jg.coursework.customheroesapp.presentation.components.Counter
import jg.coursework.customheroesapp.presentation.components.StarRating
import jg.coursework.customheroesapp.presentation.screen.catalog.CatalogViewModel
import jg.coursework.customheroesapp.ui.theme.CustomHeroesOrange
import jg.coursework.customheroesapp.util.Constant
import jg.coursework.customheroesapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun FigureDetailScreen(figureModel: Int, viewModel: CatalogViewModel = hiltViewModel()) {
    var loading = true
    val figureDetailState by viewModel.figureDetailState.collectAsState()
    val context = LocalContext.current
    val imageList = remember { mutableStateOf<List<Bitmap>?>(null) }
    var temp by remember { mutableStateOf(viewModel.basketCount) }


    LaunchedEffect(figureDetailState) {
        imageList.value = emptyList()
        viewModel.getFigureById(figureModel)
        viewModel.getCountInBasket(figureModel)
        when (figureDetailState.status) {
            Resource.Status.SUCCESS -> {
                loading = false
                for(i in 1..3) {
                    withContext(Dispatchers.IO) {
                        val image = Constant.minioClient.getObject(
                            GetObjectArgs.builder()
                                .bucket(Constant.BUCKET_NAME)
                                .`object`(viewModel.figureDetailList.value[0].sourcePath + "/content/$i.jpg")
                                .build()
                        )
                        println("Скачано $i")
                        val decodedBitmap = BitmapFactory.decodeStream(image)
                        imageList.value = imageList.value?.plus(decodedBitmap)
                    }
                }
            }
            Resource.Status.ERROR -> {
                Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
            }

            Resource.Status.LOADING -> {}
        }
    }
    if(figureDetailState.status == Resource.Status.SUCCESS) {
        Column(
            Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            LazyRow(Modifier.fillMaxWidth()){
                items(imageList.value?.size ?: 0) {
                    image ->
                    val imageValue = imageList.value
                    if (imageValue != null) {
                        Image(
                            imageValue[image].asImageBitmap(),
                            modifier = Modifier
                                .height(300.dp)
                                .fillMaxWidth(),
                            contentDescription = "xd",
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.TopCenter
                        )
                    }
                    else {
                        Box(
                            modifier = Modifier
                                .height(300.dp)
                                .fillMaxWidth()
                                .background(Color.Gray)
                        )
                    }
                }
            }

            Text(
                text = viewModel.figureDetailList.value[0].title,
                style = MaterialTheme.typography.h3,
                fontWeight = FontWeight.Bold,
                color = CustomHeroesOrange
            )
            Divider()
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp), horizontalArrangement = Arrangement.Start) {
                Text(
                    text = "Описание",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = viewModel.figureDetailList.value[0].description,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(5.dp)
            )
            Divider()
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Рейтинг:",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(5.dp)
                )
                StarRating(viewModel.figureDetailList.value[0].rating)
            }
            Divider()

            Column(modifier = Modifier
                .padding(bottom = 80.dp)
                .fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom,
                ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Цена: ${viewModel.figureDetailList.value[0].price}",
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(5.dp)
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if(temp.value <= 0) {
                        CustomHeroesButton(
                            text = "Купить",
                            backgroundColor = CustomHeroesOrange,
                            contentColor = Color.White,
                            onButtonClick = {
                                viewModel.addToBasket(figureModel, temp.value + 1)
                                temp.value += 1
                                            },
                            isLoading = false
                        )
                    }
                    else{
                        Counter(figureModel, viewModel.figureDetailList.value[0].price)

                    }
                }
            }
        }

    }


}



