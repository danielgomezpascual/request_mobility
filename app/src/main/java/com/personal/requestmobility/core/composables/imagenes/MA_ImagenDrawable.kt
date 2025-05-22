package com.personal.requestmobility.core.composables.imagenes

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.R


@Preview
@Composable
fun TestImagen() {
    MA_ImagenDrawable(R.drawable.ic_launcher_foreground)
}

@Composable
fun MA_ImagenDrawable(imagen: Int, s: Dp = 48.dp) {
    Image(
        painter = painterResource(id = imagen),
        contentDescription = "Descripción de tu imagen"
    )
}