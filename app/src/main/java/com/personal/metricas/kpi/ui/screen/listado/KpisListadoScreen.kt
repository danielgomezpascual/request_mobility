package com.personal.metricas.kpi.ui.screen.listado

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
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.edittext.MA_TextBuscador
import com.personal.metricas.core.composables.listas.MA_Lista
import com.personal.metricas.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.metricas.core.navegacion.EventosNavegacion

import com.personal.metricas.core.screen.ErrorScreen
import com.personal.metricas.core.screen.LoadingScreen
import com.personal.metricas.kpi.ui.composables.KpiListItem
import com.personal.metricas.kpi.ui.screen.listado.KpisListadoVM.UIState
import com.personal.metricas.menu.Features
import org.koin.androidx.compose.koinViewModel


@Composable
fun KpisListadoScreen(viewModel: KpisListadoVM = koinViewModel(),
                      navegacion: (EventosNavegacion) -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(KpisListadoVM.Eventos.Cargar)
    }


    when (uiState) {
        is UIState.Error -> ErrorScreen((uiState as UIState.Error).mensaje)
        is UIState.Loading -> LoadingScreen((uiState as UIState.Loading).mensaje)
        is UIState.Success -> SucessListadoLectoras(
            viewModel = viewModel,
            uiState = uiState as UIState.Success,
            navegacion = navegacion
        )
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SucessListadoLectoras(viewModel: KpisListadoVM,
                          uiState: UIState.Success,
                          navegacion: (EventosNavegacion) -> Unit
) {


    MA_ScaffoldGenerico(
        titulo = "Kpis",
        tituloScreen = TituloScreen.Kpi,
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
                        onClick = { navegacion(EventosNavegacion.MenuHerramientas) }
                    )
                    Spacer(modifier = Modifier.fillMaxWidth().weight(1f))
                    MA_IconBottom(
                         modifier = Modifier.weight(1f),
                        icon = Features.Nuevo().icono,
                        labelText = Features.Nuevo().texto,
                        color = Features.Nuevo().color,
                        onClick = { navegacion(EventosNavegacion.NuevoKPI) }
                    )


                }


            }
        },
        contenido = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()

            ) {


                MA_Card() {
                    Column() {

                        // Barra de búsqueda
                        MA_TextBuscador(
                            searchText = uiState.textoBuscar,
                            onSearchTextChanged = { it ->
                                viewModel.onEvent(KpisListadoVM.Eventos.Buscar(it))
                            },
                        )





                        MA_Lista(data = uiState.lista) { item ->
                            KpiListItem(item,
                                        onClickItem = { navegacion(EventosNavegacion.CargarKPI(item.id)) })

                        }
                    }
                }

            }


        }
    )
}