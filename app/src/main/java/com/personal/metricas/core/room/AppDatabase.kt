package com.personal.metricas.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.personal.metricas.dashboards.data.ds.local.dao.DashboardDao
import com.personal.metricas.dashboards.data.ds.local.entidades.DashboardRoom
import com.personal.metricas.endpoints.data.ds.local.dao.EndPointDao
import com.personal.metricas.endpoints.data.ds.local.entidades.EndPointRoom
import com.personal.metricas.kpi.data.ds.local.dao.KpisDao
import com.personal.metricas.kpi.data.ds.local.entidades.KpisRoom
import com.personal.metricas.notas.data.ds.local.dao.NotasDao
import com.personal.metricas.notas.data.ds.local.entidades.NotasRoom
import com.personal.metricas.paneles.data.ds.local.dao.PanelesDao
import com.personal.metricas.paneles.data.ds.local.entidades.PanelesRoom
import com.personal.metricas.transacciones.data.ds.local.dao.TansaccionesDao
import com.personal.metricas.transacciones.data.ds.local.entities.TransaccionesRoom


@Database(
    entities = [TransaccionesRoom::class, KpisRoom::class, DashboardRoom::class,
        PanelesRoom::class, EndPointRoom::class, NotasRoom::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transaccionesDao(): TansaccionesDao
    abstract fun kpisDao(): KpisDao
    abstract fun dashboardDao(): DashboardDao
    abstract fun panelesDao(): PanelesDao
    abstract fun endPointDao(): EndPointDao
    abstract fun notasDao(): NotasDao


}