package com.personal.requestmobility.paneles.ui.screen.listado

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import com.personal.requestmobility.core.screen.ErrorScreen
import com.personal.requestmobility.core.screen.LoadingScreen
import com.personal.requestmobility.paneles.ui.componente.PanelListItem
import com.personal.requestmobility.paneles.ui.entidades.PanelUI
import com.personal.requestmobility.paneles.ui.screen.listado.PanelesListadoVM.UIState
import org.koin.androidx.compose.koinViewModel


@Composable
fun PanelesListadoScreen(viewModel: PanelesListadoVM = koinViewModel(),
                         onClickPanel: (PanelUI) -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(PanelesListadoVM.Eventos.Cargar)
    }


    when (uiState) {

        is PanelesListadoVM.UIState.Error -> ErrorScreen((uiState as UIState.Error).mensaje)
        is PanelesListadoVM.UIState.Loading -> LoadingScreen((uiState as UIState.Loading).mensaje)
        is PanelesListadoVM.UIState.Success -> SuccessListadoPaneles(
            viewModel = viewModel,
            uiState = uiState as UIState.Success,
            onClickPanel = onClickPanel
        )
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessListadoPaneles(viewModel: PanelesListadoVM,
                          uiState: UIState.Success,
                          onClickPanel: (PanelUI) -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Paneles") },
                navigationIcon = { Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "") }
            )
        },
        bottomBar = {

        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(16.dp)
        ) {


            // Barra de búsqueda
            MA_TextBuscador(
                searchText = uiState.textoBuscar,
                onSearchTextChanged = { it ->
                    viewModel.onEvent(PanelesListadoVM.Eventos.Buscar(it))
                },
            )

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

                Text(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f),
                    text = "${uiState.lista.size} kpis encontradas",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black,
                    textAlign = TextAlign.Start
                )




                TextButton(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f),
                    onClick = { viewModel.onEvent(PanelesListadoVM.Eventos.NuevoContenidoLocal(onClickPanel)) }) {
                    Text(text = "Nuevo")
                }

            }




            MA_Lista(data = uiState.lista) { item ->
                PanelListItem(item, onClickItem = onClickPanel)
            }

        }
    }
}