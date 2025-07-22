package com.personal.metricas.organizaciones.data.ds.remote.entidades

import com.google.gson.annotations.SerializedName
import com.personal.metricas.organizaciones.domain.entidades.Organizaciones

data class ResponseOrganizaciones(@SerializedName(value = "Response") val Response: Response)


data class Response(@SerializedName(value = "Organization") val items:
                    List<OrganizacionesRetrofit> = emptyList<OrganizacionesRetrofit>())


data class OrganizacionesRetrofit(
    @SerializedName("ORGANIZATION_CODE") val ORGANIZATION_CODE: String = "",
    @SerializedName("ORGANIZATION_NAME") val ORGANIZATION_NAME: String = "",
    @SerializedName("ORGANIZATION_ID") val ORGANIZATION_ID: String = "",
    @SerializedName("MASTER_ORGANIZATION_ID") val MASTER_ORGANIZATION_ID: String = "",

    )



fun OrganizacionesRetrofit.toOrganizacion() = Organizaciones(
        organizationCode = ORGANIZATION_CODE,
        organizationName = ORGANIZATION_NAME,
        organizationId = ORGANIZATION_ID,
        masterOrganizationId = MASTER_ORGANIZATION_ID
    )
