package com.personal.requestmobility.transacciones.ui.screens.listado

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.componentes.panel.MA_Panel
import com.personal.requestmobility.core.composables.labels.MA_LabelTituloTabla
import com.personal.requestmobility.core.screen.ErrorScreen
import com.personal.requestmobility.core.screen.LoadingScreen
import com.personal.requestmobility.transacciones.ui.screens.listado.DockTransaccionesVM.UIState
import org.koin.androidx.compose.koinViewModel

@Composable
fun DockTransaccionesScreen(viewModel: DockTransaccionesVM = koinViewModel()) {

    LaunchedEffect(Unit) {
        viewModel.onEvent(DockTransaccionesVM.Eventos.Cargar)
    }

    // Observando el flujo de estado
    val uiState by viewModel.uiState.collectAsState()
    when (uiState) {
        is UIState.Error -> ErrorScreen((uiState as UIState.Error).message)
        UIState.Loading -> LoadingScreen()
        is UIState.Success -> Success(viewModel, (uiState as UIState.Success))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Success(viewModel: DockTransaccionesVM, uiState: UIState.Success) {

    Scaffold(
        topBar = {

            TopAppBar(
                title = { Text("Request") },
                navigationIcon = { Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "") }
            )
        },
        bottomBar = {

        }
    ) { innerPadding ->
        val scrollState = rememberScrollState()

        Column(
            Modifier
                .verticalScroll(state = scrollState)
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(Modifier) {
                Column() {
                    MA_LabelTituloTabla("Transacciones")
                    val modifier: Modifier = Modifier.fillMaxSize()
                    Column() {
                        uiState.kpis.forEach { kpiUI ->
                            MA_Panel(
                                modifier = modifier,
                                panelData = kpiUI.panelData
                            )

                            App.log.d(kpiUI.panelData)
                        }
                    }
                }
            }
        }

    }

}



