package com.personal.requestmobility.kpi.ui.screen.listado

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
import com.personal.requestmobility.kpi.ui.composables.KpiListItem
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.kpi.ui.screen.listado.KpisListadoVM.UIState
import org.koin.androidx.compose.koinViewModel


@Composable
fun KpisListadoScreen(viewModel: KpisListadoVM = koinViewModel(),
                          onClickLectora: (KpiUI) -> Unit) {
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
            onClickKpi = onClickLectora
        )
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SucessListadoLectoras(viewModel: KpisListadoVM,
                          uiState: UIState.Success,
                          onClickKpi: (KpiUI) -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kpis") },
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
                    viewModel.onEvent(KpisListadoVM.Eventos.Buscar(it))
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
                    onClick = { viewModel.onEvent(KpisListadoVM.Eventos.NuevoContenidoLocal(onClickKpi)) }) {
                    Text(text = "Nuevo")
                }

            }




            MA_Lista(data = uiState.lista) { item ->
                KpiListItem(item, onClickItem = onClickKpi)
            }

        }
    }
}