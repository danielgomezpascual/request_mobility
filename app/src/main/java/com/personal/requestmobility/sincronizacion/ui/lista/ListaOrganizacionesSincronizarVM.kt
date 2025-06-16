package com.personal.requestmobility.sincronizacion.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.requestmobility.App
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
    private val  guardar: GuardarTransacciones
) : ViewModel() {


    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    var textoBuscar: String = ""

    var listaOrganizacionesSincronizarUI: List<OrganizacionesSincronizarUI> = emptyList()


    sealed class UIState {
        data class Success(
            val organizaciones: List<OrganizacionesSincronizarUI> = emptyList<OrganizacionesSincronizarUI>(),
            val textoBuscar: String = "",
        ) : UIState()

        data class Error(val message: String) : UIState()
        object Loading : UIState()
    }

    sealed class Eventos {
        object Cargar : Eventos()
        data class Buscar(val texto: String) : Eventos()
        data class OnChangeSeleccionCheck(val organizacionUI: OrganizacionesSincronizarUI) : Eventos()
        object RealizarSincronizacion: Eventos()
        object EliminarDatosActuales: Eventos()
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

            listaOrganizacionesSincronizarUI= listaOrganizacionesSincronizarUI.map { org ->
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


    private fun eliminarDatosActuales(){
        viewModelScope.launch {
            repoTrx.eliminarTodo()
        }


    }
    private fun realizarSincronizacion(){

        if (_uiState.value is UIState.Success) {
            val orgSeleccionadas = (_uiState.value as UIState.Success).organizaciones.filter { it.seleccionado == true }
            val totalOrganizaciones = orgSeleccionadas.size
            App.log.d("Organizaciones que se van a sincronizar ${totalOrganizaciones}")
            viewModelScope.launch {
                orgSeleccionadas.forEach { organizacion ->
                    val trx = repoTrx.getTrxOracle(organizacion.organizationId)
                    guardar.guardar(trx)
                }

            }
        }

/*
       val repoTrx: TransaccionesRepoImp = getKoin().get()
       val guardar: GuardarTransacciones = getKoin().get()
       val repoOrganizaciones: OrganizacionesRepoImp = getKoin().get()




       runBlocking {
           val t = repoOrganizaciones.getAll()
           val totalOrganizaciones = t.size
           App.log.d("Organizaciones encontradas ${t.size}")

           t.filter { it.organizationCode.equals("DFM") }.forEachIndexed { indice, org ->

               launch {
                   App.log.d("[${indice+1} / $totalOrganizaciones] Procesando Organizacion encontradas ${org.toString()}")
                   val trx = repoTrx.getTrxOracle(org.organizationId)
                   guardar.guardar(trx)
               }
           }
       }*/
    }


}
