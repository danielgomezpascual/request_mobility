package com.personal.requestmobility.paneles.ui.screen.detalle

import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.requestmobility.App
import com.personal.requestmobility.paneles.domain.entidades.PanelOrientacion
import com.personal.requestmobility.paneles.domain.entidades.PanelTipoGrafica
import com.personal.requestmobility.core.navegacion.RespuestaAccionCU
import com.personal.requestmobility.kpi.domain.interactors.EliminarKpiCU
import com.personal.requestmobility.kpi.domain.interactors.GuardarKpiCU
import com.personal.requestmobility.kpi.domain.interactors.ObtenerKpiCU
import com.personal.requestmobility.kpi.domain.interactors.ObtenerKpisCU
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.kpi.ui.entidades.fromKPI
import com.personal.requestmobility.paneles.domain.interactors.EliminarPanelCU
import com.personal.requestmobility.paneles.domain.interactors.GuardarPanelCU
import com.personal.requestmobility.paneles.domain.interactors.ObtenerPanelCU
import com.personal.requestmobility.paneles.ui.entidades.PanelUI
import com.personal.requestmobility.paneles.ui.entidades.fromPanel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetallePanelVM(
    private val obtenerPanelCU: ObtenerPanelCU,
    private val guardarPanelCU: GuardarPanelCU,
    private val eliminarPanelCU: EliminarPanelCU,
    private val obtenerKpis: ObtenerKpisCU,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    sealed class UIState() {
        data class Success(val panelUI: PanelUI, val kpiDisponibles : List<KpiUI> = emptyList<KpiUI>()) : UIState()
        data class Error(val mensaje: String) : UIState()
        object Loading : UIState()
    }

    sealed class Eventos() {
        data class Cargar(val identificador: Int) : Eventos()
        data class OnChangeTitulo(val titulo: String) : Eventos()
        data class OnChangeDescripcion(val descripcion: String) : Eventos()
        data class OnChangeKpiSeleccionado(val identificador: Int) : Eventos()

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


        object ObtenerKpisDisponibles : Eventos()

    }


    init {
        App.log.d("[INIT] Dentro del view model de los kpis")
    }




        fun onEvent(evento: Eventos) {
            when (evento) {
                is Eventos.ObtenerKpisDisponibles -> obtenerKpis()
                is Eventos.Cargar -> cargar(evento.identificador)
                is Eventos.Guardar -> guardar(evento.onProcess)
                is Eventos.Eliminar -> eliminar(evento.onProcess)
                else -> {
                    _uiState.update { estado ->
                        if (estado is UIState.Success) {
                            when (evento) {
                                is Eventos.OnChangeTitulo -> estado.copy(panelUI = estado.panelUI.copy(titulo = evento.titulo))
                                is Eventos.OnChangeDescripcion -> estado.copy(panelUI = estado.panelUI.copy(descripcion = evento.descripcion))
                                is Eventos.OnChangeKpiSeleccionado ->{
                                    estado.copy(panelUI = estado.panelUI.copy(kpi = estado.kpiDisponibles.first { it.id == evento.identificador }))
                                }


                                is Eventos.onChangeOrientacion -> {
                                    estado.copy(
                                        panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(orientacion = PanelOrientacion.from(evento.valor)))
                                    )
                                }

                                is Eventos.onChangeTipoGrafica -> {
                                    estado.copy(
                                        panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(tipo = PanelTipoGrafica.from(evento.valor)))
                                    )
                                }

                                is Eventos.onChangeLimiteElementos -> {
                                    estado.copy(
                                        panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(limiteElementos = evento.valor.toInt()))
                                    )
                                }

                                is Eventos.onChangeMostrarEtiquetas -> {
                                    estado.copy(
                                        panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(mostrarEtiquetas = evento.valor))
                                    )
                                }

                                is Eventos.onChangeMostrarOrdenado -> {
                                    estado.copy(
                                        panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(ordenado = evento.valor))
                                    )

                                }

                                is Eventos.onChangeEspacioGrafica -> {
                                    estado.copy(
                                        panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(espacioGrafica = ((evento.valor).toFloat() / 100.0f)))
                                    )

                                }


                                is Eventos.onChangeEspacioTabla -> {
                                    estado.copy(
                                        panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(espacioTabla = ((evento.valor).toFloat() / 100.0f)))
                                    )

                                }

                                is Eventos.onChangeAgruparValores -> {
                                    estado.copy(
                                        panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(agruparValores = evento.valor))
                                    )

                                }

                                is Eventos.onChangeMosrtarTabla -> {
                                    estado.copy(
                                        panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(mostrarTabla = evento.valor))
                                    )

                                }

                                is Eventos.onChangeMostrarGrafica -> {
                                    estado.copy(
                                        panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(mostrarGrafica = evento.valor))
                                    )

                                }

                                is Eventos.onChangeMostrarTitulosTabla -> {
                                    estado.copy(
                                        panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(mostrarTituloTabla = evento.valor))
                                    )

                                }


                                is Eventos.onChangeCampoAgrupacionTabla -> {
                                    estado.copy(
                                        panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(campoAgrupacionTabla = evento.valor.toInt()))
                                    )

                                }

                                is Eventos.onChangeCampoSumaTabla -> {
                                    estado.copy(
                                        panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(campoSumaValorTabla = evento.valor.toInt()))
                                    )

                                }

                                is Eventos.onChangeAjustarContenido -> {
                                    estado.copy(
                                        panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(ajustarContenidoAncho = evento.valor))
                                    )

                                }

                                is Eventos.onChangeIndicadorColor -> {
                                    estado.copy(
                                        panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(indicadorColor = evento.valor))
                                    )

                                }

                                is Eventos.onChangeFilasColor -> {
                                    estado.copy(
                                        panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(filasColor = evento.valor))
                                    )

                                }

                                is Eventos.onChangeAlto -> {
                                    estado.copy(
                                        panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(height = (evento.valor.toInt()).dp))
                                    )

                                }

                                is Eventos.onChangeAncho -> {
                                    estado.copy(
                                        panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(width = (evento.valor.toInt()).dp))
                                    )

                                }


                                is Eventos.onChangeOcuparTodoEspacio -> {
                                    estado.copy(
                                        panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(ocuparTodoEspacio = evento.valor))
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
                    obtenerKpis()
                    if (identificador == 0) {
                        _uiState.value = UIState.Success(panelUI = PanelUI())
                    } else {
                        val panelUI: PanelUI = PanelUI().fromPanel(obtenerPanelCU.obtener(id = identificador))
                        _uiState.value = UIState.Success(panelUI = panelUI)
                    }
                }
            }
        }
    fun obtenerKpis() {

        viewModelScope.launch {
            obtenerKpis.getAll().map { listaKpi -> listaKpi.map { k  -> KpiUI().fromKPI(k) } }
                .flowOn(Dispatchers.IO)
                .catch { ex ->  _uiState.update { UIState.Error(ex.toString()) }}
                .collect { listakpi ->
                    _uiState.update { estado  ->
                        if (estado is UIState.Success){
                            estado.copy(kpiDisponibles =  listakpi)
                        }else{
                            estado
                        }
                    }
                }


        }
    }

        private fun guardar(onProcess: ((RespuestaAccionCU) -> Unit)) {
            viewModelScope.launch {
                val id: Long = async(Dispatchers.IO) {
                    val panelUI = (_uiState.value as UIState.Success).panelUI
                    guardarPanelCU.guardar(panelUI)
                }.await()

                onProcess(RespuestaAccionCU(id > 0, "Guardado $id"))
            }
        }

        private fun eliminar(onProcess: ((RespuestaAccionCU) -> Unit)) {
            viewModelScope.launch {
                async(Dispatchers.IO) {
                    val panelUI = (_uiState.value as UIState.Success).panelUI
                    eliminarPanelCU.eliminar(panelUI)
                }.await()
                onProcess(RespuestaAccionCU(true, "Eliinado"))
            }
        }
    }