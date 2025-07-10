package com.personal.requestmobility.dashboards.ui.screen.detalle

import MA_IconBottom
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.card.MA_Card
import com.personal.requestmobility.core.composables.checks.MA_SwitchNormal
import com.personal.requestmobility.core.composables.combo.MA_ComboLista
import com.personal.requestmobility.core.composables.edittext.MA_TextoNormal
import com.personal.requestmobility.core.composables.formas.MA_Avatar
import com.personal.requestmobility.core.composables.labels.MA_Titulo2
import com.personal.requestmobility.core.composables.listas.MA_ListaReordenable_EstiloYouTube
import com.personal.requestmobility.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.core.screen.ErrorScreen
import com.personal.requestmobility.core.screen.LoadingScreen
import com.personal.requestmobility.dashboards.ui.composables.SeleccionPanelItemDashboard
import com.personal.requestmobility.kpi.ui.composables.KpiComboItem
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.menu.Features
import com.personal.requestmobility.paneles.ui.entidades.PanelUI
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetalleDashboardUI(
    identificador: Int,
    viewModel: DetalleDashboardVM = koinViewModel(),
    navegacion: (EventosNavegacion) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // LaunchedEffect con Unit en el ejemplo, pero es mejor usar identificador como key
    // si puede cambiar y queremos recargar. El ejemplo usa Unit
    // .
    LaunchedEffect(Unit) { // Siguiendo el ejemplo
        viewModel.onEvento(DetalleDashboardVM.Eventos.Cargar(identificador))
    }
    
    
    // Si el identificador pudiera cambiar mientras la pantalla está en la pila,
    // LaunchedEffect(identificador) { ... } sería más robusto.

    when (val state = uiState) { // Renombrado uiState a state
        is DetalleDashboardVM.UIState.Error -> ErrorScreen(state.mensaje)
        is DetalleDashboardVM.UIState.Loading -> LoadingScreen(state.mensaje)
        is DetalleDashboardVM.UIState.Success -> DetalleDashboardUIScreen( // Nombre corregido
            viewModel = viewModel,
            uiState = state, // Pasando el objeto correcto
            navegacion = navegacion
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleDashboardUIScreen( // Nombre corregido del Composable de éxito
    viewModel: DetalleDashboardVM,
    uiState: DetalleDashboardVM.UIState.Success,
    navegacion: (EventosNavegacion) -> Unit
) {
    
    val dashboardUI =   uiState.dashboardUI
    MA_ScaffoldGenerico(
        volver = false,
        titulo = if (dashboardUI.id == 0) "Nuevo Dashboard" else "Datos Dashboard", // Título adaptado
        // 'navegacion' en ScaffoldGenerico del ejemplo original es la acción del icono de navegación del TopAppBar
        navegacion = { }, // Adaptado para claridad, asume que ScaffoldGenerico tiene 'navigateUp'
        contenidoBottomBar = {
            BottomAppBar() {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Bottom

                ) {

                    MA_IconBottom(
                        modifier = Modifier.weight(1f),
                        icon = Features.Menu().icono,
                        labelText = Features.Menu().texto,
                        onClick = {
                            navegacion(EventosNavegacion.MenuDashboard)
                        }
                    )

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )

                    MA_IconBottom(
                        modifier = Modifier.weight(1f),
                        icon = Features.Eliminar().icono,
                        labelText = Features.Eliminar().texto,
                        color = Features.Eliminar().color,
                        onClick = {
                            viewModel.onEvento(DetalleDashboardVM.Eventos.Eliminar(navegacion))
                            //navegacion(EventosNavegacion.MenuDashboard)
                        }
                    )

                    MA_IconBottom(
                        modifier = Modifier.weight(1f),
                        icon = Features.Guardar().icono,
                        labelText = Features.Guardar().texto,
                        color = Features.Guardar().color,
                        onClick = {
                            viewModel.onEvento(DetalleDashboardVM.Eventos.Guardar(navegacion))
                            //navegacion(EventosNavegacion.MenuDashboard)
                        }
                    )


                }

            }

        },
        contenido = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier

                    .padding(horizontal = 16.dp) // Padding adicional para el contenido interno
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Para que el contenido sea scrollable
            ) {


                MA_Avatar(dashboardUI.nombre)


                MA_Titulo2("Informacion")

                MA_TextoNormal(
                    valor = dashboardUI.nombre,
                    titulo = "Nombre", // Equivalente a "Item"
                    onValueChange = { valor ->
                        viewModel.onEvento(DetalleDashboardVM.Eventos.OnChangeNombre(valor))
                    }
                )

                // No hay CheckBoxNormal para "Global" en Dashboard

                MA_TextoNormal(
                    valor = dashboardUI.descripcion,
                    titulo = "Descripción", // Equivalente a "Proveedor"
                    onValueChange = { valor ->
                        viewModel.onEvento(DetalleDashboardVM.Eventos.OnChangeDescripcion(valor))
                    },

                    )

                MA_SwitchNormal(valor = dashboardUI.home, titulo = "Mostrar Inicio", icono = Icons.Default.Star) { valor ->  viewModel.onEvento(DetalleDashboardVM.Eventos.OnChangeInicial(valor)) }
                // No hay más campos como "Codigo Organizacion" o "Codigo" para Dashboard
                
                
                MA_Titulo2("KPI")
                MA_Card {
                    Box(modifier = Modifier.height(100.dp)) {
                        MA_ComboLista<KpiUI>(titulo = "",
                                             descripcion = "Seleccione el KPI a enlazar",
                                             valorInicial = {
                                                 KpiComboItem(kpiUI = dashboardUI.kpiOrigen ?: KpiUI())
                                                 
                                             },
                                             elementosSeleccionables = uiState.kpisDisponibles,
                                             item = { kpiUI ->
                                                 KpiComboItem(kpiUI = kpiUI)
                                             },
                                             onClickSeleccion = { kpiUI ->
                                                 viewModel.onEvento(DetalleDashboardVM.Eventos.OnChangeKpiSeleccionado(kpiUI))
                                             })
                    }
                }
                
                MA_Titulo2("Paneles")

                Box(modifier = Modifier.height(400.dp)) {

                    val paneles: List<PanelUI> = dashboardUI.listaPaneles
                 
                    MA_ListaReordenable_EstiloYouTube(
                        data = dashboardUI.listaPaneles.sortedBy { it.orden },
                        itemContent = { panel, isDragging ->
                            // Tu Composable para el ítem.
                            // Puedes usar 'isDragging' para cambiar la apariencia si lo deseas
                            // ej. MiPanelItem(panel, if (isDragging) Modifier.border(...) else Modifier)
                            SeleccionPanelItemDashboard(panel) { panelSeleccionado ->
                                App.log.d("[PREV] ${panelSeleccionado.seleccionado} ${panelSeleccionado.titulo}")
                                val panelesR: List<PanelUI> = paneles.map { panel ->
                                    if (panel.id == panelSeleccionado.id) {
                                        App.log.d("Encontrado")
                                        App.log.d("${!panelSeleccionado.seleccionado} ${panelSeleccionado.titulo}")
                                        panel.copy(seleccionado = !panelSeleccionado.seleccionado)
                                    } else {
                                        panel
                                    }
                                }

                                val p = panelesR.first { it.id == panelSeleccionado.id }
                                App.log.d("[POST] ${p.seleccionado} ${p.titulo}")
                                viewModel.onEvento(DetalleDashboardVM.Eventos.OnActualizarPaneles(panelesR))

                            }
                        },
                        onItemClick = { /* ... */ },
                        onListReordered = {listaReordenada ->
                            App.log.lista("LR" , listaReordenada)

                            var listaR : List<PanelUI> = emptyList()
                            listaReordenada.forEachIndexed{indice, panel ->
                                listaR = listaR.plus(panel.copy(orden =  indice))
                            }
                            App.log.lista("Reodrd" , listaR)

                            viewModel.onEvento(DetalleDashboardVM.Eventos.OnActualizarPaneles(listaR))

                        },
                        itemHeight = 72.dp // ¡IMPORTANTE! Ajusta esto a la altura real de tus ítems
                    )
                }

            }
        }
    )
}