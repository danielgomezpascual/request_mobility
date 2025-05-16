package com.personal.requestmobility.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.personal.requestmobility.kpi.data.ds.local.dao.KpisDao
import com.personal.requestmobility.kpi.data.ds.local.entidades.KpisRoom
import com.personal.requestmobility.transacciones.data.local.dao.TansaccionesDao
import com.personal.requestmobility.transacciones.data.local.entities.TransaccionesRoom


@Database(
    entities = [TransaccionesRoom::class, KpisRoom::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transaccionesDao(): TansaccionesDao
    abstract fun kpisDao(): KpisDao

}