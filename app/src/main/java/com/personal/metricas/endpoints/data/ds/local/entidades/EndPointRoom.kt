package com.personal.metricas.endpoints.data.ds.local.entidades


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.personal.metricas.core.room.IRoom
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.core.utils._toJson
import com.personal.metricas.core.utils._toObjectFromJson
import com.personal.metricas.endpoints.domain.entidades.EndPoint

@Entity(tableName = "EndPoints")
data class EndPointRoom(
	@PrimaryKey(autoGenerate = true)
	var id: Int = 0,
	val nombre: String = "",
	val descripcion: String = "",
	val url: String = "",
	val parametros: String = "",
	val tabla: String = "",
	val nodoIdentificadorFila : String = ""
) : IRoom

fun EndPointRoom.toEndPoint(): EndPoint = EndPoint(
	id = this.id,
	nombre = this.nombre,
	descripcion = this.descripcion,
	url = this.url,
	parametros  = _toObjectFromJson<Parametros>(this.parametros) ?: Parametros(),
	tabla = this.tabla,
	nodoIdentificadorFila =  this.nodoIdentificadorFila
)

fun EndPointRoom.fromEndPoint(endPoint: EndPoint): EndPointRoom = EndPointRoom(
	id = endPoint.id,
	nombre = endPoint.nombre,
	descripcion = endPoint.descripcion,
	url = endPoint.url,
	parametros = _toJson(endPoint.parametros),
	tabla = endPoint.tabla,
	nodoIdentificadorFila = endPoint.nodoIdentificadorFila
)