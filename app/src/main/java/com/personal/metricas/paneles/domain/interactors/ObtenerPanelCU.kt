package com.personal.metricas.paneles.domain.interactors

import com.personal.metricas.paneles.domain.repositorios.PanelesRepositorio

class ObtenerPanelCU(private val repositorio: PanelesRepositorio) {
    suspend fun obtener(id: Int) = repositorio.obtener(id)

}