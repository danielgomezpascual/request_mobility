package com.personal.metricas.core.composables.botones

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.imagenes.MA_Icono

@Composable
fun MA_BotonSecundarioSinBorde(texto: String, icono: ImageVector? = null, modifier: Modifier = Modifier, onClick: () -> Unit) {

        Row( modifier = modifier.clickable(enabled = true, onClick = onClick),
             verticalAlignment = Alignment.CenterVertically) {
            if (icono != null) {
                MA_Icono(icono = icono,
                         color = MaterialTheme.colorScheme.inversePrimary)
            }
            Text(text = texto)
        }


}