package com.personal.metricas.paneles.ui.componente

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MA_RowSeccion(conteniddo: @Composable () ->Unit){
    Row(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
conteniddo()
    }
}
