package com.personal.requestmobility.menu.screen

import MA_IconBottom
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.personal.requestmobility.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.requestmobility.menu.Features
import com.personal.requestmobility.menu.navegacion.Modulos

@Composable
fun ScreenMenu(accion: (Modulos) -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        MA_ScaffoldGenerico(
            titulo = "",
            volver =  false,
            navegacion = { },
            contenidoBottomBar = {

                BottomAppBar() {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom

                    ) {


                        /*MA_IconBottom(
                            //modifier = Modifier.weight(1f),
                            icon = Features.Transacciones().icono,
                            labelText = Features.Transacciones().texto,
                            onClick = { accion(Modulos.Transacciones) }
                        )*/


                        MA_IconBottom(
                            //   modifier = Modifier.weight(1f),
                            icon = Features.Cuadriculas().icono,
                            labelText = Features.Cuadriculas().texto,
                            seleccionado = true,
                            destacado = true,
                            onClick = { accion(Modulos.Cuadricula) }
                        )
                        MA_IconBottom(
                            //  modifier = Modifier.weight(1f),
                            icon = Features.Dashboard().icono,
                            labelText = Features.Dashboard().texto,
                            onClick = { accion(Modulos.Dashboards) }
                        )
                        MA_IconBottom(
                            //   modifier = Modifier.weight(1f),
                            icon = Features.Paneles().icono,
                            labelText = Features.Paneles().texto,
                            onClick = { accion(Modulos.Paneles) }
                        )

                        MA_IconBottom(
                            // modifier = Modifier.weight(1f),
                            icon = Features.Kpi().icono,
                            labelText = Features.Kpi().texto,
                            onClick = { accion(Modulos.Kpis) }
                        )
                    }


                }
            },
            contenido = {
                Text("Lorem dolor ipsum")
            }
        )


    }
}