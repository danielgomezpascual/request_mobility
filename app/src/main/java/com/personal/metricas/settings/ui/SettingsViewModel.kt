package com.personal.metricas.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.metricas.App
import com.personal.metricas.App.Companion.ENTORNO
import com.personal.metricas.R
import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.composables.dialogos.DialogosResultado
import com.personal.metricas.core.room.AppDatabase
import com.personal.metricas.core.utils.Preferencias
import com.personal.metricas.core.utils._t
import com.personal.metricas.core.utils.if3
import com.personal.metricas.firebase.domain.interactors.DescargarContenidoFirestore
import com.personal.metricas.firebase.domain.interactors.SubirContenidoLocalFirebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(
	private val descargarFirebase: DescargarContenidoFirestore,
	private val subirFirebase: SubirContenidoLocalFirebase,
	private val db: AppDatabase,

	private val dialog: DialogManager,
) : ViewModel() {


	private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
	val uiState: StateFlow<UIState> = _uiState.asStateFlow()


	sealed class UIState() {
		data class Success(
			val trabajando: Boolean = false,
			val herramientas: Boolean = false,
			val ajustes: Boolean = false,
			val sincronizacion: Boolean = false,
			val entornoProduccion: Boolean = false,
			val sincronizarAuto: Boolean = false,

			) : UIState()

		data class Error(val mensaje: String) : UIState()
		object Loading : UIState()
	}

	sealed class Eventos() {
		object Cargar : Eventos()
		object SubirFirebase : Eventos()
		object DescargarFirebase : Eventos()
		object EliminarDatos : Eventos()
		data class AccesoHerramientas(val valor: Boolean) : Eventos()
		data class AccesoAjustes(val valor: Boolean) : Eventos()
		data class AccesosSincronizacion(val valor: Boolean) : Eventos()
		data class EntornoProduccion(val valor: Boolean) : Eventos()
		data class SincronizarAuto(val valor: Boolean) : Eventos()

	}

	fun onEvent(evento: Eventos) {
		if (evento == Eventos.Cargar) {


			_uiState.value = UIState.Success(
				trabajando = false,
				herramientas = App.sharedPrerfences.get<Boolean>(Preferencias.ACCESO_HERRAMIENTAS, false),
				ajustes = App.sharedPrerfences.get<Boolean>(Preferencias.ACCESO_AJUSTES, false),
				sincronizacion = App.sharedPrerfences.get<Boolean>(Preferencias.ACCESO_SINCRONIZACION, false),
				entornoProduccion = App.sharedPrerfences.get<Boolean>(Preferencias.ENTORNO_PRO, false),
				sincronizarAuto = App.sharedPrerfences.get<Boolean>(Preferencias.SINCRONIZAR_AUTO, false),
			)
			return
		}




		_uiState.update { estado ->

			if (estado is UIState.Success) {
				when (evento) {


					is Eventos.AccesoAjustes         -> {
						App.sharedPrerfences.put<Boolean>(Preferencias.ACCESO_AJUSTES, evento.valor)
						estado.copy(ajustes = evento.valor)
					}

					is Eventos.AccesoHerramientas    -> {
						App.sharedPrerfences.put<Boolean>(Preferencias.ACCESO_HERRAMIENTAS, evento.valor)
						estado.copy(herramientas = evento.valor)
					}

					is Eventos.AccesosSincronizacion -> {
						App.sharedPrerfences.put<Boolean>(Preferencias.ACCESO_SINCRONIZACION, evento.valor)
						estado.copy(sincronizacion = evento.valor)
					}
is Eventos.SincronizarAuto -> {
						App.sharedPrerfences.put<Boolean>(Preferencias.SINCRONIZAR_AUTO, evento.valor)
						estado.copy(sincronizarAuto = evento.valor)
					}

					is Eventos.EntornoProduccion     -> {
						App.sharedPrerfences.put<Boolean>(Preferencias.ENTORNO_PRO, evento.valor)
						ENTORNO = if3(App.sharedPrerfences.get<Boolean>(Preferencias.ENTORNO_PRO, false), "PRO", "DEV")
						estado.copy(entornoProduccion = evento.valor)
					}

					Eventos.SubirFirebase            -> {
						estado.copy(trabajando = true)
						viewModelScope.launch {
							withContext(Dispatchers.IO) {
								subirFirebase.uploadFirestore()
								dialog.informacion(_t(R.string.subida_a_firestore_finalizada)) {}

							}
						}
						estado.copy(trabajando = false)
					}

					Eventos.DescargarFirebase        -> {
						estado.copy(trabajando = true)
						viewModelScope.launch {
							withContext(Dispatchers.IO) {
								descargarFirebase.descargar()
								dialog.informacion(_t(R.string.descarga_de_firestore_fializada)) {}

							}
						}
						estado.copy(trabajando = false)
					}

					Eventos.EliminarDatos            -> {
						dialog.sino(_t(R.string.seguro_que_desea_elminar_los_datos_actuales_de_la_base_de_dtos)) { resp ->
							if (resp == DialogosResultado.Si) {
								viewModelScope.launch {
									listOf<String>("Transacciones", "paneles", "dashboard", "Kpis", "Notas", "EndPoints").forEach {
										db.openHelper.writableDatabase.execSQL("DELETE FROM $it")
									}
									dialog.informacion("Datos eliminados") { }
								}

							}
						}

						estado
					}

					else                             -> estado
				}
			} else {
				estado
			}


		}


	}
}