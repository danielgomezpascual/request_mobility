package com.personal.metricas.firebase.data.entidades

import com.personal.metricas.dashboards.data.ds.local.entidades.DashboardRoom
import com.personal.metricas.kpi.data.ds.local.entidades.KpisRoom
import kotlin.Int

class DashboardFirestore(
	var id: Int = 0,
	val tipo: Int = 0,
	val nombre: String = "",
	val home: String = "N",
	val logo: String = "",
	val descripcion: String = "",
	val idKpi: Int = 0,
	val paneles: String = "",
	val autogenerado: String = "N",
	val etiqueta: String = "",
	val identificadorUsuario: String,
) {
	companion object {
		fun fromRoom(dashboardRoom: DashboardRoom, identificadorUsuario: String) = DashboardFirestore(
			id = dashboardRoom.id,
			tipo = dashboardRoom.tipo,
			nombre = dashboardRoom.nombre,
			home = dashboardRoom.home,
			logo = dashboardRoom.logo,
			descripcion = dashboardRoom.descripcion,
			idKpi = dashboardRoom.idKpi,
			paneles = dashboardRoom.paneles,
			autogenerado = dashboardRoom.autogenerado,
			etiqueta = dashboardRoom.etiqueta,
			identificadorUsuario = identificadorUsuario,
		)

	}
}
