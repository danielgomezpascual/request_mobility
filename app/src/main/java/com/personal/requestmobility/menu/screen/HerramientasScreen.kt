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
import com.personal.requestmobility.core.composables.imagenes.MA_ImagenDrawable
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
                    Row (modifier = Modifier.fillMaxWidth().padding(15.dp),
                        horizontalArrangement =  Arrangement.Start,
                         verticalAlignment = Alignment.CenterVertically) {
                        //MA_Avatar(Features.Dashboard().texto)
                        MA_ImagenDrawable(TituloScreen.DashboardLista.icono)
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
                    Row (modifier = Modifier.fillMaxWidth().padding(15.dp),
                         horizontalArrangement =  Arrangement.Start,
                         verticalAlignment = Alignment.CenterVertically) {

                        MA_ImagenDrawable(TituloScreen.Paneles.icono)
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
                    Row (modifier = Modifier.fillMaxWidth().padding(15.dp),
                         horizontalArrangement =  Arrangement.Start,
                         verticalAlignment = Alignment.CenterVertically) {

                        MA_ImagenDrawable(TituloScreen.Kpi.icono)
                        MA_LabelNormal(
                            modifier = Modifier.padding(2.dp),
                            valor = Features.Kpi().texto
                        )
                    }
                }

            }


        }
    )

}