package com.personal.metricas.dashboards.ui.screen.cuadricula

import MA_IconBottom
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HdrAuto
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stars
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.edittext.MA_TextBuscador
import com.personal.metricas.core.composables.formas.MA_Avatar
import com.personal.metricas.core.composables.imagenes.MA_Icono
import com.personal.metricas.core.composables.labels.MA_LabelNegrita
import com.personal.metricas.core.composables.layouts.MA_Columnas
import com.personal.metricas.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.screen.ErrorScreen
import com.personal.metricas.core.screen.LoadingScreen
import com.personal.metricas.core.utils._toJson
import com.personal.metricas.dashboards.ui.composables.MA_EtiquetaItem
import com.personal.metricas.menu.Features
import com.personal.metricas.paneles.domain.entidades.FuncionesCondicionesCeldaManager
import org.koin.androidx.compose.koinViewModel

@Composable
fun CuadriculDashboardUI(
        viewModel: CuadriculaDashboardVM = koinViewModel(),
        navegacion: (EventosNavegacion) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.onEvento(CuadriculaDashboardVM.Eventos.Cargar) }

    when (val state = uiState) { // Renombrado uiState a state para claridad en el when
        is CuadriculaDashboardVM.UIState.Error ->
                ErrorScreen(state.mensaje) // Asume ErrorScreen(mensaje: String)
        is CuadriculaDashboardVM.UIState.Loading ->
                LoadingScreen(state.mensaje) // Asume LoadingScreen(mensaje: String)
        is CuadriculaDashboardVM.UIState.Success ->
                SuccessCuadriculaDashboard( // Nombre corregido
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
        navegacion: (EventosNavegacion) -> Unit,
) {
    var mostrarBuscador by remember { mutableStateOf(false) }

    MA_ScaffoldGenerico(
            tituloScreen = TituloScreen.DashboardLista,
            navegacion = navegacion,
            accionesSuperiores = {
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Top
                ) {
                    MA_IconBottom(
                        icon = Icons.Default.Search,
                        color = Color.DarkGray
                    ) {
                        mostrarBuscador = !mostrarBuscador
                        if (!mostrarBuscador) {
                            viewModel.onEvento(CuadriculaDashboardVM.Eventos.Buscar(""))
                        }
                    }
                    MA_IconBottom(
                            icon = Features.Dashboard().icono,
                            color = Features.Dashboard().color
                    ) { navegacion(EventosNavegacion.NuevoPanel) }
                }
            },
            contenido = {
                Column(
                        modifier = Modifier.fillMaxWidth() // fillMaxWidth para la columna principal
                ) {
                    if (uiState.etiquetasDisponibles.isNotEmpty()) {
                        Row(
                                modifier =
                                        Modifier.fillMaxWidth()
                                                .horizontalScroll(state = rememberScrollState()),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                            uiState.etiquetasDisponibles.forEach { etiqueta ->
                                MA_EtiquetaItem(etiqueta) {
                                    viewModel.onEvento(
                                            CuadriculaDashboardVM.Eventos.FiltrarEtiquetas(etiqueta)
                                    )
                                }
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = mostrarBuscador,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        MA_TextBuscador(
                            searchText = uiState.textoBuscar,
                            onSearchTextChanged = { texto ->
                                viewModel.onEvento(CuadriculaDashboardVM.Eventos.Buscar(texto))
                            }
                        )
                    }

                    MA_Card {


                        MA_Columnas(data = uiState.lista.sortedBy { it.nombre }) { item ->


                            MA_Card(
                                modifier =
                                    Modifier.padding(4.dp)
                                        .clickable(
                                            enabled = true,
                                            onClick = {
                                                navegacion(
                                                    EventosNavegacion
                                                        .VisualizadorDashboard(
                                                            item.id,
                                                            _toJson(
                                                                item.parametros
                                                            )
                                                        )
                                                )
                                            }
                                        ),
                                color = Color(item.color).copy(alpha = 0.1f),
                                elevacion = 0.dp
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    MA_Avatar(item.nombre, color = Color(item.color))

                                    Spacer(modifier = Modifier.size(8.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (item.home)
                                            MA_Icono(Icons.Default.Stars, Modifier.size(16.dp))
                                        if (item.autogenerado)
                                            MA_Icono(Icons.Default.HdrAuto, Modifier.size(16.dp))
                                    }
                                    if (item.nombre.split("(").first().trim().length == 6) {
                                        FuncionesCondicionesCeldaManager()
                                            .banderas(item.nombre)
                                            .composable()
                                    } else {
                                        MA_LabelNegrita(
                                            alineacion = TextAlign.Center,
                                            modifier = Modifier.padding(2.dp),
                                            valor = item.nombre
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
    )
}
