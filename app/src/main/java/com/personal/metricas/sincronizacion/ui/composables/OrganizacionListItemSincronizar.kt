package com.personal.metricas.sincronizacion.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.metricas.core.composables.labels.MA_LabelMini
import com.personal.metricas.core.composables.labels.MA_LabelNegrita
import com.personal.metricas.sincronizacion.ui.entidades.OrganizacionesSincronizarUI

@Composable
fun OrganizacionListItemSincronizar(
    organizacionUI: OrganizacionesSincronizarUI,
    onClickItem: (OrganizacionesSincronizarUI) -> Unit,
) {
    val backgroundColor = if (organizacionUI.seleccionado) Color(0xFFE1F5FE) else Color.White
    val contentColor = if (organizacionUI.seleccionado) Color(0xFF01579B) else Color.Black
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable { onClickItem(organizacionUI) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            
            // Iconografía con letras
            val colors = listOf(
                Color(0xFFFFCDD2), // Red 100
                Color(0xFFF8BBD0), // Pink 100
                Color(0xFFE1BEE7), // Purple 100
                Color(0xFFD1C4E9), // Deep Purple 100
                Color(0xFFC5CAE9), // Indigo 100
                Color(0xFFBBDEFB), // Blue 100
                Color(0xFFB2EBF2), // Cyan 100
                Color(0xFFB2DFDB), // Teal 100
                Color(0xFFC8E6C9), // Green 100
                Color(0xFFF0F4C3), // Lime 100
                Color(0xFFFFF9C4), // Yellow 100
                Color(0xFFFFE0B2), // Orange 100
                Color(0xFFD7CCC8)  // Brown 100
            )
            val iconColor = colors[kotlin.math.abs(organizacionUI.organizationCode.hashCode()) % colors.size]

            Box(
                modifier = Modifier
                    .size(40.dp) // Más pequeño
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp)) // Cuadrado con bordes redondeados
                    .background(iconColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = organizacionUI.organizationCode.take(2).uppercase(),
                    color = Color.DarkGray, // Texto oscuro para contrastar con los fondos claros
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Nombre y detalles
            Column {
                MA_LabelNegrita(
                    valor = "${organizacionUI.organizationId} - ${organizacionUI.organizationCode}",
                    color = contentColor
                )
                MA_LabelMini(
                    valor = "${organizacionUI.organizationName}", 
                    size = 14.sp
                )
            }
        }

        HorizontalDivider(
            color = Color.LightGray.copy(alpha = 0.3f),
            thickness = 1.dp
        )
    }
}
