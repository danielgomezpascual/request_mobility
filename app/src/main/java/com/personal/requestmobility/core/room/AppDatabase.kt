package com.personal.requestmobility.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.personal.requestmobility.dashboards.data.ds.local.dao.DashboardDao
import com.personal.requestmobility.dashboards.data.ds.local.entidades.DashboardRoom
import com.personal.requestmobility.kpi.data.ds.local.dao.KpisDao
import com.personal.requestmobility.kpi.data.ds.local.entidades.KpisRoom
import com.personal.requestmobility.paneles.data.ds.local.dao.PanelesDao
import com.personal.requestmobility.paneles.data.ds.local.entidades.PanelesRoom
import com.personal.requestmobility.transacciones.data.ds.local.dao.TansaccionesDao
import com.personal.requestmobility.transacciones.data.ds.local.entities.TransaccionesRoom


@Database(
    entities = [TransaccionesRoom::class, KpisRoom::class, DashboardRoom::class, PanelesRoom::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transaccionesDao(): TansaccionesDao
    abstract fun kpisDao(): KpisDao
    abstract fun dashboardDao(): DashboardDao
    abstract fun panelesDao(): PanelesDao

}