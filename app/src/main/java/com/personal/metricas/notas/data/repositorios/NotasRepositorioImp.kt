package com.personal.metricas.notas.data.repositorios

import com.personal.metricas.core.data.ds.TIPO_DS
import com.personal.metricas.core.data.repositorio.BaseRepositorio
import com.personal.metricas.endpoints.data.ds.IDataSourceEndPoint
import com.personal.metricas.endpoints.domain.entidades.EndPoint
import com.personal.metricas.endpoints.domain.repositorios.EndPointRepositorio
import com.personal.metricas.notas.data.ds.IDataSourceNotas
import com.personal.metricas.notas.domain.entidades.Notas
import com.personal.metricas.notas.domain.repositorios.NotasRepositorio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NotasRepositorioImp(
	fuentesDatos: List<IDataSourceNotas>
) : BaseRepositorio<IDataSourceNotas>(fuentesDatos), NotasRepositorio {

	override suspend fun getAll(): Flow<List<Notas>> = flow {
		emit(dameDS(TIPO_DS.ROOM).getAll())
	}

	override suspend fun eliminar(notas: Notas) {
		dameDS(TIPO_DS.ROOM).eliminar(notas)
	}

	override suspend fun eliminarTodos() {
		dameDS(TIPO_DS.ROOM).eliminarTodos()
	}

	override suspend fun guardar(notas: Notas): Long {
		return dameDS(TIPO_DS.ROOM).guardar(notas)
	}

	override suspend fun obtener(hash: String): Notas {
		return dameDS(TIPO_DS.ROOM).getPorID(hash)
	}
}