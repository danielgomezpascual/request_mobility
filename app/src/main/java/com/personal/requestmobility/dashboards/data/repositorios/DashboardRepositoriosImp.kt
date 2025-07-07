package com.personal.requestmobility.dashboards.data.repositorios

import com.personal.requestmobility.core.data.ds.TIPO_DS
import com.personal.requestmobility.core.data.repositorio.BaseRepositorio
import com.personal.requestmobility.dashboards.data.ds.IDataSourceDashboard
import com.personal.requestmobility.dashboards.domain.entidades.Dashboard
import com.personal.requestmobility.dashboards.domain.repositorios.DashboardRepositorio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class DashboardRepositoriosImp(
    fuentesDatos: List<IDataSourceDashboard>
) : BaseRepositorio<IDataSourceDashboard>(fuentesDatos), DashboardRepositorio {
    override suspend fun getAllHome(): Flow<List<Dashboard>> = flow{
        val listado: List<Dashboard> = dameDS(TIPO_DS.ROOM).getAllHome()
        emit(listado)
    }

    override suspend fun getAll(): Flow<List<Dashboard>> = flow {
        // App.modo determinaría si se obtienen datos locales o remotos primero,
        // o si se hace una sincronización. El ejemplo usa App.modo para elegir una fuente.
        val listado: List<Dashboard> = dameDS(TIPO_DS.ROOM).getAll() // dameDS es de BaseRepositorio
        emit(listado)
    }

    override suspend fun eliminar(dashboard: Dashboard) {
        // Las operaciones de escritura suelen ir al local.
        dameDS(TIPO_DS.ROOM).eliminar(dashboard)
    }

    override suspend fun eliminar() { // Corresponde a eliminarTodos en el LocalDS
        dameDS(TIPO_DS.ROOM).eliminarTodos()
    }

    override suspend fun guardar(dashboard: Dashboard): Long {
        return dameDS(TIPO_DS.ROOM).guardar(dashboard)
    }

    override suspend fun obtener(id: Int): Dashboard {

            return dameDS(TIPO_DS.ROOM).getPorID(id)

    }
}