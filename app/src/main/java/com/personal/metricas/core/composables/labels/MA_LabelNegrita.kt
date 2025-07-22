package com.personal.metricas.core.composables.labels

import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign



@Composable
fun MA_LabelNegrita(
    valor: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.secondary,
    alineacion : TextAlign = TextAlign.Start,
    icono: Icons? = null,

    ) {
    Text(
        text = valor,
        modifier = modifier,
        color = color,
       fontWeight = FontWeight.Bold,
        textAlign =  alineacion,
        style = MaterialTheme.typography.bodyMedium, )
}