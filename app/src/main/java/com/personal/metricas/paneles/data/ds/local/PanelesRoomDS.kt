package com.personal.metricas.paneles.data.ds.local

import com.personal.metricas.core.data.ds.TIPO_DS
import com.personal.metricas.paneles.data.ds.IDataSourcePaneles
import com.personal.metricas.paneles.data.ds.local.dao.PanelesDao
import com.personal.metricas.paneles.data.ds.local.entidades.PanelesRoom
import com.personal.metricas.paneles.data.ds.local.entidades.fromPanel
import com.personal.metricas.paneles.data.ds.local.entidades.toPanel
import com.personal.metricas.paneles.domain.entidades.Panel

class PanelesRoomDS(private val dao: PanelesDao) : IDataSourcePaneles {
    override val tipo: TIPO_DS
        get() = TIPO_DS.ROOM

    override suspend fun getAll(): List<Panel> = dao.todosPaneles().map { panelRoom -> panelRoom.toPanel() }

    override suspend fun eliminar(panel: Panel) = dao.delete(PanelesRoom().fromPanel(panel = panel))

    override suspend fun eliminarTodas() = dao.vaciarTabla()

    override suspend fun guardar(panel: Panel): Long = dao.insert(PanelesRoom().fromPanel(panel = panel))

    override suspend fun obtener(id: Int): Panel = (dao.getPorID(id) ?: PanelesRoom()).toPanel()

}