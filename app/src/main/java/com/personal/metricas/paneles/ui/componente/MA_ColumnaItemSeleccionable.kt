package com.personal.metricas.paneles.ui.componente

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.labels.MA_LabelMini
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.composables.tabla.Columnas

@Composable
fun MA_ColumnaItemSeleccionable(columna: Columnas){
    Column(modifier = Modifier) {
        Row() {
            MA_IconColumna()
            /*Icon(imageVector = Icons.Default.Dataset, contentDescription = "",
                 tint = Color(231, 153, 57, 255))*/
            MA_Spacer(modifier = Modifier.width(2.dp))
            MA_LabelNormal(valor = "${columna.nombre} [${columna.posicion}]")
        }
    }
}