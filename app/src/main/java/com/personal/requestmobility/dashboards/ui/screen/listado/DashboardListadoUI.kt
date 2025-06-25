package com.personal.requestmobility.dashboards.ui.screen.listado

import MA_IconBottom
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.edittext.MA_TextBuscador
import com.personal.requestmobility.core.composables.listas.MA_Lista
import com.personal.requestmobility.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.core.screen.ErrorScreen
import com.personal.requestmobility.core.screen.LoadingScreen
import com.personal.requestmobility.dashboards.ui.composables.MA_DashboardItem
import com.personal.requestmobility.menu.Features
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardListadoUI(
    viewModel: DashboardListadoVM = koinViewModel(),
    navegacion: (EventosNavegacion) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvento(DashboardListadoVM.Eventos.Cargar)
    }

    when (val state = uiState) { // Renombrado uiState a state para claridad en el when
        is DashboardListadoVM.UIState.Error -> ErrorScreen(state.mensaje) // Asume ErrorScreen(mensaje: String)
        is DashboardListadoVM.UIState.Loading -> LoadingScreen(state.mensaje) // Asume LoadingScreen(mensaje: String)
        is DashboardListadoVM.UIState.Success -> SuccessListadoDashboards( // Nombre corregido
            viewModel = viewModel,
            uiState = state,
            navegacion = navegacion
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessListadoDashboards( // Nombre corregido del Composable de éxito
    viewModel: DashboardListadoVM,
    uiState: DashboardListadoVM.UIState.Success,
    navegacion: (EventosNavegacion) -> Unit
) {
    MA_ScaffoldGenerico(
        titulo = "Dashboards", // Título adaptado
        navegacion = { navegacion(EventosNavegacion.MenuApp) }, // Para el icono de navegación del TopAppBar
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
                        onClick = { navegacion(EventosNavegacion.MenuHerramientas) }
                    )
                    Spacer(modifier = Modifier.fillMaxWidth().weight(1f))
                    MA_IconBottom(
                        modifier = Modifier.weight(1f),
                        icon = Features.Nuevo().icono,
                        labelText = Features.Nuevo().texto,
                        color = Features.Nuevo().color,
                        onClick = { navegacion(EventosNavegacion.NuevoDashboard) }
                    )


                }


            }


           /* BottomAppBar {
                IconButton(onClick = {
                    // App.log.d("Click de volver desde el menu") // Comentado como en el original si App.log no está disponible
                    navegacion(EventosNavegacion.MenuApp)
                }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }

                IconButton(onClick = {
                    navegacion(EventosNavegacion.Cargar(0)) // id 0 para nuevo item
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Nuevo")
                }
            }*/
        },
        contenido = {
            Column(
                modifier = Modifier
                    .fillMaxWidth() // fillMaxWidth para la columna principal
                    .padding(16.dp) // Padding general del contenido como en el ejemplo
            ) {
                MA_TextBuscador(
                    searchText = uiState.textoBuscar,
                    onSearchTextChanged = { texto -> // Parámetro renombrado a 'texto'
                        viewModel.onEvento(DashboardListadoVM.Eventos.Buscar(texto))
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp) // Como en el ejemplo
                            .weight(1f),
                        text = "${uiState.lista.size} elementos",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black, // Como en el ejemplo
                        textAlign = TextAlign.Start
                    )
                    // El TextButton de "Nuevo" del ejemplo está comentado y la funcionalidad está en el BottomAppBar
                }
                MA_Lista( // Asume que Lista es un Composable que maneja internamente LazyColumn
                    data = uiState.lista,
                    itemContent = { item -> // item es DashboardUI
                        MA_DashboardItem(item, navegacion = navegacion)
                    }
                )
            }
        }
    )
}