package com.personal.requestmobility.sincronizacion.ui.lista

import MA_IconBottom
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.edittext.MA_TextBuscador
import com.personal.requestmobility.core.composables.listas.MA_Lista
import com.personal.requestmobility.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.core.screen.ErrorScreen
import com.personal.requestmobility.core.screen.LoadingScreen
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
        UIState.Loading -> LoadingScreen()
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


    MA_ScaffoldGenerico(
        titulo = "Sincroniacion",
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

        })

}



