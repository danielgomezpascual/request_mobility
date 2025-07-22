package com.personal.metricas.kpi.domain.interactors

import com.personal.metricas.kpi.domain.repositorios.KpisRepositorio

class ObtenerKpiCU(private val repositorio: KpisRepositorio) {
    suspend fun obtener(id: Int) = repositorio.obtener(id)

}