package com.personal.metricas.notas.data.ds.local.entidades


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.personal.metricas.core.room.IRoom
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.core.utils._toJson
import com.personal.metricas.core.utils._toObjectFromJson
import com.personal.metricas.endpoints.domain.entidades.EndPoint
import com.personal.metricas.notas.domain.entidades.Notas

@Entity(tableName = "Notas")
data class NotasRoom(
	@PrimaryKey(autoGenerate = false)
	var hash: String = "",
	val descripcion: String = "",

) : IRoom

fun NotasRoom.toNotas(): Notas = Notas(
	hash = this.hash,
	descripcion = this.descripcion,

)

fun NotasRoom.fromNotas(notas: Notas): NotasRoom = NotasRoom(
	hash= notas.hash,
	descripcion = notas.descripcion,

)