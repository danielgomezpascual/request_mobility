package com.personal.metricas.paneles.domain.interactors

import com.personal.metricas.paneles.domain.repositorios.PanelesRepositorio

class ObtenerPanelesCU(private val repo: PanelesRepositorio) {
    suspend fun getAll() = repo.getAll()

}