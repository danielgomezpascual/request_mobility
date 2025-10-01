package com.personal.metricas.organizaciones.data.repositorio

import com.personal.metricas.App
import com.personal.metricas.core.data.ds.TIPO_DS
import com.personal.metricas.core.data.repositorio.BaseRepositorio
import com.personal.metricas.organizaciones.data.ds.IDataSourceOrganizaciones
import com.personal.metricas.organizaciones.domain.entidades.Organizaciones
import com.personal.metricas.organizaciones.domain.repositorio.IRepoOrganizaciones

class OrganizacionesRepoImp(fuentesDatos: List<IDataSourceOrganizaciones>) :
    BaseRepositorio<IDataSourceOrganizaciones>(fuentesDatos), IRepoOrganizaciones {

        override suspend fun getAll(): List<Organizaciones>  = dameDS(TIPO_DS.RETROFIT).getAll()

        override suspend fun guardar(organizacion: Organizaciones): Long {
                App.log.d(organizacion.toString())
                return dameDS(TIPO_DS.ROOM).guardar(organizacion)
        }

        override suspend fun getPorID(organizatinCode: String): Organizaciones =  dameDS(TIPO_DS.ROOM).obtener(organizatinCode)

}