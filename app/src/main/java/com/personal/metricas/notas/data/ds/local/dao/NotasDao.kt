package com.personal.metricas.notas.data.ds.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.personal.metricas.core.room.BaseDaoExtended
import com.personal.metricas.endpoints.data.ds.local.entidades.EndPointRoom
import com.personal.metricas.notas.data.ds.local.entidades.NotasRoom
import org.koin.core.component.KoinComponent

@Dao
abstract class NotasDao : BaseDaoExtended<NotasRoom>(), KoinComponent {
	override val TABLA: String
		get() = "Notas"

	@Query("SELECT * FROM Notas")
	abstract suspend fun todasNotas(): List<NotasRoom>

	@Query("SELECT * FROM Notas WHERE hash = :hash")
	abstract suspend fun getPorID(hash: String): NotasRoom?
}