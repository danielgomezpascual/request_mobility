package com.personal.metricas.kpi.domain.interactors

import com.personal.metricas.kpi.domain.repositorios.KpisRepositorio
import com.personal.metricas.kpi.ui.entidades.KpiUI
import com.personal.metricas.kpi.ui.entidades.toKpi

class EliminarKpiCU(private val repositorio: KpisRepositorio) {
    suspend fun eliminar(kpiUI: KpiUI) {
        repositorio.eliminar(kpiUI.toKpi())
    }
}