package com.personal.requestmobility.core.composables.formas

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Preview(showBackground = true)
@Composable
fun PreviewEstadoCirculo() {
    MA_Circulo(color = Color.Red, 20.dp)
}

@Composable
fun MA_Circulo(color: Color, size: Dp = 50.dp) {

    Box(contentAlignment = Alignment.Center){
        Canvas(modifier = Modifier.width(size)) {

            val canvasWidth = size.value
            val canvasHeight = size.value
            val centerX = canvasWidth / 2
            val centerY = canvasHeight / 2

            val radius = Math.min(centerX, centerY) * 0.8f

            val paint = Paint()

            drawCircle(
                color = color,
                radius = radius,
                center = Offset(centerX, centerY)
            )
        }
    }

}