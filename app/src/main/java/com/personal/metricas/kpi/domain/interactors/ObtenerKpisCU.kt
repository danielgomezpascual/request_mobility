package com.personal.metricas.kpi.domain.interactors

import com.personal.metricas.kpi.domain.repositorios.KpisRepositorio

class ObtenerKpisCU(private val repoKpi: KpisRepositorio) {
    suspend fun getAll() = repoKpi.getAll()

}