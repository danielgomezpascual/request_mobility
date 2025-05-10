package com.personal.requestmobility.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.personal.requestmobility.transacciones.data.local.dao.TansaccionesDao
import com.personal.requestmobility.transacciones.data.local.entities.TransaccionesRoom


@Database(
    entities = [TransaccionesRoom::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transacciones(): TansaccionesDao

}