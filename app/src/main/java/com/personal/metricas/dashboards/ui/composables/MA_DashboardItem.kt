package com.personal.metricas.dashboards.ui.composables


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HdrAuto
import androidx.compose.material.icons.filled.Stars
import androidx.compose.runtime.Composable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.formas.MA_Avatar
import com.personal.metricas.core.composables.imagenes.MA_Icono
import com.personal.metricas.core.composables.labels.MA_LabelMini
import com.personal.metricas.core.composables.labels.MA_LabelNegrita
import com.personal.metricas.core.composables.layouts.MA_Columnas
import com.personal.metricas.core.composables.listas.MA_Divider
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.dashboards.ui.entidades.DashboardUI
import com.personal.metricas.paneles.ui.componente.MA_InfoPanel


@Composable
fun MA_DashboardItem(dashboardUI: DashboardUI, navegacion: (EventosNavegacion) -> Unit) {
	var mostrarPaneles by remember { mutableStateOf(false) }

	Column(modifier = Modifier.padding(10.dp)) {

		Row(modifier = Modifier
			.fillMaxWidth()
			.clickable { navegacion(EventosNavegacion.Cargar(dashboardUI.id)) }
			.padding(top = 2.dp, start = 6.dp, end = 2.dp, bottom = 0.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			// Podrías añadir un icono aquí si quisieras
			// Icon(Icons.Filled.Dashboard, contentDescription = "Icono Dashboard", modifier = Modifier.size(40.dp))
			//

			MA_Avatar(dashboardUI.nombre, color = Color(dashboardUI.color))


			Spacer(modifier = Modifier.width(8.dp))
			Column(modifier = Modifier.weight(1f)) {


				Row(verticalAlignment = Alignment.CenterVertically) {
					if (dashboardUI.home) MA_Icono(Icons.Default.Stars, Modifier.size(16.dp))
					if (dashboardUI.autogenerado) MA_Icono(Icons.Default.HdrAuto, Modifier.size(16.dp))
					MA_LabelNegrita(valor = dashboardUI.nombre)
				}


				MA_LabelMini(valor = dashboardUI.descripcion)


			}
			IconButton(onClick = { mostrarPaneles = !mostrarPaneles }) {
				Icon(
					imageVector = if (mostrarPaneles) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
					contentDescription = if (mostrarPaneles) "Ocultar paneles" else "Mostrar paneles",
					tint = Color.Gray
				)
			}
		}

		AnimatedVisibility(visible = mostrarPaneles) {
			val m = Modifier.sizeIn(minHeight = 0.dp, maxHeight = 250.dp)
			MA_Columnas(
				modifier = m,
				columnas = 3,
				data = dashboardUI.listaPaneles.filter { it.seleccionado }) {
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier.background(color = Color(233, 244, 255, 255))
					//.padding(4.dp)
				) {
					//MA_InfoPanelVertical(modifier = m, panel = it, mostrarNombre = true)
					MA_InfoPanel(
						panel = it,
						mostrarNombre = true,
						modifier = Modifier.fillMaxWidth().height(100.dp)
					)
				}

			}
		}



		MA_Divider()


	}


}
