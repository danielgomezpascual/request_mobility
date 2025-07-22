package com.personal.requestmobility.endpoints.domain.interactors
import com.personal.requestmobility.endpoints.domain.entidades.EndPoint
import com.personal.requestmobility.endpoints.domain.repositorios.EndPointRepositorio

class GuardarEndPointCU(private val repo: EndPointRepositorio) {
	suspend fun guardar(endPoint: EndPoint) = repo.guardar(endPoint)
}