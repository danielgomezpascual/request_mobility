package com.personal.metricas.core.composables.componentes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.metricas.dashboards.ui.entidades.Etiquetas

@Composable
fun MA_FiltroFecha(etiqueta: Etiquetas, onClick: () -> Unit) {
	val isSelected = etiqueta.seleccionada
	
	Surface(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 4.dp)
			.clickable(onClick = onClick),
		shape = RoundedCornerShape(12.dp),
		color = if (isSelected) Color(0xFF2563EB) else Color(0xFFF3F4F6),
		tonalElevation = if (isSelected) 2.dp else 0.dp
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp, vertical = 14.dp),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.Start
			) {
				Icon(
					imageVector = Icons.Default.DateRange,
					contentDescription = null,
					tint = if (isSelected) Color.White else Color(0xFF6B7280),
					modifier = Modifier.padding(end = 12.dp)
				)
				
				Text(
					text = etiqueta.etiqueta,
					style = MaterialTheme.typography.bodyMedium,
					fontSize = 15.sp,
					fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
					color = if (isSelected) Color.White else Color(0xFF1F2937)
				)
			}
		}
	}
}
