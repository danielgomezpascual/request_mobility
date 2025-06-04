package com.personal.requestmobility.dashboards.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.checks.MA_SwitchNormal
import com.personal.requestmobility.core.composables.formas.MA_Avatar
import com.personal.requestmobility.core.composables.labels.MA_LabelEtiqueta
import com.personal.requestmobility.core.composables.labels.MA_LabelNormal
import com.personal.requestmobility.core.utils.if3
import com.personal.requestmobility.dashboards.ui.entidades.SeleccionPanelUI
import com.personal.requestmobility.paneles.domain.entidades.Panel
import com.personal.requestmobility.paneles.ui.entidades.PanelUI

@Composable
fun SeleccionPanelItem(panelUI: PanelUI,
                       onClickItem: (PanelUI) -> Unit) {

    var modifier: Modifier = Modifier


    val transparencia = if3 (panelUI.seleccionado, 1f, 0.2f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClickItem(panelUI) }
            .padding(16.dp)
            .alpha(transparencia),

        ) {


        // Nombre y detalles
        Row() {

            MA_Avatar(panelUI.titulo)
            Spacer(modifier = Modifier.padding(8.dp))
            Column {
                MA_LabelEtiqueta(valor = panelUI.titulo)
                MA_LabelNormal(valor = panelUI.descripcion)
            }
            Spacer(modifier = Modifier.padding(8.dp))
           /* MA_SwitchNormal(
                valor = panelUI.seleccionado,
                titulo = "",
                icono = Icons.Default.CheckCircle,
                onValueChange = { res ->
                    onClickItem(panelUI)
                }

            )*/
        }









        Spacer(modifier = Modifier.Companion.width(16.dp))

    }

    HorizontalDivider()


}