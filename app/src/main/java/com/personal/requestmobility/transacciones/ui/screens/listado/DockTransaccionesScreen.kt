package com.personal.requestmobility.transacciones.ui.screens.listado

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.botones.BotonNormal
import com.personal.requestmobility.core.composables.componentes.panel.Panel
import com.personal.requestmobility.core.composables.edittext.TextBuscador
import com.personal.requestmobility.core.composables.labels.LabelTituloTabla
import com.personal.requestmobility.core.composables.listas.Lista
import com.personal.requestmobility.core.screen.ErrorScreen
import com.personal.requestmobility.core.screen.LoadingScreen
import com.personal.requestmobility.transacciones.domain.interactors.ObtenerKPIsCU
import com.personal.requestmobility.transacciones.ui.entidades.Filtro
import com.personal.requestmobility.transacciones.ui.screens.composables.ItemFiltroTransaccion
import com.personal.requestmobility.transacciones.ui.screens.composables.ModalInferiorFiltros
import com.personal.requestmobility.transacciones.ui.screens.listado.DockTransaccionesVM.UIState
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.inject
import kotlin.getValue

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

            //item {
            Box() {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = "${uiState.transacciones.size} transacciones encontradas",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black,
                    textAlign = TextAlign.Start
                )
            }

            // }

            // item {
            Box() {
                Column {


                    Row(Modifier.fillMaxWidth()) {
                        BotonNormal("Graficas con Trx") { viewModel.onEvent(DockTransaccionesVM.Eventos.CalcularGraficasTransaccionesPresentadas) }
                        BotonNormal("Borrar filtros") { viewModel.onEvent(DockTransaccionesVM.Eventos.BorrarFiltrosAplicados) }

                        ModalInferiorFiltros() {
                            Column {
                                LabelTituloTabla(valor = uiState.textoBuscar)

                                TextBuscador(searchText = viewModel.textoBuscar) { str ->
                                    viewModel.onEvent(DockTransaccionesVM.Eventos.ModificarTextoBuscar(str))
                                }

                                Lista(data = uiState.filtrosTransaccion.filtros) { filtro ->
                                    if (filtro is Filtro.Seleccion) {

                                        ItemFiltroTransaccion(
                                            filtro,
                                            onClick = {
                                                viewModel.onEvent(DockTransaccionesVM.Eventos.ModicarSeleccionFiltro(filtro))
                                            },
                                            onLongClick = {
                                                viewModel.onEvent(DockTransaccionesVM.Eventos.InvertirSeleccionFiltro(filtro))
                                            })
                                    }
                                }

                            }


                        }
                    }
                }
            }
            // }

            //   item {
            /*
                Box() {
                    Row(Modifier.height(300.dp)) {
                        Lista(data = uiState.transacciones.filter { it.estado != EstadoProceso.OK }) { trx ->
                            ItemTransacciones(trx) { }
                        }
                    }
                }*/
            //  }

            //  item {

            /* Row(Modifier.height(300.dp)) {

                 Lista(data = uiState.transacciones) { trx ->
                     ItemTransacciones(trx) {
                         viewModel.onEvent(DockTransaccionesVM.Eventos.ModificarSeleccionTransaccion(trx))
                     }
                 }
             }*/


            //}

            val cu: ObtenerKPIsCU by inject<ObtenerKPIsCU>(ObtenerKPIsCU::class.java)

            val scope = rememberCoroutineScope()
            //  item {
            Box(Modifier) {
                Column() {
                    LabelTituloTabla("Transacciones")

                    val modifier: Modifier = Modifier.fillMaxSize()
                    Column() {

                        /* GraficaBarasConLeyendaTabla(
                             modifier = modifier,
                             columnaClave = 1,
                             columnaValor = 2,
                             listaValores = dameValoresTestTabla()
                         )*/


                        /*uiState.transacciones.forEach { trx ->
                            GraTab(
                                modifier = modifier,
                                graTabData = trx
                            )
                        }*/

                        /*GraTab(
                            modifier = modifier,
                            graTabData = GraTabData(
                                graTabConfiguracion = GraTabConfiguracion(tipo = GraTabTipoGrafica.BARRAS_ANCHAS_VERTICALES, titulo = "PRUEAS 02"),
                                valoresGrafica = ValoresGrafica(elementos = uiState.valoresGraficaErroresTipo)

                            )
                        )*/


                        uiState.kpis.forEach { kpi ->
                            Panel(
                                modifier = modifier,
                                panelData = kpi.panelData
                            )

                            App.log.d(kpi.panelData)
                        }

                        /*  GraTab(
                    modifier = modifier,
                    graTabData = GraTabData(valoresGrafica = ValoresGrafica(elementos = uiState.valoresGraficaErroresTipo))
                )*/


                        /*  BarrasConTablaVertical(
                              modifier = modifier,
                              titulo = "Errores Tipos",
                              listaValores = uiState.valoresGraficaErroresTipo,
                              target = 1f
                          )*/


                        /* CircularConTablaHorizontal(
                             modifier = modifier,
                             titulo = "Tipos",
                             listaValores = uiState.valoresGraficaTiposTransacciones,
                             rellenoCentro = true
                         )*/


                        /*CircularConTablaVertical(
                            modifier = Modifier.fillMaxWidth(),
                            titulo = "Estados",
                            listaValores = uiState.valoresGraficaEstadosTiposProcesamiento,
                            rellenoCentro = true
                        )
                        GraficoCircular(
                            modifier = Modifier.fillMaxSize(),

                            listaValores = uiState.valoresGraficaEstadosTiposProcesamiento,
                            rellenoCentro = true
                        )*/
                    }
                }

            }
            //}

            //item {
            /* Box(Modifier.height(350.dp)) {
                 Row() {
                     LabelTitulo("Errores")

                     val modifier: Modifier = Modifier.fillMaxSize()
                     Column(modifier = modifier) {
                         GraficoCircular(
                             modifier = modifier.weight(1f),

                             listaValores = uiState.valoresGraficaDistribucionErroresPorTipo,
                             rellenoCentro = true
                         )

                         GraficoBarrasVerticales(
                             modifier = modifier.weight(1f),

                             listaValores = uiState.valoresGraficaErroresTipo,

                             )


                     }
                 }

             }*/
            // }
        }

    }

}



