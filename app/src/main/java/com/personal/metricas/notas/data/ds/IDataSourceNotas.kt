package com.personal.metricas.notas.data.ds

import com.personal.metricas.core.data.ds.IDS
import com.personal.metricas.endpoints.domain.entidades.EndPoint
import com.personal.metricas.notas.domain.entidades.Notas

interface IDataSourceNotas: IDS {
	suspend fun getAll(): List<Notas>
	suspend fun eliminar(notas: Notas)
	suspend fun eliminarTodos()
	suspend fun guardar(notas: Notas): Long
	suspend fun getPorID(identificador: String): Notas
}