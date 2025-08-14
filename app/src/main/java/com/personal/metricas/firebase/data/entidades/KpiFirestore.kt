package com.personal.metricas.firebase.data.entidades

import com.personal.metricas.kpi.data.ds.local.entidades.KpisRoom
import kotlin.Int

data class KpiFirestore(
	var id: Int = 0,
	val titulo: String = "",
	val descripcion: String = "",
	val origen: String = "",
	val sql: String = "",
	val parametros: String = "",
	//  val configuracion: String = ""
	val autogenerado: String = "N",
	val identificadorUsuario: String,
) {
	companion object {
		fun fromRoom(kpiRoom: KpisRoom, identificadorUsuario: String) = KpiFirestore(
			id = kpiRoom.id,
			titulo = kpiRoom.titulo,
			descripcion = kpiRoom.descripcion,
			origen = kpiRoom.origen,
			sql = kpiRoom.sql,
			parametros = kpiRoom.parametros,
			autogenerado = kpiRoom.autogenerado,
			identificadorUsuario = identificadorUsuario,
		)

	}
}