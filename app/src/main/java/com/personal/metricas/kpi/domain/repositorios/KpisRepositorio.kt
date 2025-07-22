package com.personal.metricas.kpi.domain.repositorios

import com.personal.metricas.kpi.domain.entidades.Kpi
import kotlinx.coroutines.flow.Flow

interface KpisRepositorio {
    suspend fun getAll(): Flow<List<Kpi>>
    suspend fun eliminar(lectora: Kpi)
    suspend fun eliminarTodos()
    suspend fun guardar(lectora: Kpi): Long
    suspend fun obtener(id: Int): Kpi

}