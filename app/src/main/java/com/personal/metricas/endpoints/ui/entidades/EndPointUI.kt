package com.personal.metricas.endpoints.ui.entidades


import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.endpoints.domain.entidades.EndPoint

data class EndPointUI(
	val id: Int = 0,
	val nombre: String = "",
	val descripcion: String ="",
	val url: String = "",
	val parametros: Parametros,
	val tabla: String = "",
	val nodoIdentificadorFila : String = ""
){
	companion object{

		fun fromDomain(endPoint: EndPoint): EndPointUI{
			return EndPointUI(
				id = endPoint.id,
				nombre =  endPoint.nombre,
				descripcion = endPoint.descripcion,
				url = endPoint.url,
				parametros = endPoint.parametros,
				tabla = endPoint.tabla,
				nodoIdentificadorFila = endPoint.nodoIdentificadorFila
			)
		}
	}
}



fun EndPointUI.toDomain(): EndPoint {
	return EndPoint(
		id = this.id,
		nombre = this.nombre,
		descripcion = this.descripcion,
		url = this.url,
		parametros = this.parametros,
		tabla = this.tabla,
		nodoIdentificadorFila = this.nodoIdentificadorFila
	)
}