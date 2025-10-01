package com.personal.metricas.organizaciones.data.ds.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.personal.metricas.core.room.BaseDaoExtended
import com.personal.metricas.kpi.data.ds.local.entidades.KpisRoom
import com.personal.metricas.organizaciones.data.ds.local.entidades.OrganizacionesRoom
import org.koin.core.component.KoinComponent

@Dao
abstract class OrganizacionesDao : BaseDaoExtended<OrganizacionesRoom>(), KoinComponent {
    override val TABLA: String
        get() = "Organizaciones"

    @Query("SELECT * FROM  Organizaciones")
    abstract suspend fun getAll(): List<OrganizacionesRoom>


    @Query("SELECT * FROM  Organizaciones WHERE organizationCode = :organizationCode")
    abstract suspend fun getPorID(organizationCode: String): OrganizacionesRoom?



}