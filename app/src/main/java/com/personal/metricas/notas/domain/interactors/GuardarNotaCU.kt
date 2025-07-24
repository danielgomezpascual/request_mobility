package com.personal.metricas.notas.domain.interactors

import com.personal.metricas.notas.domain.entidades.Notas
import com.personal.metricas.notas.domain.repositorios.NotasRepositorio

class GuardarNotaCU(private val repo: NotasRepositorio) {
	suspend fun guardar(nota: Notas) = repo.guardar(nota)
}