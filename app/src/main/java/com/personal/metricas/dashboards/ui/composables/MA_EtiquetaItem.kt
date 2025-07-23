package com.personal.metricas.dashboards.ui.composables

import MA_IconBottom
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NotInterested
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.room.util.TableInfo
import com.personal.metricas.App
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.imagenes.MA_Icono
import com.personal.metricas.core.composables.labels.MA_LabelEtiqueta
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.composables.labels.MA_Titulo2
import com.personal.metricas.core.utils.if3
import com.personal.metricas.dashboards.ui.entidades.Etiquetas

@Composable
fun MA_EtiquetaItem(etiqueta: Etiquetas) {

	Box(modifier = Modifier.padding(5.dp).border(width = 1.dp, color = Color(234, 234, 234, 100)).background( color = Color(251, 233, 231, 255))){
		Row (modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.CenterVertically) {

			if (etiqueta.seleccionada){
				MA_Icono(Icons.Default.Check,  modifier = Modifier.size(12.dp))
			}

			MA_Spacer()
			MA_LabelNormal(etiqueta.etiqueta)

		}
	}


}

