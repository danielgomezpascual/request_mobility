package com.personal.metricas.paneles.ui.componente


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LinearScale
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.imagenes.MA_Icono
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.paneles.domain.entidades.Conector


@Composable
fun MA_ConectorItem(conector: Conector) {
	Column(modifier = Modifier.padding(10.dp)) {

		Row(modifier = Modifier
			.fillMaxWidth()
			.padding(top = 2.dp, start = 6.dp, end = 2.dp, bottom = 0.dp), verticalAlignment = Alignment.Top) {

			MA_Icono(Icons.Default.LinearScale)
			MA_Spacer()
			MA_LabelNormal(conector.descripcion)

		}


	}


}
