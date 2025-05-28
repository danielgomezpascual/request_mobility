package com.personal.requestmobility.kpi.ui.screen.detalle


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.App
import com.personal.requestmobility.kpi.ui.screen.detalle.DetalleKpiVM.UIState
import com.personal.requestmobility.core.composables.botones.MA_BotonNormal
import com.personal.requestmobility.core.composables.card.MA_Card
import com.personal.requestmobility.core.composables.checks.MA_CheckBoxNormal
import com.personal.requestmobility.core.composables.combo.MA_Combo
import com.personal.requestmobility.core.composables.edittext.MA_TextoNormal
import com.personal.requestmobility.core.composables.labels.MA_Titulo
import com.personal.requestmobility.core.navegacion.RespuestaAccionCU
import com.personal.requestmobility.core.screen.ErrorScreen
import com.personal.requestmobility.core.screen.LoadingScreen
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import org.koin.androidx.compose.koinViewModel


@Composable
fun DetalleKpiScreen(identificador: Int,
                     viewModel: DetalleKpiVM = koinViewModel(),
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
                .padding(4.dp)
                .verticalScroll(rememberScrollState())
        ) {


            MA_TextoNormal(
                valor = kpiUI.id.toString(), "ID",
                onValueChange = { valor -> null }
            )




            MA_TextoNormal(
                valor = kpiUI.titulo, titulo = "Titulo",
                onValueChange = { valor ->
                    viewModel.onEvent(DetalleKpiVM.Eventos.OnChangeTitulo(valor))
                }
            )

            MA_TextoNormal(
                valor = kpiUI.descripcion, titulo = "Descripcion",
                onValueChange = { valor ->
                    viewModel.onEvent(DetalleKpiVM.Eventos.OnChangeDescripcion(valor))
                }
            )



            MA_TextoNormal(
                valor = kpiUI.sql, titulo = "SQL",
                onValueChange = { valor ->
                    viewModel.onEvent(DetalleKpiVM.Eventos.OnChangeSQL(valor))
                }
            )

          /*  MA_Card {
                Column {
                    MA_Titulo("Configuración")

                    Spacer(modifier = Modifier.padding(5.dp))

                    MA_Combo(
                        titulo = "Orientacion de la gráfica",
                        descripcion = "Seleccione  como quiere que se presenten la grafica y la tabla",
                        valorInicial = kpiUI.panelData.panelConfiguracion.orientacion.name,
                        elementosSeleccionables = listOf("HORIZONTAL", "VERTICAL"),
                        onClickSeleccion = { str, indice ->
                            viewModel.onEvent(DetalleKpiVM.Eventos.onChangeOrientacion(str))
                        })

                    MA_Combo(
                        titulo = "Tipo de Gráfica",
                        descripcion = "Seleccione  el tipo de gráfica a utilizar",
                        valorInicial = kpiUI.panelData.panelConfiguracion.tipo.name,
                        elementosSeleccionables = listOf(
                            "BARRAS_ANCHAS_VERTICALES",
                            "BARRAS_FINAS_VERTICALES",
                            "CIRCULAR",
                            "ANILLO",
                            "LINEAS",
                        ),
                        onClickSeleccion = { str, indice ->
                            viewModel.onEvent(DetalleKpiVM.Eventos.onChangeTipoGrafica(str))
                        })

                    MA_TextoNormal(
                        valor = kpiUI.titulo, titulo = "Titulo",
                        onValueChange = { valor ->
                            viewModel.onEvent(DetalleKpiVM.Eventos.OnChangeTitulo(valor))
                        }
                    )

                    MA_TextoNormal(
                        valor = kpiUI.descripcion, titulo = "Descripcion",
                        onValueChange = { valor ->
                            viewModel.onEvent(DetalleKpiVM.Eventos.OnChangeDescripcion(valor))
                        }
                    )

                    MA_Combo(
                        titulo = "Límite de elementos",
                        descripcion = "Límite de elementos que se pueden presentar en la lista",
                        valorInicial = kpiUI.panelData.panelConfiguracion.limiteElementos.toString(),
                        elementosSeleccionables = (0..10).map { it.toString() },
                        onClickSeleccion = { str, indice ->
                            viewModel.onEvent(DetalleKpiVM.Eventos.onChangeLimiteElementos(str))
                        })



                    MA_CheckBoxNormal(
                        titulo = "Mostrar etiquetas", valor = kpiUI.panelData.panelConfiguracion.mostrarEtiquetas,
                        onValueChange = { valor -> viewModel.onEvent(DetalleKpiVM.Eventos.onChangeMostrarEtiquetas(valor)) })


                    MA_CheckBoxNormal(
                        titulo = "Datos Ordenadoss", valor = kpiUI.panelData.panelConfiguracion.ordenado,
                        onValueChange = { valor -> viewModel.onEvent(DetalleKpiVM.Eventos.onChangeMostrarOrdenado(valor)) })



                    MA_Combo(
                        titulo = "Espacio Gráfica",
                        descripcion = "Porcentaje del espaci que va  utilizar la gráfica en el panel",
                        valorInicial = (kpiUI.panelData.panelConfiguracion.espacioGrafica * 100).toString(),
                        elementosSeleccionables = (0..100 step 10).map { it.toString() },
                        onClickSeleccion = { str, indice ->
                            viewModel.onEvent(DetalleKpiVM.Eventos.onChangeEspacioGrafica(str))
                        })


                    MA_Combo(
                        titulo = "Espacio Tabla",
                        descripcion = "Porcentaje del espaci que va  utilizar la gráfica en el panel",
                        valorInicial = (kpiUI.panelData.panelConfiguracion.espacioTabla * 100).toString(),
                        elementosSeleccionables = (0..100 step 10).map { it.toString() },
                        onClickSeleccion = { str, indice ->
                            viewModel.onEvent(DetalleKpiVM.Eventos.onChangeEspacioTabla(str))
                        })



                    MA_CheckBoxNormal(
                        titulo = "Ocupar todo el espacio", valor = kpiUI.panelData.panelConfiguracion.ocuparTodoEspacio,
                        onValueChange = { valor -> viewModel.onEvent(DetalleKpiVM.Eventos.onChangeOcuparTodoEspacio(valor)) })


                    MA_Combo(
                        titulo = "Ancho Total",
                        descripcion = "Ancho a ocupar en (DP)",
                        valorInicial = (kpiUI.panelData.panelConfiguracion.width.value.toInt()).toString(),
                        elementosSeleccionables = (400..1000 step 50).map { it.toString() },
                        onClickSeleccion = { str, indice ->
                            viewModel.onEvent(DetalleKpiVM.Eventos.onChangeAncho(str))
                        })


                    MA_Combo(
                        titulo = "Alto Total",
                        descripcion = "Alto a ocupar en (DP)",
                        valorInicial = (kpiUI.panelData.panelConfiguracion.height.value.toInt()).toString(),
                        elementosSeleccionables = (400..1000 step 50).map { it.toString() },
                        onClickSeleccion = { str, indice ->
                            viewModel.onEvent(DetalleKpiVM.Eventos.onChangeAlto(str))
                        })



                    MA_CheckBoxNormal(
                        modifier = Modifier.background(color = Color.Yellow),
                        titulo = "Mostrar Grafica",
                        valor = kpiUI.panelData.panelConfiguracion.mostrarGrafica,
                        onValueChange = { valor -> viewModel.onEvent(DetalleKpiVM.Eventos.onChangeMostrarGrafica(valor)) }
                    )



                    MA_CheckBoxNormal(
                        modifier = Modifier.background(color = Color.Yellow),
                        titulo = "Mostrar Tabla",
                        valor = kpiUI.panelData.panelConfiguracion.mostrarTabla,
                        onValueChange = { valor -> viewModel.onEvent(DetalleKpiVM.Eventos.onChangeMosrtarTabla(valor)) }
                    )


                    MA_CheckBoxNormal(
                        titulo = "Mostrar Título Tabla",
                        valor = kpiUI.panelData.panelConfiguracion.mostrarTituloTabla,
                        onValueChange = { valor -> viewModel.onEvent(DetalleKpiVM.Eventos.onChangeMostrarOrdenado(valor)) }
                    )


                    MA_Combo(
                        titulo = "Campo Agrupacion tabla",
                        descripcion = "Campo  por el que se van a agrupar los valores cuando la tabla se encuentre limitada",
                        valorInicial = (kpiUI.panelData.panelConfiguracion.campoAgrupacionTabla).toString(),
                        elementosSeleccionables = (1..15).map { it.toString() },
                        onClickSeleccion = { str, indice ->
                            viewModel.onEvent(DetalleKpiVM.Eventos.onChangeCampoAgrupacionTabla(str))
                        })


                    MA_Combo(
                        titulo = "Campo Suma tabla",
                        descripcion = "Campo por el que se van a sumar los valores",
                        valorInicial = (kpiUI.panelData.panelConfiguracion.campoSumaValorTabla).toString(),
                        elementosSeleccionables = (1..100).map { it.toString() },
                        onClickSeleccion = { str, indice ->
                            viewModel.onEvent(DetalleKpiVM.Eventos.onChangeCampoSumaTabla(str))
                        })


                    MA_CheckBoxNormal(
                        titulo = "Ajustar contenido ancho",
                        valor = kpiUI.panelData.panelConfiguracion.ajustarContenidoAncho,
                        onValueChange = { valor -> viewModel.onEvent(DetalleKpiVM.Eventos.onChangeAjustarContenido(valor)) }
                    )

                    MA_CheckBoxNormal(
                        titulo = "Indicador Color en las filas",
                        valor = kpiUI.panelData.panelConfiguracion.indicadorColor,
                        onValueChange = { valor -> viewModel.onEvent(DetalleKpiVM.Eventos.onChangeIndicadorColor(valor)) }
                    )

                    MA_CheckBoxNormal(
                        titulo = "Filas en color",
                        valor = kpiUI.panelData.panelConfiguracion.filasColor,
                        onValueChange = { valor -> viewModel.onEvent(DetalleKpiVM.Eventos.onChangeFilasColor(valor)) }
                    )

                }

            }*/
          /*  kpiUI.reloadPanelData()
            MA_Panel(
                modifier = Modifier,
                panelData = kpiUI.panelData
            )*/

            App.log.d(kpiUI.toString())


            Column {
                MA_BotonNormal("Guardar") { viewModel.onEvent(DetalleKpiVM.Eventos.Guardar(onProcess)) }
                MA_BotonNormal("Eliminar") { viewModel.onEvent(DetalleKpiVM.Eventos.Eliminar(onProcess)) }
            }


        }
    }
}


