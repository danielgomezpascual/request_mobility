package com.personal.requestmobility.kpi.domain.interactors

import com.personal.requestmobility.kpi.domain.repositorios.KpisRepositorio
import com.personal.requestmobility.kpi.ui.entidades.KpiUI
import com.personal.requestmobility.kpi.ui.entidades.toKpi

class EliminarKpiCU(private val repositorio: KpisRepositorio) {
    suspend fun eliminar(kpiUI: KpiUI) {
        repositorio.eliminar(kpiUI.toKpi())
    }
}