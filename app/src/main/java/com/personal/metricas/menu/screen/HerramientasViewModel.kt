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

					//Select distinct  organization_id, organization_code from INV.MTL_PARAMETERS where organization_code
					// in (SELECT distinct substr(lectora_id,0,3)  FROM   uee_mob_to_tcb_requests R ) order by 1 desc
					//val organizaciones =listOf<String>("2649","2645","2643","2637","2636","2633","2629","2628","2627","2623","2619","2598","2597","2595","2592","2569","2568","2563","2560","2558","2553","2552","2551","2550","2548","2547","2537","2501","2499","2497","2493","2492","2485","2462","2461","2459","2458","2457","2446","2427","2408","2407","2360","2283","2271","2216","2215","2214","2211","2210","2206","1985","1947","1946","1945","1925","1905","1670","1667","1545","1507","1425","1265","1141","1121","947","860","857","856","855","851","850","849","848","804","746",		)
					val organizaciones =listOf<String>("804","746"		)
					val orgSeleccionadas = organizaciones.joinToString(";")
					App.sharedPrerfences.put(K.ORGANIZACIONES, orgSeleccionadas)
					inicalizadorManager.start()
					dialog.informacion(texto = "Incializacion de datos finalizada") {}
				}
			}
		})

	}


}

