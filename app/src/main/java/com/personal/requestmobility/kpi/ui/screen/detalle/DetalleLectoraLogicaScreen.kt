package com.personal.requestmobility.kpi.ui.screen.detalle


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.kpi.ui.screen.detalle.DetalleKpiVM.UIState
import com.personal.requestmobility.core.composables.botones.BotonNormal
import com.personal.requestmobility.core.composables.edittext.TextoNormal
import com.personal.requestmobility.core.navegacion.RespuestaAccionCU
import com.personal.requestmobility.core.screen.ErrorScreen
import com.personal.requestmobility.core.screen.LoadingScreen
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import org.koin.androidx.compose.koinViewModel


@Composable
fun DetalleKpiScreen(identificador: Int, viewModel: DetalleKpiVM = koinViewModel(),
                     onProcess: ((RespuestaAccionCU) -> Unit)) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(DetalleKpiVM.Eventos.Cargar(identificador))
    }


    when (uiState) {
        is UIState.Error -> ErrorScreen((uiState as UIState.Error).mensaje)
        UIState.Loading -> LoadingScreen()
        is UIState.Success -> SuccessScreenDetalleKpi(
            viewModel,
            (uiState as UIState.Success).kpiUI,
            onProcess
        )

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessScreenDetalleKpi(viewModel: DetalleKpiVM,
                            kpiUI: KpiUI,
                            onProcess: ((RespuestaAccionCU) -> Unit)) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kpi") },

                )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            TextoNormal(
                valor = kpiUI.id.toString(), "ID",
                onValueChange = { valor -> null }
            )

            TextoNormal(
                valor = kpiUI.titulo, titulo = "Titulo",
                onValueChange = { valor ->
                    viewModel.onEvent(DetalleKpiVM.Eventos.OnChangeTitulo(valor))
                }
            )


            TextoNormal(
                valor = kpiUI.descripcion, titulo = "Descripcion",
                onValueChange = { valor ->
                    viewModel.onEvent(DetalleKpiVM.Eventos.OnChangeDescripcion(valor))
                }
            )

            TextoNormal(
                valor = kpiUI.sql, titulo = "SQL",
                onValueChange = { valor ->
                    viewModel.onEvent(DetalleKpiVM.Eventos.OnChangeSQL(valor))
                }
            )



            Column {
                BotonNormal("Guardar") { viewModel.onEvent(DetalleKpiVM.Eventos.Guardar(onProcess)) }
                BotonNormal("Eliminar") { viewModel.onEvent(DetalleKpiVM.Eventos.Eliminar(onProcess)) }
            }


        }
    }
}