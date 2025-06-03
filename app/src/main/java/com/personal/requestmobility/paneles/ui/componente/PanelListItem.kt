package com.personal.requestmobility.paneles.ui.componente

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
import com.personal.requestmobility.core.composables.formas.MA_Avatar
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.paneles.ui.entidades.PanelUI

@Composable
fun PanelListItem(panelUI: PanelUI,
                  onClickItem: (PanelUI) -> Unit) {


    Column {


        Row(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .clickable { onClickItem(panelUI)/* Manejar clic en el usuario  viewModel.abrirUsuario(usuario)*/ }
                .padding(16.dp),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {



            MA_Avatar(panelUI.titulo)

            Spacer(modifier = Modifier.Companion.width(16.dp))

            // Nombre y detalles
            Column {
                Text(
                    text = "${panelUI.titulo}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Companion.Bold
                )
                Text(
                    text = "${panelUI.descripcion} ",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Companion.Normal
                )


            }
        }

        HorizontalDivider()


    }
}