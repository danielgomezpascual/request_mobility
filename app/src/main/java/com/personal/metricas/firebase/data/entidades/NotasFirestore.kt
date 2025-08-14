package com.personal.metricas.firebase.data.entidades

import com.personal.metricas.kpi.data.ds.local.entidades.KpisRoom
import com.personal.metricas.notas.data.ds.local.entidades.NotasRoom
import kotlin.String

data class NotasFirestore(
	var hash: String = "",
	val descripcion: String = "",
	val identificadorUsuario: String,
) {


	companion object {
		fun fromRoom(notasRoom: NotasRoom, identificadorUsuario: String) = NotasFirestore(
			hash = notasRoom.hash,
			descripcion = notasRoom.descripcion,
			identificadorUsuario = identificadorUsuario,
		)

	}
}
