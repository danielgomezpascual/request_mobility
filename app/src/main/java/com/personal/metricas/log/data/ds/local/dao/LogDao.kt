package com.personal.metricas.log.data.ds.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.personal.metricas.core.room.BaseDaoExtended
import com.personal.metricas.log.data.ds.local.entidades.LogRoom
import com.personal.metricas.log.domain.entidades.Log
import com.personal.metricas.notas.data.ds.local.entidades.NotasRoom
import org.koin.core.component.KoinComponent

@Dao
abstract class LogDao : BaseDaoExtended<LogRoom>(), KoinComponent {
	override val TABLA: String
		get() = "Log"

	@Query("SELECT * FROM Logs")
	abstract suspend fun todosLogs(): List<LogRoom>

	@Query("SELECT * FROM Logs WHERE id = :id")
	abstract suspend fun getPorID(id: Int): LogRoom?
}