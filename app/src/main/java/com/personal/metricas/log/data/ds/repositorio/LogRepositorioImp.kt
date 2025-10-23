package com.personal.metricas.log.data.ds.repositorio

import com.personal.metricas.core.data.ds.TIPO_DS
import com.personal.metricas.core.data.repositorio.BaseRepositorio
import com.personal.metricas.endpoints.data.ds.IDataSourceEndPoint
import com.personal.metricas.endpoints.domain.entidades.EndPoint
import com.personal.metricas.endpoints.domain.repositorios.EndPointRepositorio
import com.personal.metricas.log.data.ds.IDataSourceLog
import com.personal.metricas.log.domain.entidades.Log
import com.personal.metricas.log.domain.repositorio.ILogRepositorio
import com.personal.metricas.notas.data.ds.IDataSourceNotas
import com.personal.metricas.notas.domain.entidades.Notas
import com.personal.metricas.notas.domain.repositorios.NotasRepositorio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LogRepositorioImp(
	fuentesDatos: List<IDataSourceLog>
) : BaseRepositorio<IDataSourceLog>(fuentesDatos), ILogRepositorio {


	override suspend fun getAll(): Flow<List<Log>> = flow {
		emit(dameDS(TIPO_DS.ROOM).getAll())
	}

	override suspend fun eliminar(log: Log) {
		dameDS(TIPO_DS.ROOM).eliminar(log)
	}

	override suspend fun eliminarTodos() {
		dameDS(TIPO_DS.ROOM).eliminarTodos()
	}

	override suspend fun guardar(log: Log): Long {
		return dameDS(TIPO_DS.ROOM).guardar(log)
	}

	override suspend fun obtener(id: Int): Log {
		return dameDS(TIPO_DS.ROOM).getPorID(id)
	}
}