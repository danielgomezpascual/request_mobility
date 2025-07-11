package com.personal.requestmobility.dashboards.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.formas.MA_Avatar
import com.personal.requestmobility.core.composables.labels.MA_LabelEtiqueta
import com.personal.requestmobility.core.composables.labels.MA_LabelMini
import com.personal.requestmobility.core.composables.labels.MA_LabelNormal
import com.personal.requestmobility.core.utils.if3
import com.personal.requestmobility.paneles.ui.componente.MA_InfoPanel
import com.personal.requestmobility.paneles.ui.entidades.PanelUI

@Composable
fun SeleccionPanelItemDashboard(panelUI: PanelUI,
                                onClickItem: (PanelUI) -> Unit) {

    var modifier: Modifier = Modifier
    val transparencia = if3 (panelUI.seleccionado, 1f, 0.2f)

    Column(verticalArrangement = Arrangement.Center,

        modifier = modifier
            //.fillMaxWidth()
            .clickable { onClickItem(panelUI) }
            .padding(8.dp)
            .alpha(transparencia)


        ) {


        // Nombre y detalles
        Row() {

            Column (horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                MA_Avatar(panelUI.titulo)
                MA_InfoPanel(panelUI)
            }

            Spacer(modifier = Modifier.padding(8.dp))
            Column {
                MA_LabelEtiqueta(valor = panelUI.titulo)
                MA_LabelMini(valor = "${panelUI.descripcion} ")

            }
            Spacer(modifier = Modifier.padding(8.dp))

        }

        HorizontalDivider()

    }




}