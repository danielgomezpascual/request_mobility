package com.personal.metricas.endpoints.data.ds

import com.personal.metricas.core.data.ds.IDS
import com.personal.metricas.endpoints.domain.entidades.EndPoint

interface IDataSourceEndPoint: IDS {
	suspend fun getAll(): List<EndPoint>
	suspend fun eliminar(endPoint: EndPoint)
	suspend fun eliminarTodos()
	suspend fun guardar(endPoint: EndPoint): Long
	suspend fun getPorID(identificador: Int): EndPoint
}