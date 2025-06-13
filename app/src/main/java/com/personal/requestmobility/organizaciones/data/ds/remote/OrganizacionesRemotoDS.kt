package com.personal.requestmobility.organizaciones.data.ds.remote

import com.personal.requestmobility.App
import com.personal.requestmobility.core.data.ds.TIPO_DS
import com.personal.requestmobility.core.data.ds.remote.network.retrofit.request.objectToHeaderMap
import com.personal.requestmobility.organizaciones.data.ds.IDataSourceOrganizaciones
import com.personal.requestmobility.organizaciones.data.ds.remote.entidades.OrganizacionesRetrofit
import com.personal.requestmobility.organizaciones.data.ds.remote.entidades.toOrganizacion
import com.personal.requestmobility.organizaciones.data.ds.remote.paramteros.ParamOrganizaciones
import com.personal.requestmobility.organizaciones.data.ds.remote.servicio.OrganizacionesApiRemoto
import com.personal.requestmobility.organizaciones.domain.entidades.Organizaciones

class OrganizacionesRemotoDS(private val api: OrganizacionesApiRemoto) : IDataSourceOrganizaciones {


    override val tipo: TIPO_DS
        get() = TIPO_DS.RETROFIT


    override suspend fun getAll(): List<Organizaciones> {
        val r: ParamOrganizaciones = ParamOrganizaciones()
        val headers = r.objectToHeaderMap()
        val response = api.getAll(headers)
        val organizaciones: List<OrganizacionesRetrofit> = response.Response.items
        val orgs: List<Organizaciones> = organizaciones.map { it.toOrganizacion() }
        return orgs

    }
}