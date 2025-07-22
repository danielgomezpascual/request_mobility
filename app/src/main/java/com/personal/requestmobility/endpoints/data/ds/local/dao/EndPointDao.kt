package com.personal.requestmobility.endpoints.data.ds.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.personal.requestmobility.core.room.BaseDaoExtended
import com.personal.requestmobility.endpoints.data.ds.local.entidades.EndPointRoom
import org.koin.core.component.KoinComponent

@Dao
abstract class EndPointDao : BaseDaoExtended<EndPointRoom>(), KoinComponent {
	override val TABLA: String
		get() = "EndPoints"

	@Query("SELECT * FROM EndPoints")
	abstract suspend fun todosEndPoints(): List<EndPointRoom>

	@Query("SELECT * FROM EndPoints WHERE id = :id")
	abstract suspend fun getPorID(id: Int): EndPointRoom?
}