package com.personal.metricas.log.data.ds.local.entidades


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.personal.metricas.core.room.IRoom
import com.personal.metricas.core.utils.if3
import com.personal.metricas.log.domain.entidades.Log
import com.personal.metricas.notas.domain.entidades.Notas
import com.personal.metricas.sincronizacion.domain.interactors.TIPO_SINCRONIZACION

@Entity(tableName = "Logs")
data class LogRoom(
	@PrimaryKey(autoGenerate = true)
	var id: Int = 0,
	val organization_code: String = "",
	val hora: String = "",
	val tipo: String = "",
	val trx: Int = 0,

	) : IRoom {
	companion object {

	}
}

fun LogRoom.toLog(): Log {

	return  Log(id = this.id,
				organization_code = this.organization_code,
				hora = this.hora,
				tipo =
					if3(this.tipo.equals("MANUAL", true), TIPO_SINCRONIZACION.MANUAL, TIPO_SINCRONIZACION.PLANIFICADA),
				trx = trx?:0

	)
}

fun LogRoom.fromLog(l: Log): LogRoom = LogRoom(
									   organization_code = l.organization_code,
									   hora = l.hora,
									   tipo = l.tipo.toString(),
									   trx = l.trx
)


