package com.personal.metricas.transacciones.data.ds.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.personal.metricas.core.room.BaseDaoExtended
import com.personal.metricas.transacciones.data.ds.local.entities.TransaccionesRoom
import org.koin.core.component.KoinComponent

@Dao
abstract class TansaccionesDao : BaseDaoExtended<TransaccionesRoom>(), KoinComponent {
    override val TABLA: String
        get() = "Transacciones"

    @Query("SELECT * FROM  Transacciones")
    abstract suspend fun todasTransacciones(): List<TransaccionesRoom>


    @Query("SELECT * FROM  Transacciones WHERE MOB_REQUEST_ID = :mob_request_id")
    abstract suspend fun getPorID(mob_request_id: Int): TransaccionesRoom?
}