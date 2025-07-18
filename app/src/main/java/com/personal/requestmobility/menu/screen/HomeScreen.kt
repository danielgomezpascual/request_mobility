package com.personal.requestmobility.menu.screen

import MA_IconBottom
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.personal.requestmobility.core.composables.card.MA_Card
import com.personal.requestmobility.core.composables.componentes.TituloScreen
import com.personal.requestmobility.core.composables.listas.MA_NoData
import com.personal.requestmobility.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.core.screen.ErrorScreen
import com.personal.requestmobility.core.screen.LoadingScreen
import com.personal.requestmobility.core.utils.Parametros
import com.personal.requestmobility.inicializador.domain.InicializadorManager
import com.personal.requestmobility.menu.Features
import com.personal.requestmobility.menu.screen.HomeVM.UIState
import com.personal.requestmobility.paneles.domain.entidades.PanelData
import com.personal.requestmobility.paneles.ui.componente.MA_Panel
import org.koin.androidx.compose.koinViewModel


@Composable
fun HomeScreen(
    viewModel: HomeVM = koinViewModel(),
    navegacion: (EventosNavegacion) -> Unit
) {

    LaunchedEffect(Unit) {
        viewModel.onEvent(HomeVM.Eventos.Carga)
    }
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is UIState.Error -> ErrorScreen((uiState as UIState.Error).message)
        UIState.Loading -> LoadingScreen()
        is UIState.Success -> {
            SuccessMenu(viewModel, (uiState as UIState.Success), navegacion)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessMenu(viewModel: HomeVM,
                uiState: UIState.Success,
                navegacion: (EventosNavegacion) -> Unit) {
  /*  Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {*/

        MA_ScaffoldGenerico(
            titulo = "",
            tituloScreen = TituloScreen.Home,
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
                            icon = Features.Sincronizar().icono,
                            labelText = Features.Sincronizar().texto,
                            seleccionado = false,
                            destacado = false,
                            onClick = { navegacion(EventosNavegacion.Sincronizacion) }
                        )



                        MA_IconBottom(
                            //   modifier = Modifier.weight(1f),
                            icon = Features.Cuadriculas().icono,
                            labelText = Features.Cuadriculas().texto,
                            seleccionado = true,
                            destacado = true,
                            onClick = { navegacion(EventosNavegacion.MenuVisualizadorDashboard) }
                        )

                        MA_IconBottom(
                            //   modifier = Modifier.weight(1f),
                            icon = Features.InicializadorMetricas().icono,
                            labelText = Features.InicializadorMetricas().texto,
                            seleccionado = false,
                            destacado = false,
                            onClick = {
                            viewModel.onEvent(HomeVM.Eventos.InicializadorMetricas)
                            }
                        )
                        MA_IconBottom(
                            //   modifier = Modifier.weight(1f),
                            icon = Features.Herramientas().icono,
                            labelText = Features.Herramientas().texto,
                            seleccionado = false,
                            destacado = false,
                            onClick = { navegacion(EventosNavegacion.MenuHerramientas) }
                        )
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
                    }


                }
            },
            contenido = {



                val scroll = rememberScrollState()

                Box(Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.verticalScroll(state = scroll), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        if (uiState.paneles.size > 0){
                            uiState.paneles.forEach { panelUI ->
                                MA_Card {
                                    MA_Panel(panelData = PanelData.fromPanelUI(panelUI, Parametros()))
                                }

                            }
                        }else{
                            MA_NoData()
                        }

                    }
                }


            }
        )


   // }
}

