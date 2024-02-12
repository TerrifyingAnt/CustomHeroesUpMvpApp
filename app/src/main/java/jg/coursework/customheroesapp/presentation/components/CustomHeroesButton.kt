package jg.coursework.customheroesapp.presentation.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import jg.coursework.customheroesapp.ui.theme.CustomHeroesGray
import jg.coursework.customheroesapp.ui.theme.CustomHeroesOrange

@Composable
fun CustomHeroesButton(
    text: String,
    backgroundColor: Color,
    contentColor: Color,
    enabled: Boolean = true,
    onButtonClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier,
        onClick = {
            onButtonClick()
        },
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = contentColor,
            disabledContentColor = contentColor,
            containerColor = backgroundColor,
            disabledContainerColor = backgroundColor
        ),
        enabled = enabled
    ) {
        if(isLoading) {
            CircularProgressIndicator(
                color = contentColor,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthButtonPreview() {
    CustomHeroesButton(
        text = "Войти",
        backgroundColor = CustomHeroesOrange,
        contentColor = CustomHeroesGray,
        onButtonClick = { /*TODO*/ },
        isLoading = false,
        modifier = Modifier.fillMaxWidth()
    )
}