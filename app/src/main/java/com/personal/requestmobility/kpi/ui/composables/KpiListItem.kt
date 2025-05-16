package com.personal.requestmobility.kpi.ui.composables

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
import com.personal.requestmobility.kpi.ui.entidades.KpiUI

@Composable
fun KpiListItem(kpiUI: KpiUI,
                onClickItem: (KpiUI) -> Unit) {


    Column {


        Row(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .clickable { onClickItem(kpiUI)/* Manejar clic en el usuario  viewModel.abrirUsuario(usuario)*/ }
                .padding(16.dp),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {


            Spacer(modifier = Modifier.Companion.width(16.dp))

            // Nombre y detalles
            Column {
                Text(
                    text = "${kpiUI.titulo}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Companion.Bold
                )
                Text(
                    text = "${kpiUI.descripcion} ",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Companion.Normal
                )


            }
        }

        HorizontalDivider()


    }
}