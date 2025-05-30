package com.personal.requestmobility.kpi.ui.screen.detalle

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
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.kpi.ui.entidades.fromKPI
import com.personal.requestmobility.kpi.ui.entidades.toKpi
import com.personal.requestmobility.menu.Features
import com.personal.requestmobility.paneles.domain.entidades.Panel
import com.personal.requestmobility.paneles.domain.entidades.PanelConfiguracion
import com.personal.requestmobility.paneles.domain.interactors.GuardarPanelCU
import com.personal.requestmobility.paneles.ui.entidades.PanelUI
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

    private val guardarPanel: GuardarPanelCU

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

        object Guardar : Eventos()
        object Eliminar : Eventos()
        object CrearPanel : Eventos()

    }


    init {
        App.log.d("[INIT] Dentro del view model de los kpis")
    }


    fun onEvent(evento: Eventos) {
        when (evento) {
            is Eventos.Cargar -> cargar(evento.identificador)
            Eventos.Guardar -> guardar()
            Eventos.Eliminar -> eliminar()
            Eventos.CrearPanel -> crearPanel()

            else -> {
                _uiState.update { estado ->
                    if (estado is UIState.Success) {
                        when (evento) {
                            is Eventos.OnChangeTitulo -> estado.copy(kpiUI = estado.kpiUI.copy(titulo = evento.titulo))
                            is Eventos.OnChangeDescripcion -> estado.copy(kpiUI = estado.kpiUI.copy(descripcion = evento.descripcion))
                            is Eventos.OnChangeSQL -> {

                                val k = KpiUI(
                                    id = estado.kpiUI.id,
                                    titulo = estado.kpiUI.titulo,
                                    descripcion = estado.kpiUI.descripcion,
                                    sql = evento.sql
                                )
                                /*panelData = estado.kpiUI.panelData,
                                resultadoSQL = estado.kpiUI.resultadoSQL).reloadPanelData()*/




                                estado.copy(
                                    kpiUI = k
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

     private fun crearPanel() {

        if (_uiState.value is UIState.Success) {
            viewModelScope.launch {
                val kpiUI: KpiUI = (_uiState.value as UIState.Success).kpiUI
                val panelUI: PanelUI = PanelUI(id = 0, titulo = kpiUI.titulo, descripcion = kpiUI.descripcion, configuracion = PanelConfiguracion(), kpiUI)
                guardarPanel.guardar(panelUI)
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


    private fun guardar() {
        viewModelScope.launch {
            val id: Long = async(Dispatchers.IO) {
                val kpiUI = (_uiState.value as UIState.Success).kpiUI
                guardarKpi.guardar(kpiUI)
            }.await()

//onProcess(RespuestaAccionCU(id > 0, "Guardado $id"))
        }
    }

    private fun eliminar() {
        viewModelScope.launch {
            async(Dispatchers.IO) {
                val kpiUI = (_uiState.value as UIState.Success).kpiUI
                eliminarKpi.eliminar(kpiUI)
            }.await()
            //   onProcess(RespuestaAccionCU(true, "Eliinado"))
        }
    }
}