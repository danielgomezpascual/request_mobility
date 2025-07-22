package com.personal.metricas.core.composables.labels

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun MA_LabelExtendido(
    valor: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    extension: @Composable () -> Unit = {},
) {
    Row (modifier = modifier){
        extension()
        Text(valor, color = color, style = MaterialTheme.typography.bodyMedium, modifier = modifier)
    }

}