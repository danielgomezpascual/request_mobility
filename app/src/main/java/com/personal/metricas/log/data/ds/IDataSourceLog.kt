package com.personal.metricas.log.data.ds

import com.personal.metricas.core.data.ds.IDS
import com.personal.metricas.log.domain.entidades.Log

interface IDataSourceLog: IDS {
	suspend fun getAll(): List<Log>
	suspend fun eliminar(notas: Log)
	suspend fun eliminarTodos()
	suspend fun guardar(notas: Log): Long
	suspend fun getPorID(identificador: Int): Log
}