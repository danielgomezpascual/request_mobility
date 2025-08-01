package com.personal.metricas.core.composables.botones

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.personal.metricas.core.composables.imagenes.MA_Icono

@Composable
fun MA_BotonSecundario(texto: String, icono: ImageVector? = null, modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icono != null) {
                MA_Icono(icono = icono,
                         color = MaterialTheme.colorScheme.inversePrimary)
            }
            Text(text = texto)
        }

    }
}