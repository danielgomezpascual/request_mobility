package com.personal.metricas.paneles.ui.componente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.componentes.MA_Marco
import com.personal.metricas.core.composables.labels.MA_LabelMini
import com.personal.metricas.paneles.domain.entidades.PanelConfiguracion

@Composable
fun MA_GraficaConTablaHorizontal(modifier: Modifier = Modifier.Companion,
                                 panelConfiguracion: PanelConfiguracion = PanelConfiguracion(),
                                 grafica: @Composable () -> Unit,
                                 tabla: @Composable () -> Unit) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {

        val m = Modifier.Companion
            .width(Dp(panelConfiguracion.width.toFloat()))
            .height(Dp(panelConfiguracion.height.toFloat()))

        MA_Marco(titulo = panelConfiguracion.titulo, modifier = m, componente = {

            MA_LabelMini(panelConfiguracion.descripcion)

            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Companion.CenterVertically) {
                if (panelConfiguracion.mostrarGrafica) {
                    Box(
                        modifier = Modifier.Companion
                            .fillMaxWidth(panelConfiguracion.espacioGrafica.toFloat()/100.0f)
                    ) {
                        grafica()
                    }
                }
                if (panelConfiguracion.mostrarTabla) {

                    var modifier: Modifier = Modifier.Companion
                    modifier = if (panelConfiguracion.ocuparTodoEspacio) {
                        modifier.padding(PaddingValues(0.dp, 0.dp))
                    } else {
                        modifier.padding(PaddingValues(60.dp, 15.dp))
                    }


                    Box(
                        modifier = modifier
                            .fillMaxWidth(panelConfiguracion.espacioTabla.toFloat()/100.0f)
                    ) {
                        tabla()
                    }
                }
            }

        })
    }
}