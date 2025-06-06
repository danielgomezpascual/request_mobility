package com.personal.requestmobility.dashboards.ui.screen.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.requestmobility.App
import com.personal.requestmobility.dashboards.ui.entidades.SeleccionPanelUI
import com.personal.requestmobility.dashboards.domain.interactors.CargarDashboardCU
import com.personal.requestmobility.dashboards.domain.interactors.EliminarDashboardCU
import com.personal.requestmobility.dashboards.domain.interactors.GuardarDashboardCU
import com.personal.requestmobility.dashboards.domain.interactors.ObtenerSeleccionPanel
import com.personal.requestmobility.dashboards.ui.entidades.DashboardUI
import com.personal.requestmobility.dashboards.ui.entidades.fromDashboard
import com.personal.requestmobility.dashboards.ui.entidades.toDashboard
import com.personal.requestmobility.paneles.ui.entidades.PanelUI
import com.personal.requestmobility.paneles.ui.entidades.fromPanel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetalleDashboardVM(
    private val cargarDashboardCU: CargarDashboardCU,
    private val eliminarDashboardCU: EliminarDashboardCU,
    private val guardarDashboardCU: GuardarDashboardCU,
    private val obtenerSeleccionPanel: ObtenerSeleccionPanel

) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading("Cargando..."))
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()


    private var listaPaneles: List<SeleccionPanelUI> = emptyList()

    sealed class UIState {
        data class Success(val dashboardUI: DashboardUI) : UIState()
        data class Error(val mensaje: String) : UIState()
        data class Loading(val mensaje: String) : UIState()
    }

    sealed class Eventos {

        data class Cargar(val identificador: Int) : Eventos()
        object Eliminar : Eventos() // Renombrado para claridad
        object Guardar : Eventos()  // Renombrado para claridad

        data class OnChangeNombre(val valor: String) : Eventos()     // Adaptado desde OnChangeItem
        data class OnChangeDescripcion(val valor: String) : Eventos() // Adaptado desde OnChangeProveedor
        data class OnChangeInicial(val valor: Boolean) : Eventos() //
        data class OnActualizarPaneles(val panelesUI: List<PanelUI>) : Eventos() // Adaptado desde OnChangeProveedor
        data class ActualizarLogo(val rutaLogo: String) : Eventos() // Adaptado desde OnChangeProveedor
        // Los otros OnChange (global, codigoOrganizacion, codigo) no aplican a Dashboard
    }


    fun onEvento(eventos: Eventos) {
        when (eventos) {
            is Eventos.Cargar -> cargar(eventos.identificador)
            Eventos.Eliminar -> eliminar()
            Eventos.Guardar -> guardar()
            else -> { // Manejo de eventos OnChange
                _uiState.update { estado ->
                    if (estado is UIState.Success) {
                        when (eventos) {
                            is Eventos.OnChangeNombre -> estado.copy(dashboardUI = estado.dashboardUI.copy(nombre = eventos.valor))
                            is Eventos.OnChangeDescripcion -> estado.copy(dashboardUI = estado.dashboardUI.copy(descripcion = eventos.valor))
                            is Eventos.OnActualizarPaneles -> {
                                estado.copy(dashboardUI = estado.dashboardUI.copy(listaPaneles = eventos.panelesUI))
                            }

                            is Eventos.ActualizarLogo -> estado.copy(dashboardUI = estado.dashboardUI.copy(logo = eventos.rutaLogo))
                            is Eventos.OnChangeInicial -> estado.copy(dashboardUI = estado.dashboardUI.copy(home = eventos.valor))
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
        //  _uiState.value = UIState.Loading("Cargando Dashboard...")

        viewModelScope.launch {


            App.log.d("ID-> $id")
            try {


                obtenerSeleccionPanel.obtenerTodos().map { lp -> lp.map { panel -> PanelUI().fromPanel(panel) } }
                    .flowOn(Dispatchers.IO)
                    .catch { ex -> _uiState.update { UIState.Error(ex.toString()) } }
                    .collect { paneles ->
                        _uiState.update { estado ->
                            if (id != 0) {
                                val ds: DashboardUI = DashboardUI().fromDashboard(cargarDashboardCU.cargar(id))
                                val panelesSeleccionados: List<PanelUI> = ds.listaPaneles
                                UIState.Success(dashboardUI = ds.copy(listaPaneles =panelesSeleccionados))
                            } else {
                                UIState.Success(dashboardUI = DashboardUI(listaPaneles = paneles))
                            }
                        }
                    }


            } catch (e: Exception) {
                _uiState.value = UIState.Error(e.message ?: "Error desconocido al cargar")
            }
        }
    }

    private fun eliminar() {
        viewModelScope.launch {
            try { // El ejemplo no tiene try-catch aquí, pero es buena práctica
                withContext(Dispatchers.IO) {
                    val dashboardUi = (_uiState.value as UIState.Success).dashboardUI
                    eliminarDashboardCU.eliminar(dashboardUi.toDashboard())
                }
            } catch (e: Exception) {
                _uiState.value = UIState.Error("Error al eliminar: ${e.message}")
            }
        }
    }

    private fun guardar() {

        viewModelScope.launch {
            try { // El ejemplo no tiene try-catch aquí, pero es buena práctica
                withContext(Dispatchers.IO) {
                    val dashboardUi = (_uiState.value as UIState.Success).dashboardUI
                    guardarDashboardCU.guardar(dashboardUi.toDashboard())
                }
            } catch (e: Exception) {
                _uiState.value = UIState.Error("Error al guardar: ${e.message}")
            }
        }
    }


}