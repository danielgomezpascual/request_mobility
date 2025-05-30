package com.personal.requestmobility.core.composables.formas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Función para generar un color aleatorio para el avatar
private fun getRandomColor(): Color {
    val red = (0..255).random()
    val green = (0..255).random()
    val blue = (0..255).random()
    return Color(red, green, blue)
}

@Preview
@Composable
fun Test_MA_Avatar(){
    MA_Avatar("Pruebas")
}

@Composable
fun MA_Avatar(texto: String , size : Dp = 50.dp, color : Color = getRandomColor()){
    // Avatar
    Box(
        modifier = Modifier.Companion
            .size(size)
            .clip(CircleShape)
            .background(getRandomColor()),
        contentAlignment = Alignment.Companion.Center
    ) {
        // Inicial del usuario, ajusta según la información disponible
        Text(
            text = texto.take(1).uppercase(),
            style = MaterialTheme.typography.titleLarge,
            color = Color.Companion.White
        )

    }
}