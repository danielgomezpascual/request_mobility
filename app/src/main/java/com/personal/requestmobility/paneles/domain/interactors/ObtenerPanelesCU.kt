package com.personal.requestmobility.paneles.domain.interactors

import com.personal.requestmobility.paneles.domain.repositorios.PanelesRepositorio

class ObtenerPanelesCU(private val repo: PanelesRepositorio) {
    suspend fun getAll() = repo.getAll()

}