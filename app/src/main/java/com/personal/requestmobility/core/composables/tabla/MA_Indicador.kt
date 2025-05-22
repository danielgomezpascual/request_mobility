package com.personal.requestmobility.core.composables.tabla

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.formas.MA_Circulo

@Composable
fun MA_Indicador(modifier: Modifier = Modifier, color: Color, celda: @Composable () -> Unit) {
    Row(modifier = modifier) {
        MA_Circulo(color = color, size = 30.dp)
        celda()
    }

}