package com.personal.metricas.firebase.data.entidades

import androidx.room.PrimaryKey
import com.personal.metricas.dashboards.data.ds.local.entidades.DashboardRoom
import com.personal.metricas.endpoints.data.ds.local.entidades.EndPointRoom

data class EndPointFirestore(

	var id: Int = 0,
	val nombre: String = "",
	val descripcion: String = "",
	val url: String = "",
	val parametros: String = "",
	val tabla: String = "",
	val nodoIdentificadorFila: String = "",
	val eliminarDatos: String = "",
	val identificadorUsuario: String
)
{
	companion object {
		fun fromRoom(endPointRoom: EndPointRoom, identificadorUsuario: String) = EndPointFirestore(
			id =  endPointRoom.id,
			nombre =  endPointRoom.nombre,
			descripcion =  endPointRoom.descripcion,
			url =  endPointRoom.url,
			parametros =  endPointRoom.parametros,
			tabla =  endPointRoom.tabla,
			nodoIdentificadorFila =  endPointRoom.nodoIdentificadorFila,
			eliminarDatos =  endPointRoom.eliminarDatos,
			identificadorUsuario = identificadorUsuario,
		)


	}
}
