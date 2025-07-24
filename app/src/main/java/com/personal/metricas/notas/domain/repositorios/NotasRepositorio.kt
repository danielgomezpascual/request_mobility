package com.personal.metricas.notas.domain.repositorios

import com.personal.metricas.notas.domain.entidades.Notas
import kotlinx.coroutines.flow.Flow

interface NotasRepositorio {
	suspend fun getAll(): Flow<List<Notas>>
	suspend fun eliminar(notas: Notas)
	suspend fun eliminarTodos()
	suspend fun guardar(notas: Notas): Long
	suspend fun obtener(hash: String): Notas
}