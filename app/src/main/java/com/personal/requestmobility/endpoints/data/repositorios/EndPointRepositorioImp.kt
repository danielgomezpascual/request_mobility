package com.personal.requestmobility.endpoints.data.repositorios

import com.personal.requestmobility.core.data.ds.TIPO_DS
import com.personal.requestmobility.core.data.repositorio.BaseRepositorio
import com.personal.requestmobility.endpoints.data.ds.IDataSourceEndPoint
import com.personal.requestmobility.endpoints.domain.entidades.EndPoint
import com.personal.requestmobility.endpoints.domain.repositorios.EndPointRepositorio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EndPointRepositorioImp(
	fuentesDatos: List<IDataSourceEndPoint>
) : BaseRepositorio<IDataSourceEndPoint>(fuentesDatos), EndPointRepositorio {

	override suspend fun getAll(): Flow<List<EndPoint>> = flow {
		emit(dameDS(TIPO_DS.ROOM).getAll())
	}

	override suspend fun eliminar(endPoint: EndPoint) {
		dameDS(TIPO_DS.ROOM).eliminar(endPoint)
	}

	override suspend fun eliminarTodos() {
		dameDS(TIPO_DS.ROOM).eliminarTodos()
	}

	override suspend fun guardar(endPoint: EndPoint): Long {
		return dameDS(TIPO_DS.ROOM).guardar(endPoint)
	}

	override suspend fun obtener(id: Int): EndPoint {
		return dameDS(TIPO_DS.ROOM).getPorID(id)
	}
}