package com.personal.requestmobility.paneles.ui.screen.detalle

import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.requestmobility.App
import com.personal.requestmobility.R
import com.personal.requestmobility.core.composables.dialogos.DialogManager
import com.personal.requestmobility.core.composables.dialogos.DialogosResultado
import com.personal.requestmobility.core.composables.tabla.ValoresTabla
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.core.utils._t
import com.personal.requestmobility.core.utils.siVacio
import com.personal.requestmobility.paneles.domain.entidades.PanelOrientacion
import com.personal.requestmobility.paneles.domain.entidades.PanelTipoGrafica
import com.personal.requestmobility.kpi.domain.interactors.ObtenerKpisCU
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.kpi.ui.entidades.fromKPI
import com.personal.requestmobility.paneles.domain.interactors.EliminarPanelCU
import com.personal.requestmobility.paneles.domain.interactors.GuardarPanelCU
import com.personal.requestmobility.paneles.domain.interactors.ObtenerPanelCU
import com.personal.requestmobility.paneles.ui.entidades.Condiciones
import com.personal.requestmobility.paneles.ui.entidades.PanelUI
import com.personal.requestmobility.paneles.ui.entidades.fromPanel
import com.personal.requestmobility.transacciones.domain.entidades.ResultadoSQL
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
    private val dialog: DialogManager,

    ) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    sealed class UIState() {
        data class Success(val panelUI: PanelUI,
                           val kpiDisponibles: List<KpiUI> = emptyList<KpiUI>(),
                           val valoresTabla: ValoresTabla = ValoresTabla()

        ) : UIState()

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
        data class OnChangeAgruparResto(val valor: Boolean) : Eventos()
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
        data class onChangeEsquemaColores(val valor: Int) : Eventos()

        data class Preview(val navegacion: (EventosNavegacion) -> Unit) : Eventos()
        data class Guardar(val navegacion: (EventosNavegacion) -> Unit) : Eventos()
        data class Eliminar(val navegacion: (EventosNavegacion) -> Unit) : Eventos()


        object ObtenerKpisDisponibles : Eventos()

        data class AgregarCondicion(val condicion: Condiciones) : Eventos()
        data class EliminarCondicion(val condicion: Condiciones) : Eventos()
    }


    fun onEvent(evento: Eventos) {
        when (evento) {
            is Eventos.ObtenerKpisDisponibles -> obtenerKpis()
            is Eventos.Cargar -> cargar(evento.identificador)
            is Eventos.Guardar -> guardar(evento.navegacion)
            is Eventos.Eliminar -> eliminar(evento.navegacion)
            is Eventos.Preview -> preview(evento.navegacion)
            else -> {
                _uiState.update { estado ->
                    if (estado is UIState.Success) {
                        when (evento) {
                            is Eventos.OnChangeTitulo -> {
                                estado.copy(
                                    panelUI = estado.panelUI.copy
                                        (titulo = evento.titulo, configuracion = estado.panelUI.configuracion.copy(titulo = evento.titulo))
                                )


                            }

                            is Eventos.OnChangeDescripcion -> {
                                estado.copy(
                                    panelUI = estado.panelUI.copy
                                        (descripcion = evento.descripcion, configuracion = estado.panelUI.configuracion.copy(descripcion = evento.descripcion))
                                )
                            }

                            is Eventos.OnChangeKpiSeleccionado -> {
                                val kpi = estado.kpiDisponibles.first { it.id == evento.identificador }

                                val valoresTabla = ResultadoSQL.fromSqlToTabla(kpi.sql)
                                estado.copy(
                                    valoresTabla = valoresTabla,
                                    panelUI = estado.panelUI.copy(kpi = kpi)

                                )
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

                            is Eventos.OnChangeAgruparResto -> {
                                estado.copy(
                                    panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(agruparResto = evento.valor))
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
                                    panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(columnaX = evento.valor.toInt()))
                                )

                            }

                            is Eventos.onChangeCampoSumaTabla -> {
                                estado.copy(
                                    panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(columnaY = evento.valor.toInt()))
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

                            is Eventos.onChangeEsquemaColores -> {
                                estado.copy(
                                    panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(colores = evento.valor))
                                )

                            }


                            is Eventos.AgregarCondicion -> {
                                var condiciones: List<Condiciones> = estado.panelUI.configuracion.condiciones
                                condiciones = condiciones.plus(evento.condicion)
                                estado.copy(
                                    panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(condiciones = condiciones))
                                )
                            }

                            is Eventos.EliminarCondicion -> {
                                var condiciones: List<Condiciones> = estado.panelUI.configuracion.condiciones
                                val nuevasCondiciones = condiciones.filter { it.id != evento.condicion.id }
                                App.log.d("Conficiones ${condiciones.size} - ${nuevasCondiciones.size}")
                                estado.copy(
                                    panelUI = estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(condiciones = nuevasCondiciones))
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


    private fun preview(navegacion: (EventosNavegacion) -> Unit) {
        if (_uiState is UIState.Success) {


            _uiState.update { estado ->
                if (estado is UIState.Success) {
                    val kpi = estado.panelUI.kpi
                    val titulo = (estado.panelUI.configuracion.titulo).siVacio(kpi.titulo)
                    val descripcion = (estado.panelUI.configuracion.descripcion).siVacio(kpi.descripcion)
                    estado.copy(estado.panelUI.copy(configuracion = estado.panelUI.configuracion.copy(titulo = titulo, descripcion = descripcion)))

                } else {
                    estado
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
                    val valoresTabla = ResultadoSQL.fromSqlToTabla(panelUI.kpi.sql)
                    _uiState.value = UIState.Success(panelUI = panelUI, valoresTabla = valoresTabla)
                }
            }
        }
    }

    fun obtenerKpis() {

        viewModelScope.launch {
            obtenerKpis.getAll().map { listaKpi -> listaKpi.map { k -> KpiUI().fromKPI(k) } }
                .flowOn(Dispatchers.IO)
                .catch { ex -> _uiState.update { UIState.Error(ex.toString()) } }
                .collect { listakpi ->
                    _uiState.update { estado ->
                        if (estado is UIState.Success) {
                            estado.copy(kpiDisponibles = listakpi)
                        } else {
                            estado
                        }
                    }
                }


        }
    }

    private fun guardar(navegacion: (EventosNavegacion) -> Unit) {
        viewModelScope.launch {
            val panelUI = (_uiState.value as UIState.Success).panelUI
            guardarPanelCU.guardar(panelUI)
            dialog.informacion(_t(R.string.elemento_guardado_correctamente)) { navegacion(EventosNavegacion.MenuPaneles) }

        }
    }

    private fun eliminar(navegacion: (EventosNavegacion) -> Unit) {
        dialog.sino(_t(R.string.seguro_que_desea_elimnar_el_panel_seleccionado)) { resp ->
            if (resp == DialogosResultado.Si) {
                viewModelScope.launch {
                    async(Dispatchers.IO) {
                        val panelUI = (_uiState.value as UIState.Success).panelUI
                        eliminarPanelCU.eliminar(panelUI)
                    }.await()
                    dialog.informacion(_t(R.string.elemnetos_eliminado)) { navegacion(EventosNavegacion.MenuPaneles) }
                }
            }
        }

    }
}