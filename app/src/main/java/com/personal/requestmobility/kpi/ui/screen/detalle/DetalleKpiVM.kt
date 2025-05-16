package com.personal.requestmobility.kpi.ui.screen.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.requestmobility.App
import com.personal.requestmobility.core.navegacion.RespuestaAccionCU
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
                            is Eventos.OnChangeSQL -> estado.copy(kpiUI = estado.kpiUI.copy(sql = evento.sql))

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
            App.log.c("Identificador : $id")
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