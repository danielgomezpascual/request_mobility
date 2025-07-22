package com.personal.metricas.core.composables.checks


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.labels.MA_LabelEtiqueta

@Composable
fun MA_SwitchNormal(valor: Boolean,
                    titulo: String,
                    icono: ImageVector,
                    modifier: Modifier = Modifier.Companion,
                    onValueChange: (Boolean) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            // .fillMaxWidth()
            .padding(8.dp)
    ) {

        MA_LabelEtiqueta(titulo)

        Switch(
            thumbContent = {
                Icon(
                    imageVector = icono,
                    contentDescription = "", // La descripción de contenido se maneja en el Column clickeable
                    modifier = Modifier.size(24.dp) // Tamaño estándar para iconos en Material 3
                )
            },
            checked = valor,
            onCheckedChange = { onValueChange(it) },
            enabled = true
        )

    }
}