package com.personal.requestmobility.paneles.ui.screen.detalle


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.App

import com.personal.requestmobility.core.composables.botones.MA_BotonNormal
import com.personal.requestmobility.core.composables.card.MA_Card
import com.personal.requestmobility.core.composables.checks.MA_CheckBoxNormal
import com.personal.requestmobility.core.composables.combo.MA_Combo
import com.personal.requestmobility.core.composables.combo.MA_ComboLista
import com.personal.requestmobility.core.composables.edittext.MA_TextoNormal
import com.personal.requestmobility.core.composables.labels.MA_Titulo
import com.personal.requestmobility.core.navegacion.RespuestaAccionCU
import com.personal.requestmobility.core.screen.ErrorScreen
import com.personal.requestmobility.core.screen.LoadingScreen
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.paneles.ui.screen.detalle.DetallePanelVM.UIState

import org.koin.androidx.compose.koinViewModel


@Composable
fun DetallePanelScreen(identificador: Int,
                       viewModel: DetallePanelVM = koinViewModel(),
                       onProcess: ((RespuestaAccionCU) -> Unit)) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(DetallePanelVM.Eventos.Cargar(identificador))
    }


    when (uiState) {
        is UIState.Error -> ErrorScreen((uiState as UIState.Error).mensaje)
        UIState.Loading -> LoadingScreen()
        is UIState.Success -> SuccessScreenDetalleKpi(
            viewModel,
            uiState as UIState.Success,
            onProcess
        )

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessScreenDetalleKpi(viewModel: DetallePanelVM,
                            uiState: UIState.Success,
                            onProcess: ((RespuestaAccionCU) -> Unit)) {
    val panelUI = uiState.panelUI

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Detalle") },

                )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(4.dp)
                .verticalScroll(rememberScrollState())
        ) {

            MA_TextoNormal(
                valor = panelUI.id.toString(), "ID",
                onValueChange = { valor -> null }
            )



            MA_TextoNormal(
                valor = panelUI.titulo, titulo = "Titulo",
                onValueChange = { valor ->
                    viewModel.onEvent(DetallePanelVM.Eventos.OnChangeTitulo(valor))
                }
            )
            MA_TextoNormal(
                valor = panelUI.descripcion, titulo = "Descripcion",
                onValueChange = { valor ->
                    viewModel.onEvent(DetallePanelVM.Eventos.OnChangeDescripcion(valor))
                }
            )


            MA_Card {
                Column {
                    MA_Titulo("Configuración")

                    Spacer(modifier = Modifier.padding(5.dp))

                    // No hay más campos como "Codigo Organizacion" o "Codigo" para Dashboard
                    Spacer(modifier = Modifier.height(16.dp)) // Espacio inferior

                    Text("Paneles:", style = MaterialTheme.typography.headlineSmall)
                    Box(modifier = Modifier.height(200.dp)) {
                        MA_ComboLista<KpiUI>(
                            titulo = "Kpi",
                            descripcion = "Seleccione el KPI a enlazar",
                            valorInicial = {
                                MA_Titulo(panelUI.kpi.titulo)
                            },
                            elementosSeleccionables = uiState.kpiDisponibles,
                            item = { i ->
                                Text(i.titulo)
                            },
                            onClickSeleccion = { kpiUI ->
                              viewModel.onEvent(DetallePanelVM.Eventos.OnChangeKpiSeleccionado(kpiUI.id))
                            }
                        )
                    }


                }
            }



            MA_Card {
                Column {
                    MA_Titulo("Configuración")

                    Spacer(modifier = Modifier.padding(5.dp))

                    MA_Combo(
                        titulo = "Orientacion de la gráfica",
                        descripcion = "Seleccione  como quiere que se presenten la grafica y la tabla",
                        valorInicial = panelUI.configuracion.orientacion.name,
                        elementosSeleccionables = listOf("HORIZONTAL", "VERTICAL"),
                        onClickSeleccion = { str, indice ->
                            viewModel.onEvent(DetallePanelVM.Eventos.onChangeOrientacion(str))
                        })

                    MA_Combo(
                        titulo = "Tipo de Gráfica",
                        descripcion = "Seleccione  el tipo de gráfica a utilizar",
                        valorInicial = panelUI.configuracion.tipo.name,
                        elementosSeleccionables = listOf(
                            "BARRAS_ANCHAS_VERTICALES",
                            "BARRAS_FINAS_VERTICALES",
                            "CIRCULAR",
                            "ANILLO",
                            "LINEAS",
                        ),
                        onClickSeleccion = { str, indice ->
                            viewModel.onEvent(DetallePanelVM.Eventos.onChangeTipoGrafica(str))
                        })





                    MA_Combo(
                        titulo = "Límite de elementos",
                        descripcion = "Límite de elementos que se pueden presentar en la lista",
                        valorInicial = panelUI.configuracion.limiteElementos.toString(),
                        elementosSeleccionables = (0..10).map { it.toString() },
                        onClickSeleccion = { str, indice ->
                            viewModel.onEvent(DetallePanelVM.Eventos.onChangeLimiteElementos(str))
                        })



                    MA_CheckBoxNormal(
                        titulo = "Mostrar etiquetas", valor = panelUI.configuracion.mostrarEtiquetas,
                        onValueChange = { valor -> viewModel.onEvent(DetallePanelVM.Eventos.onChangeMostrarEtiquetas(valor)) })


                    MA_CheckBoxNormal(
                        titulo = "Datos Ordenadoss", valor = panelUI.configuracion.ordenado,
                        onValueChange = { valor -> viewModel.onEvent(DetallePanelVM.Eventos.onChangeMostrarOrdenado(valor)) })



                    MA_Combo(
                        titulo = "Espacio Gráfica",
                        descripcion = "Porcentaje del espaci que va  utilizar la gráfica en el panel",
                        valorInicial = (panelUI.configuracion.espacioGrafica * 100).toString(),
                        elementosSeleccionables = (0..100 step 10).map { it.toString() },
                        onClickSeleccion = { str, indice ->
                            viewModel.onEvent(DetallePanelVM.Eventos.onChangeEspacioGrafica(str))
                        })


                    MA_Combo(
                        titulo = "Espacio Tabla",
                        descripcion = "Porcentaje del espaci que va  utilizar la gráfica en el panel",
                        valorInicial = (panelUI.configuracion.espacioTabla * 100).toString(),
                        elementosSeleccionables = (0..100 step 10).map { it.toString() },
                        onClickSeleccion = { str, indice ->
                            viewModel.onEvent(DetallePanelVM.Eventos.onChangeEspacioTabla(str))
                        })



                    MA_CheckBoxNormal(
                        titulo = "Ocupar todo el espacio", valor = panelUI.configuracion.ocuparTodoEspacio,
                        onValueChange = { valor -> viewModel.onEvent(DetallePanelVM.Eventos.onChangeOcuparTodoEspacio(valor)) })


                    MA_Combo(
                        titulo = "Ancho Total",
                        descripcion = "Ancho a ocupar en (DP)",
                        valorInicial = (panelUI.configuracion.width.value.toInt()).toString(),
                        elementosSeleccionables = (400..1000 step 50).map { it.toString() },
                        onClickSeleccion = { str, indice ->
                            viewModel.onEvent(DetallePanelVM.Eventos.onChangeAncho(str))
                        })


                    MA_Combo(
                        titulo = "Alto Total",
                        descripcion = "Alto a ocupar en (DP)",
                        valorInicial = (panelUI.configuracion.height.value.toInt()).toString(),
                        elementosSeleccionables = (400..1000 step 50).map { it.toString() },
                        onClickSeleccion = { str, indice ->
                            viewModel.onEvent(DetallePanelVM.Eventos.onChangeAlto(str))
                        })



                    MA_CheckBoxNormal(
                        modifier = Modifier.background(color = Color.Yellow),
                        titulo = "Mostrar Grafica",
                        valor = panelUI.configuracion.mostrarGrafica,
                        onValueChange = { valor -> viewModel.onEvent(DetallePanelVM.Eventos.onChangeMostrarGrafica(valor)) }
                    )



                    MA_CheckBoxNormal(
                        modifier = Modifier.background(color = Color.Yellow),
                        titulo = "Mostrar Tabla",
                        valor = panelUI.configuracion.mostrarTabla,
                        onValueChange = { valor -> viewModel.onEvent(DetallePanelVM.Eventos.onChangeMosrtarTabla(valor)) }
                    )


                    MA_CheckBoxNormal(
                        titulo = "Mostrar Título Tabla",
                        valor = panelUI.configuracion.mostrarTituloTabla,
                        onValueChange = { valor -> viewModel.onEvent(DetallePanelVM.Eventos.onChangeMostrarOrdenado(valor)) }
                    )


                    MA_Combo(
                        titulo = "Campo Agrupacion tabla",
                        descripcion = "Campo  por el que se van a agrupar los valores cuando la tabla se encuentre limitada",
                        valorInicial = (panelUI.configuracion.campoAgrupacionTabla).toString(),
                        elementosSeleccionables = (1..15).map { it.toString() },
                        onClickSeleccion = { str, indice ->
                            viewModel.onEvent(DetallePanelVM.Eventos.onChangeCampoAgrupacionTabla(str))
                        })


                    MA_Combo(
                        titulo = "Campo Suma tabla",
                        descripcion = "Campo por el que se van a sumar los valores",
                        valorInicial = (panelUI.configuracion.campoSumaValorTabla).toString(),
                        elementosSeleccionables = (1..100).map { it.toString() },
                        onClickSeleccion = { str, indice ->
                            viewModel.onEvent(DetallePanelVM.Eventos.onChangeCampoSumaTabla(str))
                        })


                    MA_CheckBoxNormal(
                        titulo = "Ajustar contenido ancho",
                        valor = panelUI.configuracion.ajustarContenidoAncho,
                        onValueChange = { valor -> viewModel.onEvent(DetallePanelVM.Eventos.onChangeAjustarContenido(valor)) }
                    )

                    MA_CheckBoxNormal(
                        titulo = "Indicador Color en las filas",
                        valor = panelUI.configuracion.indicadorColor,
                        onValueChange = { valor -> viewModel.onEvent(DetallePanelVM.Eventos.onChangeIndicadorColor(valor)) }
                    )

                    MA_CheckBoxNormal(
                        titulo = "Filas en color",
                        valor = panelUI.configuracion.filasColor,
                        onValueChange = { valor -> viewModel.onEvent(DetallePanelVM.Eventos.onChangeFilasColor(valor)) }
                    )

                }

            }
            /*  kpiUI.reloadPanelData()
              MA_Panel(
                  modifier = Modifier,
                  panelData = kpiUI.panelData
              )*/

            App.log.d(panelUI.configuracion.toString())


            Column {
                MA_BotonNormal("Guardar") { viewModel.onEvent(DetallePanelVM.Eventos.Guardar(onProcess)) }
                MA_BotonNormal("Eliminar") { viewModel.onEvent(DetallePanelVM.Eventos.Eliminar(onProcess)) }
            }


        }
    }
}


