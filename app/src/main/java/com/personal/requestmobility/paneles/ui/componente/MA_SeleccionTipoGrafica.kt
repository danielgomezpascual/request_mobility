package com.personal.requestmobility.paneles.ui.componente

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.imagenes.MA_ImagenDrawable
import com.personal.requestmobility.core.composables.labels.MA_LabelNormal
import com.personal.requestmobility.paneles.domain.entidades.PanelTipoGrafica

@Composable
fun MA_SeleccionTipoGrafica(panel: PanelTipoGrafica) {
	Box(
			
			modifier =
				Modifier
					.padding(3.dp)
				
					
					.height(36.dp)
	   
	   ) {
		
		var nombre: String = ""
		var icono: Int = 0
		var id: Int = 0
		when (panel) {
			is PanelTipoGrafica.Anillo                 -> {
				
				id = panel.id
				nombre = panel.nombre
				icono = panel.icono
			}
			
			
			is PanelTipoGrafica.BarrasAnchasVerticales -> {
				id = panel.id
				nombre = panel.nombre
				icono = panel.icono
			}
			
			is PanelTipoGrafica.BarrasFinasVerticales  -> {
				id = panel.id
				nombre = panel.nombre
				icono = panel.icono
			}
			
			is PanelTipoGrafica.Circular               -> {
				id = panel.id
				nombre = panel.nombre
				icono = panel.icono
			}
			
			is PanelTipoGrafica.IndicadorHorizontal    -> {
				id = panel.id
				nombre = panel.nombre
				icono = panel.icono
			}
			
			is PanelTipoGrafica.IndicadorVertical      -> {
				id = panel.id
				nombre = panel.nombre
				icono = panel.icono
			}
			
			is PanelTipoGrafica.Lineas                 -> {
				id = panel.id
				nombre = panel.nombre
				icono = panel.icono
			}
		}
		Row(/*modifier = Modifier.fillMaxWidth()*/) {
			MA_ImagenDrawable(icono)
			MA_LabelNormal(valor = nombre)
			
		}
		
	}
}