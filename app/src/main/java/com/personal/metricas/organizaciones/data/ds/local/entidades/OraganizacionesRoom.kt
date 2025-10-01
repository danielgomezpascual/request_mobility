package com.personal.metricas.organizaciones.data.ds.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.personal.metricas.core.room.IRoom
import com.personal.metricas.organizaciones.domain.entidades.Organizaciones


@Entity(tableName = "Organizaciones")
data class OrganizacionesRoom(
	@PrimaryKey()
	val organizationCode: String = "",
	val organizationName: String = "",
	val organizationId: String = "",
	val masterOrganizationId: String = "",
	val seleccionada: String = "0",
	val tiempos: String = "",
) : IRoom


fun OrganizacionesRoom.toOrganizaciones(): Organizaciones {

	return Organizaciones(
		organizationCode = this.organizationCode,
		organizationName = this.organizationName,
		organizationId = this.organizationId,
		masterOrganizationId = this.masterOrganizationId,
		seleccionada = this.seleccionada,
		tiempos = this.tiempos,
	)
}

fun OrganizacionesRoom.fromOrganizaciones(organizacion: Organizaciones) = OrganizacionesRoom(
	organizationCode = organizacion.organizationCode,
	organizationName = organizacion.organizationName,
	organizationId = organizacion.organizationId,
	masterOrganizationId = organizacion.masterOrganizationId,
	seleccionada = organizacion.seleccionada,
	tiempos = organizacion.tiempos,
)

