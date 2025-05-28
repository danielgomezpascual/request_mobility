package com.personal.requestmobility.dashboards.ui.screen.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.requestmobility.App
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.dashboards.ui.entidades.KpiSeleccionPanel
import com.personal.requestmobility.dashboards.domain.interactors.CargarDashboardCU
import com.personal.requestmobility.dashboards.domain.interactors.EliminarDashboardCU
import com.personal.requestmobility.dashboards.domain.interactors.GuardarDashboardCU
import com.personal.requestmobility.dashboards.domain.interactors.ObtenerKpisSeleccionPanel
import com.personal.requestmobility.dashboards.ui.entidades.DashboardUI
import com.personal.requestmobility.dashboards.ui.entidades.fromDashboard
import com.personal.requestmobility.dashboards.ui.entidades.toDashboard

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetalleDashboardVM(
    private val cargarDashboardCU: CargarDashboardCU,
    private val eliminarDashboardCU: EliminarDashboardCU,
    private val guardarDashboardCU: GuardarDashboardCU,
    private val obtenerKpisSeleccionPanel: ObtenerKpisSeleccionPanel
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading("Cargando..."))
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()


    private var listaKpiPanel: List<KpiSeleccionPanel> = emptyList()

    sealed class UIState {
        data class Success(val dashboardUI: DashboardUI) : UIState()
        data class Error(val mensaje: String) : UIState()
        data class Loading(val mensaje: String) : UIState()
    }

    sealed class Eventos {
        data class Cargar(val identificador: Int) : Eventos()
        data class Eliminar(val dashboardUI: DashboardUI, val navegacionExterna: (EventosNavegacion) -> Unit) : Eventos() // Renombrado para claridad
        data class Guardar(val dashboardUI: DashboardUI, val navegacionExterna: (EventosNavegacion) -> Unit) : Eventos()  // Renombrado para claridad
        data class OnChangeNombre(val valor: String) : Eventos()     // Adaptado desde OnChangeItem
        data class OnChangeDescripcion(val valor: String) : Eventos() // Adaptado desde OnChangeProveedor
        data class OnActualizarPaneles(val paneles: List<KpiSeleccionPanel>) : Eventos() // Adaptado desde OnChangeProveedor
        data class ActualizarLogo(val rutaLogo: String) : Eventos() // Adaptado desde OnChangeProveedor
        // Los otros OnChange (global, codigoOrganizacion, codigo) no aplican a Dashboard
    }


    fun onEvento(eventos: Eventos) {
        when (eventos) {
            is Eventos.Cargar -> cargar(eventos.identificador)
            is Eventos.Eliminar -> eliminar(eventos.dashboardUI, eventos.navegacionExterna)
            is Eventos.Guardar -> guardar(eventos.dashboardUI, eventos.navegacionExterna)
            else -> { // Manejo de eventos OnChange
                _uiState.update { estado ->
                    if (estado is UIState.Success) {
                        when (eventos) {
                            is Eventos.OnChangeNombre -> estado.copy(dashboardUI = estado.dashboardUI.copy(nombre = eventos.valor))
                            is Eventos.OnChangeDescripcion -> estado.copy(dashboardUI = estado.dashboardUI.copy(descripcion = eventos.valor))
                            is Eventos.OnActualizarPaneles -> estado.copy(dashboardUI = estado.dashboardUI.copy(listaPaneles = eventos.paneles))
                            is Eventos.ActualizarLogo -> estado.copy(dashboardUI = estado.dashboardUI.copy(logo = eventos.rutaLogo))
                            else -> estado // No debería llegar aquí si los eventos están bien definidos
                        }
                    } else {
                        estado // No modificar si no es Success
                    }
                }
            }
        }
    }

    private fun cargar(id: Int) {
        _uiState.value = UIState.Loading("Cargando Dashboard...")

        viewModelScope.launch {


            try {
                obtenerKpisSeleccionPanel.obtenerTodos().collect { listaSeleccionPanel -> listaKpiPanel = listaSeleccionPanel }
                val ds: DashboardUI = if (id == 0) {
                    DashboardUI(listaPaneles = listaKpiPanel)
                } else {
                    val dashboardDomain = cargarDashboardCU.cargar(id)
                    val dashboardUi = DashboardUI().fromDashboard(dashboardDomain)
                    val ks = listaKpiPanel.map { kpi ->
                        if (dashboardUi.listaPaneles.filter { it.seleccionado && it.identificador == kpi.identificador }.isNotEmpty()){
                             kpi.copy(seleccionado = true)
                        }else{
                            kpi
                        }
                    }

                    dashboardUi.copy(listaPaneles = ks)
                }

                _uiState.value = UIState.Success(dashboardUI = ds)


            } catch (e: Exception) {
                _uiState.value = UIState.Error(e.message ?: "Error desconocido al cargar")
            }
        }
    }

    private fun eliminar(dashboardUi: DashboardUI, navegacionExterna: (EventosNavegacion) -> Unit) {
        viewModelScope.launch {
            try { // El ejemplo no tiene try-catch aquí, pero es buena práctica
                withContext(Dispatchers.IO) {
                    eliminarDashboardCU.eliminar(dashboardUi.toDashboard())
                }
                navegarAtras(navegacionExterna) // Llama a la función de navegar atrás
            } catch (e: Exception) {
                _uiState.value = UIState.Error("Error al eliminar: ${e.message}")
            }
        }
    }

    private fun guardar(dashboardUi: DashboardUI, navegacionExterna: (EventosNavegacion) -> Unit) {

        App.log.lista("Paneles", dashboardUi.listaPaneles)
        viewModelScope.launch {
            if (dashboardUi.nombre.isBlank()) { // Validación simple
                _uiState.value = UIState.Error("El nombre no puede estar vacío.")
                return@launch
            }
            try { // El ejemplo no tiene try-catch aquí, pero es buena práctica
                withContext(Dispatchers.IO) {
                    guardarDashboardCU.guardar(dashboardUi.toDashboard())
                }
                // El ejemplo original llama directamente a navegacionExterna(EventosNavegacion.Volver)
                navegacionExterna(EventosNavegacion.Volver)
            } catch (e: Exception) {
                _uiState.value = UIState.Error("Error al guardar: ${e.message}")
            }
        }
    }

    fun navegarAtras(navegacion: (EventosNavegacion) -> Unit) { // Como en el ejemplo
        navegacion(EventosNavegacion.Volver)
    }
}