package com.personal.metricas.notas.domain.interactors

import com.personal.metricas.notas.domain.entidades.Notas
import com.personal.metricas.notas.domain.repositorios.NotasRepositorio

class EliminarTodasNotasCU (private val repo: NotasRepositorio) {
	suspend fun borrar() = repo.eliminarTodos()
}