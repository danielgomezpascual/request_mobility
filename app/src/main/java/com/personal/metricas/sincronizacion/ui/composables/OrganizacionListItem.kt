package com.personal.metricas.sincronizacion.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.checks.MA_CheckBoxNormal
import com.personal.metricas.core.composables.formas.MA_Avatar
import com.personal.metricas.sincronizacion.ui.entidades.OrganizacionesSincronizarUI

@Composable
fun OrganizacionListItem(organizacionUI: OrganizacionesSincronizarUI,
                  onClickItem: (OrganizacionesSincronizarUI) -> Unit) {


    Column {


        Row(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .clickable { onClickItem(organizacionUI)/* Manejar clic en el usuario  viewModel.abrirUsuario(usuario)*/ }
                .padding(16.dp),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {

            MA_CheckBoxNormal(valor = organizacionUI.seleccionado, titulo =  "") {
                onClickItem(organizacionUI)
            }

            MA_Avatar(organizacionUI.organizationCode)

            Spacer(modifier = Modifier.Companion.width(8.dp))

            // Nombre y detalles
            Column {
                Text(
                    text = "${organizacionUI.organizationCode}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Companion.Bold
                )
                Text(
                    text = "${organizacionUI.organizationName}  ",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Companion.Normal
                )


            }
        }

        HorizontalDivider()


    }
}