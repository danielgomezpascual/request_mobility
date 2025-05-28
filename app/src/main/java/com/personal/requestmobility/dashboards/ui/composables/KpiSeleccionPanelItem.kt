package com.personal.requestmobility.dashboards.ui.composables

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.dashboards.ui.entidades.KpiSeleccionPanel
import com.personal.requestmobility.kpi.ui.entidades.KpiUI

@Composable
fun KpiSeleccionPanelItem(kpiSeleccionPanel: KpiSeleccionPanel,
                          onClickItem: (KpiSeleccionPanel) -> Unit) {

    var modifier: Modifier = Modifier

    if (kpiSeleccionPanel.seleccionado) {
        modifier = modifier.background(color = Color.Yellow)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClickItem(kpiSeleccionPanel) }
            .padding(16.dp),

        ) {


        Spacer(modifier = Modifier.Companion.width(16.dp))

        // Nombre y detalles

        Text(
            text = "${kpiSeleccionPanel.seleccionado}",
            //style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Companion.Bold
        )
        Text(
            text = "${kpiSeleccionPanel.titulo}",
            //style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Companion.Bold
        )
        Text(
            text = "${kpiSeleccionPanel.descripcion} ",
            //style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Companion.Normal
        )


    }

    HorizontalDivider()


}