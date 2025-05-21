package com.personal.requestmobility.kpi.ui.screen.detalle

import androidx.compose.ui.focus.FocusEventModifier
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.componentes.panel.PanelData
import com.personal.requestmobility.core.composables.componentes.panel.PanelOrientacion
import com.personal.requestmobility.core.composables.componentes.panel.PanelTipoGrafica
import com.personal.requestmobility.core.navegacion.RespuestaAccionCU
import com.personal.requestmobility.core.utils.if3
import com.personal.requestmobility.kpi.domain.interactors.EliminarKpiCU
import com.personal.requestmobility.kpi.domain.interactors.GuardarKpiCU
import com.personal.requestmobility.kpi.domain.interactors.ObtenerKpiCU
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.kpi.ui.entidades.fromKPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetalleKpiVM(
    private val obtenerKpi: ObtenerKpiCU,
    private val guardarKpi: GuardarKpiCU,
    private val eliminarKpi: EliminarKpiCU,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    sealed class UIState() {
        data class Success(val kpiUI: KpiUI) : UIState()
        data class Error(val mensaje: String) : UIState()
        object Loading : UIState()
    }

    sealed class Eventos() {
        data class Cargar(val identificador: Int) : Eventos()
        data class OnChangeTitulo(val titulo: String) : Eventos()
        data class OnChangeDescripcion(val descripcion: String) : Eventos()
        data class OnChangeSQL(val sql: String) : Eventos()
        data class onChangeOrientacion(val valor: String) : Eventos()
        data class onChangeTipoGrafica(val valor: String) : Eventos()
        data class onChangeLimiteElementos(val valor: String) : Eventos()
        data class onChangeMostrarEtiquetas(val valor: Boolean) : Eventos()
        data class onChangeMostrarOrdenado(val valor: Boolean) : Eventos()
        data class onChangeEspacioGrafica(val valor: String) : Eventos()
        data class onChangeEspacioTabla(val valor: String) : Eventos()
        data class onChangeOcuparTodoEspacio(val valor: Boolean) : Eventos()

        data class onChangeAncho(val valor: String) : Eventos()
        data class onChangeAlto(val valor: String) : Eventos()

        data class onChangeMostrarGrafica(val valor: Boolean) : Eventos()
        data class onChangeMosrtarTabla(val valor: Boolean) : Eventos()
        data class onChangeMostrarTitulosTabla(val valor: Boolean) : Eventos()
        data class onChangeAgruparValores(val valor: Boolean) : Eventos()

        data class onChangeCampoAgrupacionTabla(val valor: String) : Eventos()
        data class onChangeCampoSumaTabla(val valor: String) : Eventos()

        data class onChangeAjustarContenido(val valor: Boolean) : Eventos()
        data class onChangeIndicadorColor(val valor: Boolean) : Eventos()
        data class onChangeFilasColor(val valor: Boolean) : Eventos()

        data class Guardar(val onProcess: ((RespuestaAccionCU) -> Unit)) : Eventos()
        data class Eliminar(val onProcess: ((RespuestaAccionCU) -> Unit)) : Eventos()

    }


    init {
        App.log.d("[INIT] Dentro del view model de los kpis")
    }


    fun onEvent(evento: Eventos) {
        when (evento) {
            is Eventos.Cargar -> cargar(evento.identificador)
            is Eventos.Guardar -> guardar(evento.onProcess)
            is Eventos.Eliminar -> eliminar(evento.onProcess)
            else -> {
                _uiState.update { estado ->
                    if (estado is UIState.Success) {
                        when (evento) {
                            is Eventos.OnChangeTitulo -> estado.copy(kpiUI = estado.kpiUI.copy(titulo = evento.titulo))
                            is Eventos.OnChangeDescripcion -> estado.copy(kpiUI = estado.kpiUI.copy(descripcion = evento.descripcion))
                            is Eventos.OnChangeSQL -> {

                                val k = KpiUI(id = estado.kpiUI.id,
                                    titulo =  estado.kpiUI.titulo,
                                    descripcion =  estado.kpiUI.descripcion,
                                    sql = evento.sql,
                                    panelData = estado.kpiUI.panelData,
                                    resultadoSQL = estado.kpiUI.resultadoSQL).reloadPanelData()




                                estado.copy(
                                    kpiUI = k
                                    )


                            }

                            is Eventos.onChangeOrientacion -> {
                                estado.copy(
                                    kpiUI = estado.kpiUI.copy(panelData = estado.kpiUI.panelData.copy(panelConfiguracion = estado.kpiUI.panelData.panelConfiguracion.copy(orientacion = PanelOrientacion.from(evento.valor))))
                                )
                            }

                            is Eventos.onChangeTipoGrafica -> {
                                estado.copy(
                                    kpiUI = estado.kpiUI.copy(panelData = estado.kpiUI.panelData.copy(panelConfiguracion = estado.kpiUI.panelData.panelConfiguracion.copy(tipo = PanelTipoGrafica.from(evento.valor))))
                                )
                            }

                            is Eventos.onChangeLimiteElementos -> {
                                estado.copy(
                                    kpiUI = estado.kpiUI.copy(panelData = estado.kpiUI.panelData.copy(panelConfiguracion = estado.kpiUI.panelData.panelConfiguracion.copy(limiteElementos = evento.valor.toInt())))
                                )
                            }

                            is Eventos.onChangeMostrarEtiquetas -> {
                                estado.copy(
                                    kpiUI = estado.kpiUI.copy(panelData = estado.kpiUI.panelData.copy(panelConfiguracion = estado.kpiUI.panelData.panelConfiguracion.copy(mostrarEtiquetas = evento.valor)))
                                )
                            }

                            is Eventos.onChangeMostrarOrdenado -> {
                                estado.copy(
                                    kpiUI = estado.kpiUI.copy(panelData = estado.kpiUI.panelData.copy(panelConfiguracion = estado.kpiUI.panelData.panelConfiguracion.copy(ordenado = evento.valor)))
                                )
                            }

                            is Eventos.onChangeEspacioGrafica -> {
                                estado.copy(
                                    kpiUI = estado.kpiUI.copy(panelData = estado.kpiUI.panelData.copy(panelConfiguracion = estado.kpiUI.panelData.panelConfiguracion.copy(espacioGrafica = ((evento.valor).toFloat() / 100.0f))))
                                )
                            }


                            is Eventos.onChangeEspacioTabla -> {
                                estado.copy(
                                    kpiUI = estado.kpiUI.copy(panelData = estado.kpiUI.panelData.copy(panelConfiguracion = estado.kpiUI.panelData.panelConfiguracion.copy(espacioTabla = ((evento.valor).toFloat() / 100.0f))))
                                )
                            }

                            is Eventos.onChangeAgruparValores -> {
                                estado.copy(
                                    kpiUI = estado.kpiUI.copy(panelData = estado.kpiUI.panelData.copy(panelConfiguracion = estado.kpiUI.panelData.panelConfiguracion.copy(agruparValores = evento.valor)))
                                )
                            }

                            is Eventos.onChangeMosrtarTabla -> {
                                estado.copy(
                                    kpiUI = estado.kpiUI.copy(panelData = estado.kpiUI.panelData.copy(panelConfiguracion = estado.kpiUI.panelData.panelConfiguracion.copy(mostrarTabla = evento.valor)))
                                )
                            }

                            is Eventos.onChangeMostrarGrafica -> {
                                estado.copy(
                                    kpiUI = estado.kpiUI.copy(panelData = estado.kpiUI.panelData.copy(panelConfiguracion = estado.kpiUI.panelData.panelConfiguracion.copy(mostrarGrafica = evento.valor)))
                                )
                            }

                            is Eventos.onChangeMostrarTitulosTabla -> {
                                estado.copy(
                                    kpiUI = estado.kpiUI.copy(panelData = estado.kpiUI.panelData.copy(panelConfiguracion = estado.kpiUI.panelData.panelConfiguracion.copy(mostrarTituloTabla = evento.valor)))
                                )
                            }


                            is Eventos.onChangeCampoAgrupacionTabla -> {
                                estado.copy(
                                    kpiUI = estado.kpiUI.copy(panelData = estado.kpiUI.panelData.copy(panelConfiguracion = estado.kpiUI.panelData.panelConfiguracion.copy(campoAgrupacionTabla = evento.valor.toInt())))
                                )
                            }

                            is Eventos.onChangeCampoSumaTabla -> {
                                estado.copy(
                                    kpiUI = estado.kpiUI.copy(panelData = estado.kpiUI.panelData.copy(panelConfiguracion = estado.kpiUI.panelData.panelConfiguracion.copy(campoSumaValorTabla = evento.valor.toInt())))
                                )
                            }

                            is Eventos.onChangeAjustarContenido -> {
                                estado.copy(
                                    kpiUI = estado.kpiUI.copy(panelData = estado.kpiUI.panelData.copy(panelConfiguracion = estado.kpiUI.panelData.panelConfiguracion.copy(ajustarContenidoAncho = evento.valor)))
                                )
                            }

                            is Eventos.onChangeIndicadorColor -> {
                                estado.copy(
                                    kpiUI = estado.kpiUI.copy(panelData = estado.kpiUI.panelData.copy(panelConfiguracion = estado.kpiUI.panelData.panelConfiguracion.copy(indicadorColor = evento.valor)))
                                )
                            }

                            is Eventos.onChangeFilasColor -> {
                                estado.copy(
                                    kpiUI = estado.kpiUI.copy(panelData = estado.kpiUI.panelData.copy(panelConfiguracion = estado.kpiUI.panelData.panelConfiguracion.copy(filasColor = evento.valor)))
                                )
                            }

                            is Eventos.onChangeAlto -> {
                                estado.copy(
                                    kpiUI = estado.kpiUI.copy(panelData = estado.kpiUI.panelData.copy(panelConfiguracion = estado.kpiUI.panelData.panelConfiguracion.copy(height = (evento.valor.toInt()).dp)))
                                )
                            }

                            is Eventos.onChangeAncho -> {
                                estado.copy(
                                    kpiUI = estado.kpiUI.copy(panelData = estado.kpiUI.panelData.copy(panelConfiguracion = estado.kpiUI.panelData.panelConfiguracion.copy(width = (evento.valor.toInt()).dp)))
                                )
                            }


                            is Eventos.onChangeOcuparTodoEspacio -> {
                                estado.copy(
                                    kpiUI = estado.kpiUI.copy(panelData = estado.kpiUI.panelData.copy(panelConfiguracion = estado.kpiUI.panelData.panelConfiguracion.copy(ocuparTodoEspacio = evento.valor)))
                                )
                            }


                            else -> estado
                        }
                    } else {
                        estado
                    }
                }
            }
        }
    }

    private fun cargar(identificador: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (identificador == 0) {
                    _uiState.value = UIState.Success(kpiUI = KpiUI())
                } else {
                    val kpiUI: KpiUI = KpiUI().fromKPI(obtenerKpi.obtener(id = identificador))
                    _uiState.value = UIState.Success(kpiUI = kpiUI)
                }
            }
        }
    }


    private fun guardar(onProcess: ((RespuestaAccionCU) -> Unit)) {
        viewModelScope.launch {
            val id: Long = async(Dispatchers.IO) {
                val kpiUI = (_uiState.value as UIState.Success).kpiUI
                guardarKpi.guardar(kpiUI)
            }.await()

            onProcess(RespuestaAccionCU(id > 0, "Guardado $id"))
        }
    }

    private fun eliminar(onProcess: ((RespuestaAccionCU) -> Unit)) {
        viewModelScope.launch {
            async(Dispatchers.IO) {
                val kpiUI = (_uiState.value as UIState.Success).kpiUI
                eliminarKpi.eliminar(kpiUI)
            }.await()
            onProcess(RespuestaAccionCU(true, "Eliinado"))
        }
    }
}