package com.personal.metricas.firebase.data.entidades

import com.personal.metricas.kpi.data.ds.local.entidades.KpisRoom
import com.personal.metricas.paneles.data.ds.local.entidades.PanelesRoom
import kotlin.Int

data class PanelFirestore(
	var id: Int = 0,
	val titulo: String = "",
	val descripcion: String = "",
	val configuracion: String = "",
	val idKpi: Int = 0,
	val autogenerado: String = "N",
	val identificadorUsuario: String,
) {
	companion object{
		fun fromRoom(panelesRoom: PanelesRoom, identificadorUsuario: String) = PanelFirestore(
			id = panelesRoom.id,
			titulo = panelesRoom.titulo,
			descripcion = panelesRoom.descripcion,
			configuracion = panelesRoom.configuracion,
			idKpi = panelesRoom.idKpi,
			autogenerado = panelesRoom.autogenerado,
			identificadorUsuario = identificadorUsuario,
		)
	}


}

