package com.personal.requestmobility.dashboards.data.ds.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.personal.requestmobility.core.room.BaseDaoExtended
import com.personal.requestmobility.dashboards.data.ds.local.entidades.DashboardRoom
import com.personal.requestmobility.dashboards.domain.entidades.Dashboard
import org.koin.core.component.KoinComponent

@Dao
abstract class DashboardDao : BaseDaoExtended<DashboardRoom>(), KoinComponent { // Ajusta KoinComponent si no es necesario
    override val TABLA: String
        get() = "Dashboard"


    @Query("SELECT * FROM Dashboard WHERE home = 'Y' ")
    abstract suspend fun todosDashboardsHome(): List<DashboardRoom>



    @Query("SELECT * FROM Dashboard")
    abstract suspend fun todosDashboards(): List<DashboardRoom>

    @Query("SELECT * FROM Dashboard WHERE id = :id")
    abstract suspend fun getPorID(id: Int): DashboardRoom?
}