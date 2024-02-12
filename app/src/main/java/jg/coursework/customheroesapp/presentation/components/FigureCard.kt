package jg.coursework.customheroesapp.presentation.components

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.minio.GetObjectArgs

import jg.coursework.customheroesapp.domain.model.FigureModel
import jg.coursework.customheroesapp.ui.theme.CustomHeroesOrange
import jg.coursework.customheroesapp.util.Constant
import jg.coursework.customheroesapp.util.Constant.minioClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun FigureCard(figureModel: FigureModel, navigateToFigureDetail: (FigureModel) -> Unit) {
    val context = LocalContext.current as Activity
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(key1 = Unit) {
        withContext(Dispatchers.IO) {
            val image = minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(Constant.BUCKET_NAME)
                    .`object`(figureModel.sourcePath + "/preview.jpg")
                    .build()
            )
            val decodedBitmap = BitmapFactory.decodeStream(image)
            bitmap.value = decodedBitmap
        }
    }

    Surface(shape = RoundedCornerShape(10),
        elevation = 3.dp,
        modifier = Modifier.padding(bottom = 5.dp)
            .clickable { navigateToFigureDetail(figureModel) }) {
        Column(Modifier.fillMaxHeight()) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                if (bitmap.value != null) {
                    Image(
                        bitmap = bitmap.value!!.asImageBitmap(),
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth(),
                        contentDescription = "xd",
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.TopCenter
                    )
                } else {
                    // Placeholder image or loading indicator while the actual image is being loaded
                    Box(
                        modifier = Modifier
                            .height(150.dp)
                            .fillMaxWidth()
                            .background(Color.Gray)
                    )
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(figureModel.title, style = MaterialTheme.typography.h4)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                for (tag in figureModel.tags) {
                    Text(tag.title + ";", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    figureModel.price.toString() + "₽",
                    style = MaterialTheme.typography.h4,
                    fontSize = 20.sp
                )
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                CustomHeroesButton(
                    text = "Подробнее",
                    backgroundColor = CustomHeroesOrange,
                    contentColor = Color.White,
                    onButtonClick = {navigateToFigureDetail(figureModel)},
                    isLoading = false,
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
            }
        }
    }
}
