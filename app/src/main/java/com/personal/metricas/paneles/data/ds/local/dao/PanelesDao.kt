package com.personal.metricas.paneles.data.ds.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.personal.metricas.core.room.BaseDaoExtended
import com.personal.metricas.paneles.data.ds.local.entidades.PanelesRoom
import org.koin.core.component.KoinComponent

@Dao
abstract class PanelesDao : BaseDaoExtended<PanelesRoom>(), KoinComponent {
    override val TABLA: String
        get() = "Paneles"

    @Query("SELECT * FROM  Paneles")
    abstract suspend fun todosPaneles(): List<PanelesRoom>


    @Query("SELECT * FROM  Paneles WHERE id = :id")
    abstract suspend fun getPorID(id: Int): PanelesRoom?
}