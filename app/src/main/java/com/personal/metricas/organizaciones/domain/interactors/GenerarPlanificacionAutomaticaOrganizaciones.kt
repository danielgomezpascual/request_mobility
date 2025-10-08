package com.personal.metricas.organizaciones.domain.interactors

import com.personal.metricas.App
import com.personal.metricas.core.composables.listas.MA_ListaReordenable_EstiloYouTube
import java.lang.Exception

class GenerarPlanificacionAutomaticaOrganizaciones(
	private val obtenerOrganizaciones: ObtenerOrganizacionesCU,
	private val obtenerHoras: ObtenerHorasTransaccionesOrganizacionCU,
	private val guardarOrganizacion: GuardarPlanificacionOrganizacinCU,
) {

	suspend fun realizarPlanificacionAutomativa(): Boolean {
		try{
			val organizaciones = obtenerOrganizaciones.getAll()
			organizaciones.forEach { organizacion ->
				val horas = obtenerHoras.obtener(organizacion)
				guardarOrganizacion.guardar(organizacion.copy(activo = true, visible = true, horas = horas))
			}
			return true
		}catch (e: Exception){
			App.log.e(e.toString())
			return false
		}


	}
}