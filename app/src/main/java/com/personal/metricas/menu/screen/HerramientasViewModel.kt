package com.personal.metricas.menu.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.metricas.App
import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.composables.dialogos.DialogosResultado
import com.personal.metricas.core.utils.K
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

					val organizaciones =listOf<String>("746",	"804",	"848",	"849",	"850",	"851",	"855",	"856",	"857",	"860",	"947",	"1121",	"1141",	"1265",	"1425",	"1507",	"1545",	"1667",	"1670",	"1905",	"1945",	"1946",	"1947",	"1985",	"2206",	"2210",	"2211",	"2214",	"2215",	"2216",	"2234",	"2259",	"2271",	"2283",	"2360",	"2407",	"2461",	"2462",	"2489",	"2492",	"2497",	"2498",	"2499",	"2501",	"2503",	"2506",	"2537",	"2547",	"2548",	"2551",	"2552",	"2553",	"2558",	"2560",	"2563",	"2568",	"2569",	"2592",	"2595",	"2597",	"2598",	"2619",	"2623",	"2627",	"2628",	"2636",	"2637",	"2643",	"2649",					)
					val orgSeleccionadas = organizaciones.joinToString(";")
					App.sharedPrerfences.put(K.ORGANIZACIONES, orgSeleccionadas)
					inicalizadorManager.start()
					dialog.informacion(texto = "Incializacion de datos finalizada") {}
				}
			}
		})

	}


}

