package com.personal.metricas.endpoints.ui.composables


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.formas.MA_Avatar
import com.personal.metricas.core.composables.labels.MA_LabelMini
import com.personal.metricas.core.composables.labels.MA_LabelNegrita
import com.personal.metricas.core.composables.listas.MA_Divider
import com.personal.metricas.endpoints.ui.entidades.EndPointUI

@Composable
fun EndPointListItem(
	endpointUI: EndPointUI,
	onClickItem: (EndPointUI) -> Unit,
) {

	Column {


		Row(modifier = Modifier.Companion
			.fillMaxWidth()
			.clickable(onClick = { onClickItem(endpointUI) }/* Manejar clic en el usuario  viewModel.abrirUsuario(usuario)*/)
			.padding(10.dp), verticalAlignment = Alignment.Companion.CenterVertically) {

			//  MA_ImagenDrawable(imagen = R.drawable.database, s = 26.dp)
			MA_Avatar(endpointUI.nombre)
			Spacer(modifier = Modifier.padding(4.dp))
			Column {

				MA_LabelNegrita("${endpointUI.nombre}")
				MA_Spacer()
				MA_LabelMini("${endpointUI.descripcion}")


			}
		}

		MA_Divider()

	}
}