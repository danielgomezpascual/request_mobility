package com.personal.metricas.organizaciones.data.ds.local

import com.personal.metricas.core.data.ds.TIPO_DS
import com.personal.metricas.organizaciones.data.ds.IDataSourceOrganizaciones
import com.personal.metricas.organizaciones.data.ds.local.dao.OrganizacionesDao
import com.personal.metricas.organizaciones.data.ds.local.entidades.OrganizacionesRoom
import com.personal.metricas.organizaciones.data.ds.local.entidades.fromOrganizaciones
import com.personal.metricas.organizaciones.data.ds.local.entidades.toOrganizaciones
import com.personal.metricas.organizaciones.domain.entidades.Organizaciones

class OrganizacionesRoomDS(private val dao: OrganizacionesDao) : IDataSourceOrganizaciones {
    override val tipo: TIPO_DS
        get() = TIPO_DS.ROOM

    override suspend fun getAll(): List<Organizaciones> = dao.getAll().map { organizacionRoom -> organizacionRoom.toOrganizaciones() }

    override suspend fun eliminar(org: Organizaciones) = dao.delete(OrganizacionesRoom().fromOrganizaciones(organizacion = org))

    override suspend fun eliminarTodas() = dao.vaciarTabla()

    override suspend fun guardar(org: Organizaciones): Long = dao.insert(OrganizacionesRoom().fromOrganizaciones(organizacion = org))

    override suspend fun obtener(id: String): Organizaciones = (dao.getPorID(id) ?: OrganizacionesRoom()).toOrganizaciones()

}