package com.personal.requestmobility.sincronizacion.ui.entidades

import com.personal.requestmobility.organizaciones.domain.entidades.Organizaciones

data class OrganizacionesSincronizarUI(
    val organizationCode: String = "",
    val organizationName: String = "",
    val organizationId: String = "",
    val masterOrganizationId: String = "",
    val seleccionado: Boolean = false

    )


fun OrganizacionesSincronizarUI.fromOrganizacion(organizacion: Organizaciones) =
    OrganizacionesSincronizarUI(
        organizationCode = organizacion.organizationCode,
        organizationName = organizacion.organizationName,
        organizationId = organizacion.organizationId,
        masterOrganizationId = organizacion.masterOrganizationId,
    )


