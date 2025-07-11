package com.personal.requestmobility.menu.screen

import MA_IconBottom
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.card.MA_Card
import com.personal.requestmobility.core.composables.componentes.TituloScreen
import com.personal.requestmobility.core.composables.formas.MA_Avatar
import com.personal.requestmobility.core.composables.labels.MA_LabelNormal
import com.personal.requestmobility.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.menu.Features

@Composable
fun HerramientasScreen(navegacion: (EventosNavegacion) -> Unit) {

    MA_ScaffoldGenerico(
        titulo = "",
        tituloScreen = TituloScreen.Herramientas,
        volver = false,
        navegacion = { },
        contenidoBottomBar = {

            BottomAppBar() {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom

                ) {

                    MA_IconBottom(
                        //   modifier = Modifier.weight(1f),
                        icon = Features.Menu().icono,
                        labelText = Features.Menu().texto,
                        seleccionado = false,
                        destacado = false,
                        onClick = { navegacion(EventosNavegacion.MenuApp) }
                    )


                }


            }
        },
        contenido = {

            Column {


                MA_Card(
                    modifier = Modifier

                        .fillMaxWidth()
                        .clickable(
                            enabled = true,
                            onClick = { navegacion(EventosNavegacion.MenuDashboard) })
                ) {
                    Column(modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,  horizontalAlignment = Alignment.CenterHorizontally) {
                        MA_Avatar(Features.Dashboard().texto)
                        MA_LabelNormal(
                            modifier = Modifier.padding(2.dp),
                            valor = Features.Dashboard().texto
                        )
                    }
                }


                MA_Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            enabled = true,
                            onClick = {
                                App.log.d("Clien en panles")

                                navegacion(EventosNavegacion.MenuPaneles) })
                ) {
                    Column(modifier = Modifier.fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally) {
                        MA_Avatar(Features.Paneles().texto)
                        MA_LabelNormal(
                            modifier = Modifier.padding(2.dp),
                            valor = Features.Paneles().texto
                        )
                    }
                }


                MA_Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            enabled = true,
                            onClick = { navegacion(EventosNavegacion.MenuKpis) })
                ) {
                    Column(modifier = Modifier.fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally) {
                        MA_Avatar(Features.Kpi().texto)
                        MA_LabelNormal(
                            modifier = Modifier.padding(2.dp),
                            valor = Features.Kpi().texto
                        )
                    }
                }

            }
            /* MA_IconBottom(
                       //  modifier = Modifier.weight(1f),
                       icon = Features.Dashboard().icono,
                       labelText = Features.Dashboard().texto,
                       onClick = { navegacion(EventosNavegacion.MenuDashboard) }
                   )
                   MA_IconBottom(
                       //   modifier = Modifier.weight(1f),
                       icon = Features.Paneles().icono,
                       labelText = Features.Paneles().texto,
                       onClick = { navegacion(EventosNavegacion.MenuPaneles) }
                   )

                   MA_IconBottom(
                       // modifier = Modifier.weight(1f),
                       icon = Features.Kpi().icono,
                       labelText = Features.Kpi().texto,
                       onClick = { navegacion(EventosNavegacion.MenuKpis) }
                   )*/
            /*val scroll = rememberScrollState()

            Box(Modifier) {
                Column(modifier = Modifier.verticalScroll(state = scroll)) {

                    uiState.paneles.forEach { panelUI ->
                        MA_Panel(panelData = PanelData().fromPanelUI(panelUI))
                    }
                }
            }*/


        }
    )

}