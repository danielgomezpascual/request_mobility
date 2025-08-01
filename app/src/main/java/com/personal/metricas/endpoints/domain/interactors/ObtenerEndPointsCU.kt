package com.personal.metricas.endpoints.domain.interactors


import com.personal.metricas.endpoints.domain.entidades.EndPoint
import com.personal.metricas.endpoints.domain.repositorios.EndPointRepositorio
import kotlinx.coroutines.flow.Flow

class ObtenerEndPointsCU(private val repo: EndPointRepositorio) {
	suspend fun getAll(): Flow<List<EndPoint>> = repo.getAll()
}