package com.personal.metricas.log.domain.repositorio

import com.personal.metricas.log.domain.entidades.Log
import com.personal.metricas.notas.domain.entidades.Notas
import kotlinx.coroutines.flow.Flow

interface ILogRepositorio {
	suspend fun getAll(): Flow<List<Log>>
	suspend fun eliminar(log: Log)
	suspend fun eliminarTodos()
	suspend fun guardar(log: Log): Long
	suspend fun obtener(id: Int): Log
}