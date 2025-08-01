package com.personal.metricas.endpoints.domain.repositorios

import com.personal.metricas.endpoints.domain.entidades.EndPoint
import kotlinx.coroutines.flow.Flow

interface EndPointRepositorio {
	suspend fun getAll(): Flow<List<EndPoint>>
	suspend fun eliminar(endPoint: EndPoint)
	suspend fun eliminarTodos()
	suspend fun guardar(endPoint: EndPoint): Long
	suspend fun obtener(id: Int): EndPoint
}