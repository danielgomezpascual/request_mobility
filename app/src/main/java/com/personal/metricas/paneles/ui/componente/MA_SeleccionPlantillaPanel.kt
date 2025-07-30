package com.personal.metricas.paneles.ui.componente

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.imagenes.MA_ImagenDrawable
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.paneles.domain.entidades.PanelTipoGrafica
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel

@Composable
fun MA_SeleccionPlantillaPanel(plantilla: PlantillasPanel) {
	Box(
			
			modifier =
				Modifier
					.padding(3.dp)
				
					
					.height(40.dp)
	   
	   ) {
		

		Row (verticalAlignment = Alignment.CenterVertically) {
			MA_ImagenDrawable(plantilla.icono)
			MA_LabelNormal(valor = plantilla.nombre )
			
		}
		
	}
}