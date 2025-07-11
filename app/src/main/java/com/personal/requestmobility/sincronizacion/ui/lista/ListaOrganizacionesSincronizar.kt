package com.personal.requestmobility.sincronizacion.ui.lista

import MA_IconBottom
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.componentes.TituloScreen
import com.personal.requestmobility.core.composables.dialogos.MA_Dialogo_Informacion
import com.personal.requestmobility.core.composables.dialogos.MA_Dialogo_SiNo
import com.personal.requestmobility.core.composables.dialogos.ResultadoDialog
import com.personal.requestmobility.core.composables.edittext.MA_TextBuscador
import com.personal.requestmobility.core.composables.layouts.MA_Box
import com.personal.requestmobility.core.composables.listas.MA_Lista
import com.personal.requestmobility.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.core.screen.ErrorScreen
import com.personal.requestmobility.core.screen.LoadingScreen
import com.personal.requestmobility.core.utils.K
import com.personal.requestmobility.menu.Features
import com.personal.requestmobility.sincronizacion.ui.composables.OrganizacionListItem
import com.personal.requestmobility.sincronizacion.ui.lista.ListaOrganizacionesSincronizarVM.UIState
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListaOrganizacinesSincronizar(
    viewModel: ListaOrganizacionesSincronizarVM = koinViewModel(),
    navegacion: (EventosNavegacion) -> Unit) {

    LaunchedEffect(Unit) {
        viewModel.onEvent(ListaOrganizacionesSincronizarVM.Eventos.Cargar)
    }

    // Observando el flujo de estado
    val uiState by viewModel.uiState.collectAsState()
    when (uiState) {
        is UIState.Error -> ErrorScreen((uiState as UIState.Error).message)
        UIState.Trabajando -> LoadingScreen()
        is UIState.Success -> Success(
            viewModel, (uiState as UIState.Success),
            navegacion
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Success(viewModel: ListaOrganizacionesSincronizarVM,
            uiState: UIState.Success,
            navegacion: (EventosNavegacion) -> Unit) {

    var mostrarContenidoDialogoEliminar by remember { mutableStateOf(false) }
    var mostrarContenidoDialogoInformacion by remember { mutableStateOf(false) }

    MA_ScaffoldGenerico(
        titulo = "Sincroniacion",
        tituloScreen = TituloScreen.Sincronizar,
        navegacion = { navegacion(EventosNavegacion.MenuApp) },
        volver = false,
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
                        onClick = { navegacion(EventosNavegacion.MenuApp) }
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )

                    MA_IconBottom(
                        modifier = Modifier.weight(1f),
                        icon = Features.EliminarDatosActuales().icono,
                        labelText = Features.EliminarDatosActuales().texto,
                        onClick = {

                            viewModel.onEvent(ListaOrganizacionesSincronizarVM.Eventos.EliminarDatosActuales)
                        }
                    )


                    MA_IconBottom(
                        modifier = Modifier.weight(1f),
                        icon = Features.Sincronizar().icono,
                        labelText = Features.Sincronizar().texto,
                        onClick = {

                            viewModel.onEvent(ListaOrganizacionesSincronizarVM.Eventos.RealizarSincronizacion)
                        }
                    )


                }


            }
        },
        contenido = {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {


                // Barra de búsqueda
                MA_TextBuscador(
                    searchText = uiState.textoBuscar,
                    onSearchTextChanged = { it ->
                        viewModel.onEvent(ListaOrganizacionesSincronizarVM.Eventos.Buscar(it))
                    },
                )

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

                    /*Text(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .weight(1f),
                        text = "${uiState.lista.size} kpis encontradas",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black,
                        textAlign = TextAlign.Start
                    )*/

                }


                MA_Lista(data = uiState.organizaciones) { organizacionUI ->
                    OrganizacionListItem(organizacionUI = organizacionUI, onClickItem = {
                        App.log.d(organizacionUI.toString())
                        viewModel.onEvent(ListaOrganizacionesSincronizarVM.Eventos.OnChangeSeleccionCheck(organizacionUI))
                    })
                }


            }
            if (uiState.trabajando) {
                MA_Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(80.dp),
                        // Color de la barra de progreso
                        color = MaterialTheme.colorScheme.primary,
                        // Color de la "pista" o fondo de la barra
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }






        })

}


