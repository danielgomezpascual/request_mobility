package com.personal.requestmobility.organizaciones.data.repositorio

import com.personal.requestmobility.core.data.ds.TIPO_DS
import com.personal.requestmobility.core.data.repositorio.BaseRepositorio
import com.personal.requestmobility.organizaciones.data.ds.IDataSourceOrganizaciones
import com.personal.requestmobility.organizaciones.domain.entidades.Organizaciones
import com.personal.requestmobility.organizaciones.domain.repositorio.IRepoOrganizaciones

class OrganizacionesRepoImp(fuentesDatos: List<IDataSourceOrganizaciones>) :
    BaseRepositorio<IDataSourceOrganizaciones>(fuentesDatos), IRepoOrganizaciones {

        override suspend fun getAll(): List<Organizaciones>  = dameDS(TIPO_DS.RETROFIT).getAll()

}