package com.personal.requestmobility.dashboards.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.formas.MA_Avatar
import com.personal.requestmobility.core.composables.labels.MA_LabelMini
import com.personal.requestmobility.core.composables.labels.MA_LabelNegrita
import com.personal.requestmobility.core.composables.layouts.MA_Columnas
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.dashboards.ui.entidades.DashboardUI
import com.personal.requestmobility.paneles.ui.componente.MA_InfoPanel


@Composable
fun MA_DashboardItem(dashboardUI: DashboardUI, navegacion: (EventosNavegacion) -> Unit) {
	Column {
		Row(
				modifier = Modifier
					.fillMaxWidth()
					.clickable { navegacion(EventosNavegacion.Cargar(dashboardUI.id)) }
					.padding(16.dp),
				verticalAlignment = Alignment.Top
		   ) {
			// Podrías añadir un icono aquí si quisieras
			// Icon(Icons.Filled.Dashboard, contentDescription = "Icono Dashboard", modifier = Modifier.size(40.dp))
			//
			
			MA_Avatar(dashboardUI.nombre)
			
			Spacer(modifier = Modifier.width(8.dp))
			Column(modifier = Modifier) {
				
				MA_LabelNegrita(valor = dashboardUI.nombre,)
				MA_LabelMini(valor =dashboardUI.descripcion, )
				
				
				
				
				
				
				/*Row {
					dashboardUI.listaPaneles.filter { it.seleccionado }.forEach {
						
						Box(modifier = Modifier
							.padding(4.dp)
							) {
							MA_InfoPanel(panel = it, mostrarNombre = true)
						}
					}
					
				
				}*/
				
			
				
			}
		}
		
		MA_Columnas(modifier = Modifier.sizeIn(minHeight = 20.dp, maxHeight = 250.dp), columnas = 3, data = dashboardUI.listaPaneles.filter { it.seleccionado }) {
			Box(modifier = Modifier
					//.padding(4.dp)
			   ) {
				MA_InfoPanel(panel = it, mostrarNombre = true)
			}
			
		}
		HorizontalDivider()
		// Podrías añadir un icono de "chevron_right" aquí si quisieras
		// Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Ver detalle")
	}
	
	
}
