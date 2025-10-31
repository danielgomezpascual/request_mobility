package com.personal.metricas.core.composables.labels

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sin


@Composable
fun MA_Titulo(
    valor: String,
    modifier: Modifier = Modifier.padding(6.dp),
    color: Color = Color.Black,
    fondo: Color = Color.White,
    alineacion: TextAlign = TextAlign.Center,
    icono: Icons? = null,
) {

        Text(
            text = valor, modifier = modifier,
            color = color,
            fontSize =  14.sp,
            textAlign = alineacion
        )

}
