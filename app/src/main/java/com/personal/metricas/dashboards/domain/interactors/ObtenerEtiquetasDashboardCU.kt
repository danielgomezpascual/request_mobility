package com.personal.metricas.dashboards.domain.interactors

import com.personal.metricas.dashboards.ui.entidades.Etiquetas
import kotlinx.coroutines.flow.first

class ObtenerEtiquetasDashboardCU(private val obtenerDashboardsCU: ObtenerDashboardsCU) {
	suspend fun dameEtiquetas(): List<Etiquetas> {
		val ds = obtenerDashboardsCU.getAll().first()
		var etiquetas: MutableList<Etiquetas> = mutableListOf()
		ds.forEach { d -> etiquetas.add(d.etiqueta) }
		return etiquetas.distinct()
	}
}