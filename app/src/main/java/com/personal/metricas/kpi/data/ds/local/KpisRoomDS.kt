package com.personal.metricas.kpi.data.ds.local

import com.personal.metricas.core.data.ds.TIPO_DS
import com.personal.metricas.kpi.data.ds.IDataSourceKpis
import com.personal.metricas.kpi.data.ds.local.dao.KpisDao
import com.personal.metricas.kpi.data.ds.local.entidades.KpisRoom
import com.personal.metricas.kpi.data.ds.local.entidades.fromKPI
import com.personal.metricas.kpi.data.ds.local.entidades.toKpi
import com.personal.metricas.kpi.domain.entidades.Kpi

class KpisRoomDS(private val dao: KpisDao) : IDataSourceKpis {
    override val tipo: TIPO_DS
        get() = TIPO_DS.ROOM

    override suspend fun getAll(): List<Kpi> = dao.todasLectoras().map { kpiRoom -> kpiRoom.toKpi() }

    override suspend fun eliminar(kpi: Kpi) = dao.delete(KpisRoom().fromKPI(kpi = kpi))

    override suspend fun eliminarTodas() = dao.vaciarTabla()

    override suspend fun guardar(kpi: Kpi): Long = dao.insert(KpisRoom().fromKPI(kpi = kpi))

    override suspend fun obtener(id: Int): Kpi = (dao.getPorID(id) ?: KpisRoom()).toKpi()

}