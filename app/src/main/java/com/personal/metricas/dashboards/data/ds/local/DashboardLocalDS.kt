package com.personal.metricas.dashboards.data.ds.local

import com.personal.metricas.core.data.ds.TIPO_DS
import com.personal.metricas.dashboards.data.ds.IDataSourceDashboard
import com.personal.metricas.dashboards.data.ds.local.dao.DashboardDao
import com.personal.metricas.dashboards.data.ds.local.entidades.DashboardRoom
import com.personal.metricas.dashboards.data.ds.local.entidades.fromDashboard
import com.personal.metricas.dashboards.data.ds.local.entidades.toDashboard
import com.personal.metricas.dashboards.domain.entidades.Dashboard


class DashboardLocalDS(private val dao: DashboardDao) : IDataSourceDashboard {

    override val tipo: TIPO_DS
        get() = TIPO_DS.ROOM

    override suspend fun getAllHome(): List<Dashboard> {
        val dashboardsRoom: List<DashboardRoom> = dao.todosDashboardsHome()
        return dashboardsRoom.map { it.toDashboard() }
    }

    override suspend fun getAll(): List<Dashboard> {
        val dashboardsRoom: List<DashboardRoom> = dao.todosDashboards()
        return dashboardsRoom.map { it.toDashboard() }
    }

    override suspend fun eliminarTodos() {
        dao.vaciarTabla() // Método de BaseDaoExtended
    }

    override suspend fun eliminar(dashboard: Dashboard) {
        // El patrón del ejemplo crea una instancia nueva para llamar a fromDashboard
        dao.delete(DashboardRoom().fromDashboard(dashboard))
    }

    override suspend fun guardar(dashboard: Dashboard): Long {


        // El patrón del ejemplo crea una instancia nueva para llamar a fromDashboard
        return dao.insert(DashboardRoom().fromDashboard(dashboard))
    }

    override suspend fun getPorID(identificador: Int): Dashboard {
        return (dao.getPorID(identificador)?: DashboardRoom()).toDashboard()

    }
}