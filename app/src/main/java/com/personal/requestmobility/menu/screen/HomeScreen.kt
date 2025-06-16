package com.personal.requestmobility.menu.screen

import MA_IconBottom
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.botones.MA_BotonNormal
import com.personal.requestmobility.core.composables.combo.MA_ComboLista
import com.personal.requestmobility.core.composables.dialogos.MA_Dialogo
import com.personal.requestmobility.core.composables.dialogos.ResultadoDialog
import com.personal.requestmobility.core.composables.edittext.MA_TextoEditable
import com.personal.requestmobility.core.composables.edittext.MA_TextoNormal
import com.personal.requestmobility.core.composables.labels.MA_LabelNormal
import com.personal.requestmobility.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.core.screen.ErrorScreen
import com.personal.requestmobility.core.screen.LoadingScreen
import com.personal.requestmobility.menu.Features
import com.personal.requestmobility.menu.screen.HomeVM.UIState
import com.personal.requestmobility.paneles.domain.entidades.EsquemaColores
import com.personal.requestmobility.paneles.domain.entidades.PanelData
import com.personal.requestmobility.paneles.domain.entidades.fromPanelUI
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
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        MA_ScaffoldGenerico(
            titulo = "",
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
                            seleccionado = true,
                            destacado = true,
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
                        )
                    }


                }
            },
            contenido = {


                val scroll = rememberScrollState()

                Box(Modifier) {
                    Column(modifier = Modifier.verticalScroll(state = scroll)) {


                        MA_Dialogo(
                            trigger = { show -> MA_BotonNormal(texto = "Pruebas") { show() } },
                            resultado = { res ->
                                if (res is ResultadoDialog.Si) {
                                    App.log.d("Si")
                                }

                                if (res is ResultadoDialog.No) {
                                    App.log.d("No")
                                }
                            })








                        uiState.paneles.forEach { panelUI ->
                            MA_Panel(panelData = PanelData().fromPanelUI(panelUI))
                        }


                    }
                }


            }
        )


    }
}

