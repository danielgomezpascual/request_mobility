package com.personal.requestmobility.kpi.ui.screen.detalle


import MA_IconBottom
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.requestmobility.core.composables.edittext.MA_TextoNormal
import com.personal.requestmobility.core.composables.formas.MA_Avatar
import com.personal.requestmobility.core.composables.labels.MA_Titulo
import com.personal.requestmobility.core.composables.labels.MA_Titulo2
import com.personal.requestmobility.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.requestmobility.core.composables.tabla.MA_Tabla
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.core.screen.ErrorScreen
import com.personal.requestmobility.core.screen.LoadingScreen
import com.personal.requestmobility.kpi.ui.screen.detalle.DetalleKpiVM.UIState
import com.personal.requestmobility.menu.Features
import com.personal.requestmobility.transacciones.domain.entidades.ResultadoSQL
import org.koin.androidx.compose.koinViewModel


@Composable
fun DetalleKpiScreen(identificador: Int,
                     viewModel: DetalleKpiVM = koinViewModel(),
                     navegacion: (EventosNavegacion) -> Unit) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(DetalleKpiVM.Eventos.Cargar(identificador))
    }


    when (uiState) {
        is UIState.Error -> ErrorScreen((uiState as UIState.Error).mensaje)
        UIState.Loading -> LoadingScreen()
        is UIState.Success -> SuccessScreenDetalleKpi(
            viewModel,
            (uiState as UIState.Success),
            navegacion
        )

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessScreenDetalleKpi(viewModel: DetalleKpiVM,
                            uiState: UIState.Success,
                            navegacion: (EventosNavegacion) -> Unit) {

    val kpiUI = uiState.kpiUI

    MA_ScaffoldGenerico(
        titulo = "",
        navegacion = { },
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
                        onClick = {
                            navegacion(EventosNavegacion.MenuKpis)
                        }
                    )

                    MA_IconBottom(
                        modifier = Modifier.weight(1f),
                        icon = Features.Paneles().icono,
                        labelText = "Crear Panel",

                        onClick = {
                            viewModel.onEvent(DetalleKpiVM.Eventos.CrearPanel(navegacion))
                            //navegacion(EventosNavegacion.MenuApp)
                        }
                    )
                    
                    MA_IconBottom(
                            modifier = Modifier.weight(1f),
                            icon = Features.Duplicar().icono,
                            labelText = Features.Duplicar().texto,
                                                        onClick = {
                                viewModel.onEvent(DetalleKpiVM.Eventos.DuplicarKpi(navegacion))
                                //navegacion(EventosNavegacion.MenuApp)
                            }
                                 )


                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )

                    MA_IconBottom(
                        modifier = Modifier.weight(1f),
                        icon = Features.Eliminar().icono,
                        labelText = Features.Eliminar().texto,
                        color = Features.Eliminar().color,
                        onClick = {
                            viewModel.onEvent(DetalleKpiVM.Eventos.Eliminar(navegacion))
                            // navegacion(EventosNavegacion.MenuKpis)
                        }
                    )

                    MA_IconBottom(
                        modifier = Modifier.weight(1f),
                        icon = Features.Guardar().icono,
                        labelText = Features.Guardar().texto,
                        color = Features.Guardar().color,
                        onClick = {
                            viewModel.onEvent(DetalleKpiVM.Eventos.Guardar(navegacion))
                          //  navegacion(EventosNavegacion.MenuKpis)
                        }
                    )


                }


            }


        },
        contenido = {

            val scrollState = rememberScrollState() // 1. Recuerda el estado del scroll
            Column(modifier = Modifier.verticalScroll(scrollState)) {

                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    MA_Avatar(kpiUI.titulo)
                    MA_Titulo(kpiUI.titulo)
                }

                val visible: Boolean = false
                if (visible) {
                    MA_TextoNormal(
                        valor = kpiUI.id.toString(),
                        titulo = "ID",
                        onValueChange = { valor -> null }
                    )
                }



                MA_TextoNormal(
                    valor = kpiUI.titulo, titulo = "Nombre",
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


                Column(Modifier.height(400.dp)) {
                    MA_Titulo2("Resultado")
                    MA_Tabla(
                        filas = ResultadoSQL.fromSqlToTabla(kpiUI.sql).filas
                    ) { }


                }


            }

        }
    )


}
