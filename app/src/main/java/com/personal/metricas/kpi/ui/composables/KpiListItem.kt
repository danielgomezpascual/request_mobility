package com.personal.metricas.kpi.ui.composables


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HdrAuto
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.formas.MA_Avatar
import com.personal.metricas.core.composables.imagenes.MA_Icono
import com.personal.metricas.core.composables.labels.MA_LabelMini
import com.personal.metricas.core.composables.labels.MA_LabelNegrita
import com.personal.metricas.core.composables.listas.MA_Divider
import com.personal.metricas.kpi.ui.entidades.KpiUI

@Composable
fun KpiListItem(
	kpiUI: KpiUI,
	onClickItem: (KpiUI) -> Unit,
) {

	Column {


		Row(modifier = Modifier.Companion
			.fillMaxWidth()
			.clickable(onClick = { onClickItem(kpiUI) }/* Manejar clic en el usuario  viewModel.abrirUsuario(usuario)*/)
			.padding(10.dp), verticalAlignment = Alignment.Companion.CenterVertically) {

			//  MA_ImagenDrawable(imagen = R.drawable.database, s = 26.dp)
			MA_Avatar(kpiUI.titulo)
			Spacer(modifier = Modifier.padding(4.dp))

			Column {
				Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
					if (kpiUI.autogenerado) MA_Icono(Icons.Default.HdrAuto, Modifier.size(16.dp))
					MA_Spacer(Modifier.padding(3.dp))
					MA_LabelNegrita("${kpiUI.titulo}")
				}
				Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
					MA_Avatar("", color = kpiUI.dameColorDinamico(), size = 10.dp)
					Spacer(modifier = Modifier.padding(3.dp))
					MA_LabelMini("${kpiUI.descripcion}")
				}


			}
		}

		MA_Divider()

	}
}