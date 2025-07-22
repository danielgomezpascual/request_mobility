package com.personal.metricas.endpoints.domain.interactors
import com.personal.metricas.endpoints.domain.entidades.EndPoint
import com.personal.metricas.endpoints.domain.repositorios.EndPointRepositorio

class GuardarEndPointCU(private val repo: EndPointRepositorio) {
	suspend fun guardar(endPoint: EndPoint) = repo.guardar(endPoint)
}