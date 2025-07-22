package com.personal.metricas.endpoints.domain.interactors

import com.personal.metricas.endpoints.domain.entidades.EndPoint
import com.personal.metricas.endpoints.domain.repositorios.EndPointRepositorio

class EliminarEndPointCU(private val repositorio: EndPointRepositorio) {
	suspend fun eliminar(endPoint: EndPoint) {
		repositorio.eliminar(endPoint)
	}
}