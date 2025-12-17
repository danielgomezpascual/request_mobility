package com.personal.metricas.dashboards.ui.composables

import MA_IconBottom
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import com.personal.metricas.core.composables.formas.MA_Avatar
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
import com.personal.metricas.core.composables.labels.MA_LabelNegrita
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.core.utils._toJson
import com.personal.metricas.core.utils.if3
import com.personal.metricas.dashboards.navegacion.goto
import com.personal.metricas.dashboards.ui.entidades.Etiquetas
import com.personal.metricas.menu.Features

@Composable
fun MA_EtiquetaItem(etiqueta: Etiquetas, onClick : () -> Unit) {

	val mainColor = Features.Dashboard().color
	val backgroundColor = if (etiqueta.seleccionada) mainColor.copy(alpha = 0.3f) else mainColor.copy(alpha = 0.1f)

	MA_Card(
		elevacion = 0.dp,
		color = backgroundColor,
		modifier = Modifier
			.padding(2.dp)
			.clickable(onClick = onClick)
	) {
		Column(
			modifier = Modifier.padding(6.dp),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			MA_Avatar(etiqueta.etiqueta, color = mainColor, size = 25.dp, fontSize = 14.sp)

			Spacer(modifier = Modifier.size(4.dp))
			
			MA_LabelMini(
				alineacion = TextAlign.Center,
				modifier = Modifier.padding(1.dp),
				valor = etiqueta.etiqueta
			)
		}
	}
}

