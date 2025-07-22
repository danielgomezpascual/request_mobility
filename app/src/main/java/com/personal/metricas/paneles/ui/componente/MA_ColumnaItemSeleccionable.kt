package com.personal.metricas.paneles.ui.componente

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.composables.tabla.Columnas

@Composable
fun MA_ColumnaItemSeleccionable(columna: Columnas){
    Column(modifier = Modifier.padding(8.dp)) {
        Row() {
            Icon(imageVector = Icons.Default.TextFields, contentDescription = "")
            Spacer(modifier = Modifier.padding(5.dp))
            MA_LabelNormal(valor = "${columna.nombre} (${columna.posicion})")
        }
    }
}