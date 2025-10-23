package com.personal.metricas.log.data.ds.local


import com.personal.metricas.core.data.ds.TIPO_DS
import com.personal.metricas.log.data.ds.IDataSourceLog
import com.personal.metricas.log.data.ds.local.dao.LogDao
import com.personal.metricas.log.data.ds.local.entidades.LogRoom
import com.personal.metricas.log.data.ds.local.entidades.fromLog
import com.personal.metricas.log.data.ds.local.entidades.toLog
import com.personal.metricas.log.domain.entidades.Log
import com.personal.metricas.notas.data.ds.IDataSourceNotas
import com.personal.metricas.notas.data.ds.local.dao.NotasDao
import com.personal.metricas.notas.data.ds.local.entidades.NotasRoom
import com.personal.metricas.notas.data.ds.local.entidades.fromNotas
import com.personal.metricas.notas.data.ds.local.entidades.toNotas
import com.personal.metricas.notas.domain.entidades.Notas

class LogLocalDS(private val dao: LogDao) : IDataSourceLog {
	override val tipo: TIPO_DS
		get() = TIPO_DS.ROOM

	override suspend fun getAll(): List<Log> = dao.todosLogs().map { it.toLog() }
	override suspend fun eliminar(log: Log) = dao.delete(LogRoom().fromLog(log))
	override suspend fun eliminarTodos() = dao.vaciarTabla()
	override suspend fun guardar(log: Log): Long = dao.insert(LogRoom().fromLog(log))
	override suspend fun getPorID(identificador: Int): Log = (dao.getPorID(identificador) ?: LogRoom()).toLog()

}