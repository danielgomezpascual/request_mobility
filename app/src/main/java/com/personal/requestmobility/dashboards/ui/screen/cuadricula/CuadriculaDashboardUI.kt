package com.personal.requestmobility.dashboards.ui.screen.cuadricula

import MA_IconBottom
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.botones.MA_BotonPrincipal
import com.personal.requestmobility.core.composables.layouts.MA_2Columnas
import com.personal.requestmobility.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.core.screen.ErrorScreen
import com.personal.requestmobility.core.screen.LoadingScreen
import com.personal.requestmobility.menu.Features
import org.koin.androidx.compose.koinViewModel

@Composable
fun CuadriculDashboardUI(
    viewModel: CuadriculaDashboardVM = koinViewModel(),
    navegacion: (EventosNavegacion) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvento(CuadriculaDashboardVM.Eventos.Cargar)
    }

    when (val state = uiState) { // Renombrado uiState a state para claridad en el when
        is CuadriculaDashboardVM.UIState.Error -> ErrorScreen(state.mensaje) // Asume ErrorScreen(mensaje: String)
        is CuadriculaDashboardVM.UIState.Loading -> LoadingScreen(state.mensaje) // Asume LoadingScreen(mensaje: String)
        is CuadriculaDashboardVM.UIState.Success -> SuccessCuadriculaDashboard( // Nombre corregido
            viewModel = viewModel,
            uiState = state,
            navegacion = navegacion
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessCuadriculaDashboard(
    viewModel: CuadriculaDashboardVM,
    uiState: CuadriculaDashboardVM.UIState.Success,
    navegacion: (EventosNavegacion) -> Unit
) {


    MA_ScaffoldGenerico(
        titulo = "Dashboards",
        navegacion = {},
        contenidoBottomBar = { },
        contenido = {}
    )




    MA_ScaffoldGenerico(
        titulo = "Dashboards", // Título adaptado
        navegacion = { navegacion(EventosNavegacion.MenuApp) }, // Para el icono de navegación del TopAppBar
        contenidoBottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Bottom

                ) {

                    MA_IconBottom(
                        modifier = Modifier.weight(1f),
                        icon = Features.Menu().icono,
                        labelText = Features.Menu().texto,
                        onClick = { navegacion(EventosNavegacion.MenuApp) }
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    MA_IconBottom(
                        modifier = Modifier.weight(1f),
                        icon = Features.Dashboard().icono,
                        labelText = Features.Dashboard().texto,
                        color = Features.Nuevo().color,
                        onClick = { navegacion(EventosNavegacion.NuevoDashboard) }
                    )


                }


            }
        },
        contenido = {
            Column(
                modifier = Modifier
                    .fillMaxWidth() // fillMaxWidth para la columna principal
                    .padding(16.dp) // Padding general del contenido como en el ejemplo
            ) {


                MA_2Columnas(uiState.lista) { item ->


                    Column {
                        MA_BotonPrincipal (
                         /*   icon =
                                Icons.Default.DashboardCustomize,*/
                            texto = item.nombre,
                            onClick = { navegacion(EventosNavegacion.VisualizadorDashboard(item.id)) })


                        /*Card(
                            modifier = Modifier
                                .clickable {
                                    navegacion(EventosNavegacion.VisualizadorDashboard(item.id))
                                }
                                .padding(4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Text(
                                text = item.nombre,
                                modifier = Modifier.padding(16.dp)
                            )
                        }*/
                    }


                }
            }

        })
}