package com.personal.metricas.paneles.ui.componente

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.labels.MA_LabelNormal

@Composable
fun MA_SeleccionColor(color: Color) {
    Box(

        modifier =
            Modifier
                .padding(3.dp)
                .border(width = 1.dp, color = Color.Black)
                .width(36.dp)
                .height(36.dp)
                .background(color = color)
    ) {
        MA_LabelNormal("")
    }
}