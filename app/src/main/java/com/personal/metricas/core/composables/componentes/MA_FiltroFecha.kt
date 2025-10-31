package com.personal.metricas.core.composables.componentes

import MA_IconBottom
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.utils.if3
import com.personal.metricas.dashboards.ui.entidades.Etiquetas
import com.personal.metricas.menu.Features

@Composable
fun MA_FiltroFecha(etiqueta: Etiquetas, onClick: () -> Unit) {


	Row {
		//	val s: String = Parametros.reemplazar(panelData.panel.titulo, fila.toParametros(), fila.toParametros())

		val color = if3(etiqueta.seleccionada, Color(0, 77, 64, 255), Features.Dashboard().color)




		MA_IconBottom(
			icon = Features.Dashboard().icono,
			labelText = etiqueta.etiqueta,
			color = color, onClick = onClick

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
