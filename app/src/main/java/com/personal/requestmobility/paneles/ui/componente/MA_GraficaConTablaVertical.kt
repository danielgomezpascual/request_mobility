package com.personal.requestmobility.paneles.ui.componente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.componentes.MA_Marco
import com.personal.requestmobility.core.composables.labels.MA_LabelMini
import com.personal.requestmobility.paneles.domain.entidades.PanelConfiguracion

@Composable
fun MA_GraficaConTablaVertical(modifier: Modifier = Modifier.Companion,
                               panelConfiguracion: PanelConfiguracion = PanelConfiguracion(),
                               grafica: @Composable () -> Unit,
                               tabla: @Composable () -> Unit) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {

        val m = Modifier.Companion
            .width(panelConfiguracion.width)
            .height(panelConfiguracion.height)

        MA_Marco(titulo = panelConfiguracion.titulo, modifier = m, componente = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                MA_LabelMini(panelConfiguracion.descripcion)



                if (panelConfiguracion.mostrarGrafica) {
                    Box(
                        modifier = Modifier.Companion
                            .fillMaxWidth()
                            .fillMaxHeight(panelConfiguracion.espacioGrafica)
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
                            .fillMaxSize()

                    ) {
                        tabla()
                    }
                }
            }

        })
    }
}