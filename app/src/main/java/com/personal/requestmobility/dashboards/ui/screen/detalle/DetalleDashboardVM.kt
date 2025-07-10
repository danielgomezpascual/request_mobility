package com.personal.requestmobility.dashboards.ui.screen.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.requestmobility.App
import com.personal.requestmobility.R
import com.personal.requestmobility.core.composables.dialogos.DialogManager
import com.personal.requestmobility.core.composables.dialogos.DialogosResultado
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.core.utils._t
import com.personal.requestmobility.dashboards.domain.entidades.TipoDashboard
import com.personal.requestmobility.dashboards.ui.entidades.SeleccionPanelUI
import com.personal.requestmobility.dashboards.domain.interactors.CargarDashboardCU
import com.personal.requestmobility.dashboards.domain.interactors.EliminarDashboardCU
import com.personal.requestmobility.dashboards.domain.interactors.GuardarDashboardCU
import com.personal.requestmobility.dashboards.domain.interactors.ObtenerSeleccionPanel
import com.personal.requestmobility.dashboards.ui.entidades.DashboardUI
import com.personal.requestmobility.dashboards.ui.entidades.fromDashboard
import com.personal.requestmobility.dashboards.ui.entidades.toDashboard
import com.personal.requestmobility.kpi.domain.interactors.ObtenerKpisCU
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.kpi.ui.entidades.fromKPI
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
import kotlin.collections.map

class DetalleDashboardVM(
    private val cargarDashboardCU: CargarDashboardCU,
    private val eliminarDashboardCU: EliminarDashboardCU,
    private val guardarDashboardCU: GuardarDashboardCU,
    private val obtenerSeleccionPanel: ObtenerSeleccionPanel,
    private val obtenerKpisCU: ObtenerKpisCU,
    private val dialog: DialogManager

) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading("Cargando..."))
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()


    private var listaPaneles: List<SeleccionPanelUI> = emptyList()

    sealed class UIState {
        data class Success(val dashboardUI: DashboardUI, val kpisDisponibles : List<KpiUI> = emptyList<KpiUI>()) : UIState()
        data class Error(val mensaje: String) : UIState()
        data class Loading(val mensaje: String) : UIState()
    }

    sealed class Eventos {

        data class Cargar(val identificador: Int) : Eventos()
        data class Eliminar(val navegacion: (EventosNavegacion) -> Unit) : Eventos()  // Renombrado para claridad
        data class Guardar(val navegacion: (EventosNavegacion) -> Unit) : Eventos()  // Renombrado para claridad

        data class OnChangeNombre(val valor: String) : Eventos()     // Adaptado desde OnChangeItem
        data class OnChangeDescripcion(val valor: String) : Eventos() // Adaptado desde OnChangeProveedor
        data class OnChangeInicial(val valor: Boolean) : Eventos() //
        data class OnActualizarPaneles(val panelesUI: List<PanelUI>) : Eventos() // Adaptado desde OnChangeProveedor
        data class ActualizarLogo(val rutaLogo: String) : Eventos() // Adaptado desde OnChangeProveedor
        data class OnChangeKpiSeleccionado(val kpi: KpiUI) : Eventos() // Adaptado desde OnChangeProveedor
        // Los otros OnChange (global, codigoOrganizacion, codigo) no aplican a Dashboard
    }


    fun onEvento(eventos: Eventos) {
        when (eventos) {
            is Eventos.Cargar -> cargar(eventos.identificador)
            is Eventos.Eliminar -> eliminar(eventos.navegacion)
            is Eventos.Guardar -> guardar(eventos.navegacion)
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
                            is Eventos.OnChangeInicial ->
                                estado.copy(dashboardUI = estado.dashboardUI.copy(home = eventos.valor))

                           

                            is Eventos.OnChangeKpiSeleccionado ->{
                                estado.copy(dashboardUI = estado.dashboardUI.copy( kpiOrigen = eventos.kpi))
                            }
                            
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
                        //_uiState.update { estado ->
                            val newEstado: UIState
                            if (id != 0) {
                                val ds: DashboardUI = DashboardUI().fromDashboard(cargarDashboardCU.cargar(id))
                                val listaPaneles = paneles.map { p -> (ds.listaPaneles.find { it.id == p.id }) ?: p }
                                _uiState.value = UIState.Success(dashboardUI = ds.copy(listaPaneles = listaPaneles))
                            } else {
                                _uiState.value = UIState.Success(dashboardUI = DashboardUI(listaPaneles = paneles))
                            }
                            
                            
                            
                            obtenerKpis()
                      // }
                    }


            } catch (e: Exception) {
                _uiState.value = UIState.Error(e.message ?: "Error desconocido al cargar")
            }
        }
    }
    
    suspend fun obtenerKpis() {
        
       // viewModelScope.launch {
            
            
            obtenerKpisCU.getAll().map { listaKpi -> listaKpi.map { k -> KpiUI().fromKPI(k) } }
                .flowOn(Dispatchers.IO)
                .catch { ex -> _uiState.update { DetalleDashboardVM.UIState.Error(ex.toString()) } }
                .collect { listakpi ->
                    _uiState.update { estado ->
                        if (estado is DetalleDashboardVM.UIState.Success) {
                            estado.copy(kpisDisponibles =  listakpi)
                        } else {
                            estado
                        }
                    }
                }
            
            
        //}
    }

    private fun eliminar(navegacion: (EventosNavegacion) -> Unit) {

        dialog.sino(_t(R.string.seguro_que_desea_elimianr_el_elmento_seleccionado)) { resp ->

            if (resp == DialogosResultado.Si) {
                viewModelScope.launch {
                    try { // El ejemplo no tiene try-catch aquí, pero es buena práctica
                        withContext(Dispatchers.IO) {
                            val dashboardUi = (_uiState.value as UIState.Success).dashboardUI
                            eliminarDashboardCU.eliminar(dashboardUi.toDashboard())
                            dialog.informacion(_t(R.string.elemento_eliminado)) {
                                navegacion(EventosNavegacion.MenuDashboard)
                            }
                        }
                    } catch (e: Exception) {
                        _uiState.value = UIState.Error("Error al eliminar: ${e.message}")
                    }
                }
            }

        }


    }

    private fun guardar(navegacion: (EventosNavegacion) -> Unit) {
        if (validar()) {
            viewModelScope.launch {
                try { // El ejemplo no tiene try-catch aquí, pero es buena práctica
                    withContext(Dispatchers.IO) {
                        val dashboardUi = (_uiState.value as UIState.Success).dashboardUI
                        guardarDashboardCU.guardar(dashboardUi.toDashboard())
                        dialog.informacion(_t(R.string.elemento_almacenado)) {
                            navegacion(EventosNavegacion.MenuDashboard)

                        }
                    }
                } catch (e: Exception) {
                    _uiState.value = UIState.Error("Error al guardar: ${e.message}")
                }
            }
        }

    }


    private fun validar(): Boolean {
        val ui = _uiState.value as UIState.Success
        if (ui.dashboardUI.nombre.isEmpty()) {
            dialog.informacion(_t(R.string.debe_introducir_un_nombre_para_el_dashboard)) { }
            return false
        }
        return true
    }
}