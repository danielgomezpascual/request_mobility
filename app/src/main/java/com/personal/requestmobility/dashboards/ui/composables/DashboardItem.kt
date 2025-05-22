package com.personal.requestmobility.dashboards.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.dashboards.ui.entidades.DashboardUI


@Composable
fun DashboardItem(dashboardUI: DashboardUI, navegacion: (EventosNavegacion) -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navegacion(EventosNavegacion.Cargar(dashboardUI.id)) }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Podrías añadir un icono aquí si quisieras
            // Icon(Icons.Filled.Dashboard, contentDescription = "Icono Dashboard", modifier = Modifier.size(40.dp))
            // Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dashboardUI.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = dashboardUI.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            // Podrías añadir un icono de "chevron_right" aquí si quisieras
            // Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Ver detalle")
        }
        HorizontalDivider()
    }
}