package com.personal.metricas.dashboards.ui.composables

import MA_IconBottom
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LabelImportant
import androidx.compose.material.icons.filled.NotInterested
import androidx.compose.material.icons.filled.OfflinePin
import androidx.compose.material.icons.filled.Tag
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo
import com.personal.metricas.App
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.composables.imagenes.MA_Icono
import com.personal.metricas.core.composables.labels.MA_LabelEtiqueta
import com.personal.metricas.core.composables.labels.MA_LabelMini
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.composables.labels.MA_Titulo2
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.core.utils._toJson
import com.personal.metricas.core.utils.if3
import com.personal.metricas.dashboards.navegacion.goto
import com.personal.metricas.dashboards.ui.entidades.Etiquetas
import com.personal.metricas.menu.Features

@Composable
fun MA_EtiquetaItem(etiqueta: Etiquetas, onClick : () -> Unit) {

	/*Box(modifier = Modifier.padding(5.dp).border(width = 1.dp, color = Color(234, 234, 234, 100))
	.background( color = Color(251, 233, 231, 255))){
		Row (modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.CenterVertically) {

			if (etiqueta.seleccionada){
				MA_Icono(Icons.Default.Check,  modifier = Modifier.size(12.dp))
			}

			MA_Spacer()
			MA_LabelNormal(etiqueta.etiqueta)

		}
	}*/
	MA_Card(
		elevacion = 3.dp,
		//color = Color(243, 237, 120, 81),
		modifier = Modifier
			.padding(1.dp)
			//.background(color = Color(225, 245, 254, 255))
			/*.clickable {
				goto(EventosNavegacion.VisualizadorDashboard(identificadorDashboard, _toJson(fila.toParametros())),
					 App.navController)
			}*/
	) {
		Row {
		//	val s: String = Parametros.reemplazar(panelData.panel.titulo, fila.toParametros(), fila.toParametros())

			val color = if3 (etiqueta.seleccionada, Color(0, 77, 64, 255), Features.Dashboard().color)




			MA_IconBottom(
				icon = Features.Dashboard().icono,
				labelText = etiqueta.etiqueta,
				color = color, onClick =  onClick

			)



			/*MA_LabelMini(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 2.dp),
				valor = s,
				alineacion = TextAlign.End,
				size = 9.sp,
				fontStyle = FontStyle.Italic
			)*/
		}
	}


}

