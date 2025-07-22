package com.personal.metricas.endpoints.domain.interactors

import com.personal.metricas.endpoints.domain.repositorios.EndPointRepositorio

class ObtenerEndPointCU(private val repo: EndPointRepositorio) {
	suspend fun obtener(id: Int) = repo.obtener(id)
}