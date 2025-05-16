package com.personal.requestmobility.kpi.data.ds.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.personal.requestmobility.core.room.BaseDaoExtended
import com.personal.requestmobility.kpi.data.ds.local.entidades.KpisRoom
import org.koin.core.component.KoinComponent

@Dao
abstract class KpisDao : BaseDaoExtended<KpisRoom>(), KoinComponent {
    override val TABLA: String
        get() = "Kpis"

    @Query("SELECT * FROM  Kpis")
    abstract suspend fun todasLectoras(): List<KpisRoom>


    @Query("SELECT * FROM  Kpis WHERE id = :id")
    abstract suspend fun getPorID(id: Int): KpisRoom?
}