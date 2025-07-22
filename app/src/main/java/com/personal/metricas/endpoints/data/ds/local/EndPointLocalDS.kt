package com.personal.metricas.endpoints.data.ds.local


import com.personal.metricas.core.data.ds.TIPO_DS
import com.personal.metricas.endpoints.data.ds.IDataSourceEndPoint
import com.personal.metricas.endpoints.data.ds.local.dao.EndPointDao
import com.personal.metricas.endpoints.data.ds.local.entidades.EndPointRoom
import com.personal.metricas.endpoints.data.ds.local.entidades.fromEndPoint
import com.personal.metricas.endpoints.data.ds.local.entidades.toEndPoint
import com.personal.metricas.endpoints.domain.entidades.EndPoint

class EndPointLocalDS(private val dao: EndPointDao) : IDataSourceEndPoint {
	override val tipo: TIPO_DS
		get() = TIPO_DS.ROOM

	override suspend fun getAll(): List<EndPoint> {
		return dao.todosEndPoints().map { it.toEndPoint() }
	}

	override suspend fun eliminar(endPoint: EndPoint) {
		dao.delete(EndPointRoom().fromEndPoint(endPoint))
	}

	override suspend fun eliminarTodos() {
		dao.vaciarTabla()
	}

	override suspend fun guardar(endPoint: EndPoint): Long {
		return dao.insert(EndPointRoom().fromEndPoint(endPoint))
	}

	override suspend fun getPorID(identificador: Int): EndPoint {
		return (dao.getPorID(identificador) ?: EndPointRoom()).toEndPoint()
	}
}