package com.personal.metricas.menu.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.composables.dialogos.DialogosResultado
import com.personal.metricas.inicializador.domain.InicializadorManager
import kotlinx.coroutines.launch


class HerramientasViewModel(
	private val inicalizadorManager: InicializadorManager,
	private val dialog: DialogManager,
) : ViewModel() {

	sealed class Eventos {
		object InicializadorMetricas : Eventos()
	}


	fun onEvent(evento: HerramientasViewModel.Eventos) {
		when (evento) {
			Eventos.InicializadorMetricas -> inicializadoreMetricas()
		}

	}


	fun inicializadoreMetricas() {
		dialog.sino("¿Seguro que desea generar la inicilizacion de metricas? Cualquier modificación relizada sobre estos elementos será descartada", onResultadoDialog = { resultado ->
			if (resultado == DialogosResultado.Si) {
				viewModelScope.launch {
					inicalizadorManager.start()
					dialog.informacion(texto = "Incializacion de datos finalizada") {}
				}
			}
		})

	}


}