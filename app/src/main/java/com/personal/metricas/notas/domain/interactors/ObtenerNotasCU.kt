package com.personal.metricas.notas.domain.interactors

import com.personal.metricas.notas.domain.entidades.Notas
import com.personal.metricas.notas.domain.repositorios.NotasRepositorio
import kotlinx.coroutines.flow.Flow

class ObtenerNotasCU(private val repo: NotasRepositorio) {
	suspend fun getAll(): Flow<List<Notas>> = repo.getAll()
}