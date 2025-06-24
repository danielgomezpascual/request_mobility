package com.personal.requestmobility.sincronizacion.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.requestmobility.R
import com.personal.requestmobility.core.composables.dialogos.DialogManager
import com.personal.requestmobility.core.composables.dialogos.DialogosResultado
import com.personal.requestmobility.core.composables.dialogos.ResultadoDialog
import com.personal.requestmobility.core.utils.K
import com.personal.requestmobility.core.utils._t
import com.personal.requestmobility.organizaciones.domain.interactors.ObtenerOrganizacionesCU
import com.personal.requestmobility.sincronizacion.ui.entidades.OrganizacionesSincronizarUI
import com.personal.requestmobility.sincronizacion.ui.entidades.fromOrganizacion
import com.personal.requestmobility.transacciones.data.repositorios.TransaccionesRepoImp
import com.personal.requestmobility.transacciones.domain.interactors.GuardarTransacciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ListaOrganizacionesSincronizarVM(

    private val obtenerOrganizacion: ObtenerOrganizacionesCU,
    private val repoTrx: TransaccionesRepoImp,
    private val guardar: GuardarTransacciones,
    private val dialog: DialogManager
) : ViewModel() {


    private val _uiState = MutableStateFlow<UIState>(UIState.Trabajando)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    var textoBuscar: String = ""

    var listaOrganizacionesSincronizarUI: List<OrganizacionesSincronizarUI> = emptyList()


    sealed class UIState {
        data class Success(
            val organizaciones: List<OrganizacionesSincronizarUI> = emptyList<OrganizacionesSincronizarUI>(),
            val textoBuscar: String = "",
            val trabajando: Boolean = false,
            val mostrarDialogoInformacion: Boolean = false,
            val mostrarDialogoSiNo: Boolean = false,
            val texto: String = ""

        ) : UIState()

        data class Error(val message: String) : UIState()
        object Trabajando : UIState()
    }

    sealed class Eventos {
        object Cargar : Eventos()
        data class Buscar(val texto: String) : Eventos()
        data class OnChangeSeleccionCheck(val organizacionUI: OrganizacionesSincronizarUI) : Eventos()
        object RealizarSincronizacion : Eventos()

        object EliminarDatosActuales : Eventos()

    }


    fun onEvent(evento: Eventos) {
        when (evento) {
            Eventos.Cargar -> cargaInicial()
            is Eventos.Buscar -> modificarTextoBusqueta(evento.texto)
            is Eventos.OnChangeSeleccionCheck -> onChangeSeleccion(evento.organizacionUI)
            Eventos.RealizarSincronizacion -> realizarSincronizacion()

            Eventos.EliminarDatosActuales -> eliminarDatosActuales()

        }
    }


    private fun cargaInicial() {
        viewModelScope.launch {
            val listaOrganizaciones: List<OrganizacionesSincronizarUI> =
                obtenerOrganizacion.getAll().mapIndexed { indice, organzacion ->
                    OrganizacionesSincronizarUI().fromOrganizacion(organzacion)
                }

            listaOrganizacionesSincronizarUI = listaOrganizaciones
            _uiState.value = UIState.Success(organizaciones = listaOrganizaciones)
        }
    }

    private fun onChangeSeleccion(organizacionUI: OrganizacionesSincronizarUI) {
        if (_uiState.value is UIState.Success) {

            listaOrganizacionesSincronizarUI = listaOrganizacionesSincronizarUI.map { org ->
                if (organizacionUI.organizationCode.equals(org.organizationCode)) {
                    val estado = organizacionUI.seleccionado
                    organizacionUI.copy(seleccionado = !estado)
                } else {
                    org
                }
            }
            _uiState.value = UIState.Success(organizaciones = listaOrganizacionesSincronizarUI, textoBuscar = textoBuscar)
        }
    }


    private fun modificarTextoBusqueta(texto: String) {
        if (_uiState.value is UIState.Success) {
            textoBuscar = texto

            val l = if (!texto.isEmpty()) {

                listaOrganizacionesSincronizarUI.filter {
                    it.organizationName.contains(other = textoBuscar, ignoreCase = true)
                            || it.organizationCode.contains(other = textoBuscar, ignoreCase = true)

                }
            } else {
                listaOrganizacionesSincronizarUI
            }

            _uiState.update { estado ->
                if (estado is ListaOrganizacionesSincronizarVM.UIState.Success) {
                    estado.copy(textoBuscar = texto, organizaciones = l)
                } else {
                    estado
                }
            }
        }
    }


    private fun eliminarDatosActuales() {
        dialog.sino(_t(R.string.seguro_que_desea_elminar_los_datos_actuales_de_la_base_de_dtos)) { resp ->
            if (resp == DialogosResultado.Si) {
                viewModelScope.launch {
                    repoTrx.eliminarTodo()
                    dialog.informacion("Datos eliminados") { }
                }

            }
        }

    }


    private fun realizarSincronizacion() {

        if (_uiState.value is UIState.Success) {

            val oraganizciones = (_uiState.value as UIState.Success).organizaciones

            val orgSeleccionadas = oraganizciones.filter { it.seleccionado == true }
            if (orgSeleccionadas.isEmpty()) {
                dialog.informacion(_t(R.string.no_hay_ninguna_organizaci_n_seleccionada_seleccione_alguna_previamnete)) { }
                return
            }


            _uiState.value = (_uiState.value as UIState.Success).copy(trabajando = true)
            viewModelScope.launch {
                val totalOrganizaciones = oraganizciones.size
                val orgSeleccionadas = oraganizciones.filter { it.seleccionado == true }
                orgSeleccionadas.forEach { organizacion ->
                    val trx = repoTrx.getTrxOracle(organizacion.organizationId)
                    guardar.guardar(trx)
                }
                _uiState.value = UIState.Success(organizaciones = oraganizciones, trabajando = false)
                dialog.informacion(_t(R.string.information_actualizada)) {  }
            }
        }

    }


}
