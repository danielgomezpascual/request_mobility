package com.personal.metricas.organizaciones.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cyclone
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.checks.MA_CheckBoxNormal
import com.personal.metricas.core.composables.formas.MA_Avatar
import com.personal.metricas.core.composables.imagenes.MA_Icono
import com.personal.metricas.core.composables.labels.MA_LabelMini
import com.personal.metricas.core.composables.labels.MA_LabelNegrita
import com.personal.metricas.organizaciones.ui.entidades.OrganizacionUI
import com.personal.metricas.sincronizacion.ui.entidades.OrganizacionesSincronizarUI

@Composable
fun OrganizacionListItem(
	organizacionUI: OrganizacionUI,
	onClickItem: (OrganizacionUI) -> Unit

) {


	Column {


		Row(
			modifier = Modifier.Companion
				.fillMaxWidth()
				.clickable { onClickItem(organizacionUI)/* Manejar clic en el usuario  viewModel.abrirUsuario(usuario)*/ }
				.padding(6.dp),
			verticalAlignment = Alignment.Companion.CenterVertically
		) {

			MA_Avatar(organizacionUI.organizationCode)
			Spacer(modifier = Modifier.Companion.width(8.dp))

			// Nombre y detalles
			Column {
				Row(verticalAlignment = Alignment.CenterVertically){

					MA_LabelNegrita(valor = "${organizacionUI.organizationCode} (${organizacionUI.organizationId})")

					if (organizacionUI.activo){
						MA_Spacer()
						MA_Icono(icono =  Icons.Default.Recycling, color = Color(0, 77, 64, 255))
					}

					if (!organizacionUI.horas.isNullOrEmpty()){
						MA_Spacer()
						MA_Icono(icono =  Icons.Default.Timer, color = Color(255, 111, 0, 255), )
					}
				}

				MA_LabelMini(valor = "${organizacionUI.organizationName}", size = 12.sp)


			}
		}

		HorizontalDivider()


	}
}