package com.personal.requestmobility.endpoints.domain.interactors

import com.personal.requestmobility.endpoints.domain.entidades.EndPoint
import com.personal.requestmobility.endpoints.domain.repositorios.EndPointRepositorio

class EliminarEndPointCU(private val repositorio: EndPointRepositorio) {
	suspend fun eliminar(endPoint: EndPoint) {
		repositorio.eliminar(endPoint)
	}
}