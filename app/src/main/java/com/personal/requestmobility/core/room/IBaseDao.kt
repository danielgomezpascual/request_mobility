package com.personal.requestmobility.core.room

import android.database.Cursor
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.room.Update
import androidx.room.Upsert
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteQuery
import org.koin.java.KoinJavaComponent.inject


interface IBaseDao<T : IRoom> {

    val TABLA: String

    //Insert
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(entity: List<T>)


    @Upsert
    suspend fun upsert(entity: T): Long

    @Upsert
    suspend fun upsert(entity: List<T>)


    // Actualizar un objeto en la tabla correspondiente
    @Update
    suspend fun update(entity: T): Int

    @Update
    suspend fun update(entity: List<T>)

    // Eliminar un objeto de la tabla correspondiente
    @Delete
    suspend fun delete(entity: T)


    // Eliminar un objeto de la tabla correspondiente
    @Delete
    suspend fun delete(entity: List<T>)

    fun vaciarTabla() {
        val appDatabase: AppDatabase by inject<AppDatabase>(AppDatabase::class.java)
        val db: SupportSQLiteDatabase = appDatabase.openHelper.writableDatabase
        db.execSQL("DELETE FROM $TABLA")

    }

    @RawQuery
    fun getDataWithCursor(query: SupportSQLiteQuery): Cursor



}