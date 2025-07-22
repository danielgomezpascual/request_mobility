package com.personal.metricas.organizaciones.data.repositorio

import com.personal.metricas.core.data.ds.TIPO_DS
import com.personal.metricas.core.data.repositorio.BaseRepositorio
import com.personal.metricas.organizaciones.data.ds.IDataSourceOrganizaciones
import com.personal.metricas.organizaciones.domain.entidades.Organizaciones
import com.personal.metricas.organizaciones.domain.repositorio.IRepoOrganizaciones

class OrganizacionesRepoImp(fuentesDatos: List<IDataSourceOrganizaciones>) :
    BaseRepositorio<IDataSourceOrganizaciones>(fuentesDatos), IRepoOrganizaciones {

        override suspend fun getAll(): List<Organizaciones>  = dameDS(TIPO_DS.RETROFIT).getAll()

}