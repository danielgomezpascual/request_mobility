package com.personal.metricas.organizaciones.data.ds.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.personal.metricas.core.room.IRoom
import com.personal.metricas.core.utils.Utils
import com.personal.metricas.core.utils.Utils.toSiNo
import com.personal.metricas.core.utils.if3
import com.personal.metricas.organizaciones.domain.entidades.Organizaciones
import com.personal.metricas.organizaciones.ui.entidades.FORMA_SINCRONIZAR
import kotlin.Boolean


@Entity(tableName = "Organizaciones")
data class OrganizacionesRoom(
	@PrimaryKey()
	val organizationCode: String = "",
	val organizationName: String = "",
	val organizationId: String = "",
	val masterOrganizationId: String = "",

	val activo: String = "",
	val visible: String = "",
	val color: String = "",
	val formaSincronizar: String = "",
	val horas: String = "",

	) : IRoom


fun OrganizacionesRoom.toOrganizaciones(): Organizaciones {

	return Organizaciones(
		organizationCode = this.organizationCode,
		organizationName = this.organizationName,
		organizationId = this.organizationId,
		masterOrganizationId = this.masterOrganizationId,


		activo = Utils.esTrue(this.activo),
		visible = Utils.esTrue(this.visible),
		color =if3 ((this.color == null || this.color.isEmpty() ), "0", this.color).toInt(),
		formaSincronizar = if3(this.formaSincronizar.equals(FORMA_SINCRONIZAR.AUTO.toString()), FORMA_SINCRONIZAR.AUTO, FORMA_SINCRONIZAR.MANUAL),
		horas = this.horas,
	)
}

fun OrganizacionesRoom.fromOrganizaciones(organizacion: Organizaciones) = OrganizacionesRoom(
	organizationCode = organizacion.organizationCode,
	organizationName = organizacion.organizationName,
	organizationId = organizacion.organizationId,
	masterOrganizationId = organizacion.masterOrganizationId,

	activo = Utils.toSiNo(organizacion.activo),
	visible = Utils.toSiNo(organizacion.visible),
	color = organizacion.color.toString(),
	formaSincronizar = organizacion.formaSincronizar.toString(),
	horas = organizacion.horas,
)

