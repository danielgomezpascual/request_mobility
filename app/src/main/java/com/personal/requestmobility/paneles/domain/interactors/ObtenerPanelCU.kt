package com.personal.requestmobility.paneles.domain.interactors

import com.personal.requestmobility.kpi.domain.repositorios.KpisRepositorio
import com.personal.requestmobility.paneles.domain.repositorios.PanelesRepositorio

class ObtenerPanelCU(private val repositorio: PanelesRepositorio) {
    suspend fun obtener(id: Int) = repositorio.obtener(id)

}