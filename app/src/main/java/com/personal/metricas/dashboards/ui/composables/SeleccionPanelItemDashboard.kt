package com.personal.metricas.dashboards.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.formas.MA_Avatar
import com.personal.metricas.core.composables.labels.MA_LabelEtiqueta
import com.personal.metricas.core.composables.labels.MA_LabelMini
import com.personal.metricas.core.composables.tabla.Columnas
import com.personal.metricas.core.utils.if3
import com.personal.metricas.paneles.ui.componente.MA_InfoPanel
import com.personal.metricas.paneles.ui.entidades.PanelUI

@Composable
fun SeleccionPanelItemDashboard(panelUI: PanelUI,
                                columnasOrigenDatos: List<Columnas>,
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

        if (panelUI.esDinamico()) {
            val stateScroll = rememberScrollState()
            Row(modifier = Modifier.horizontalScroll(stateScroll).fillMaxWidth().background(color = Color(232, 234, 246, 255))) {

                panelUI.kpi.parametros.ps.forEach { parametro ->
                    //parametrosDashboard.ps.contains{ it.key.equals(parametro.key) }

                    //App.log.lista(parametrosDashboard.ps)
                    val encontrado = if3( parametro.fijo || (columnasOrigenDatos.find { it.nombre.lowercase().equals(parametro.key.lowercase()) } != null), true, false)


                    MA_LabelMini("$${parametro.key}", color = if3(encontrado, Color.DarkGray, Color.Red))
                }
            }
        }

        HorizontalDivider()

    }




}