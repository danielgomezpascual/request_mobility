package com.personal.requestmobility.core.composables.tabla

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.formas.Circulo

@Composable
fun Indicador(modifier: Modifier = Modifier, color: Color, celda: @Composable () -> Unit) {
    Row(modifier = modifier) {
        Circulo(color = color, size = 30.dp)
        celda()
    }

}