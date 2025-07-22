package com.personal.metricas.organizaciones.data.ds.remote.servicio

import com.personal.metricas.organizaciones.data.ds.remote.entidades.ResponseOrganizaciones
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface OrganizacionesApiRemoto {


    @GET("Organizations")
    suspend fun getAll(@HeaderMap headers: Map<String, String>): ResponseOrganizaciones


}