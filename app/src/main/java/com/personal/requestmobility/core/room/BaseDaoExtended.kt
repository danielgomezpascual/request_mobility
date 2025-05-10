package com.personal.requestmobility.core.room

import android.database.Cursor
import androidx.sqlite.db.SupportSQLiteDatabase
import com.personal.requestmobility.App
import org.koin.java.KoinJavaComponent.inject
import kotlin.getValue

abstract class BaseDaoExtended<T : IRoom> : IBaseDao<T> {

    fun sqlToList(clazz: Class<T>, sql: String = "SELECT  * FROM $TABLA LIMIT 100"): List<T> {
        var lista: List<T> = listOf<T>()
        val appDatabase: AppDatabase by inject<AppDatabase>(AppDatabase::class.java)
        val db: SupportSQLiteDatabase = appDatabase.openHelper.writableDatabase
        val cursor: Cursor = db.query(sql)
        cursor.moveToFirst()
        var numeroColumnas = cursor.columnCount
        if (numeroColumnas < 0) numeroColumnas = 0

        do {
            val inst = clazz.getDeclaredConstructor().newInstance()
            for (indiceColumna in 0..numeroColumnas - 1) {
                val nombreColumna = cursor.getColumnName(indiceColumna)
                val propiedad = clazz.declaredFields.find { it.name == nombreColumna }
                if (propiedad != null) {
                    propiedad.isAccessible = true
                    when (cursor.getType(indiceColumna)) {
                        Cursor.FIELD_TYPE_NULL -> propiedad.set(inst, null)
                        Cursor.FIELD_TYPE_INTEGER -> propiedad.setInt(inst, cursor.getInt(indiceColumna))
                        Cursor.FIELD_TYPE_FLOAT -> propiedad.setFloat(inst, cursor.getFloat(indiceColumna))
                        Cursor.FIELD_TYPE_STRING -> propiedad.set(inst, cursor.getString(indiceColumna))
                        Cursor.FIELD_TYPE_BLOB -> propiedad.set(inst, cursor.getBlob(indiceColumna))
                        else -> "Unknown"
                    }
                }
            }
            lista = lista.plus(inst)

        } while (cursor.moveToNext())
        cursor.close()
        return lista
    }

    fun sqlToListString(sql: String = "SELECT * FROM $TABLA"): ResultadoEjecucionSQL {

        val resultadoEjecucionSQL: ResultadoEjecucionSQL = ResultadoEjecucionSQL()

        val appDatabase: AppDatabase by inject(AppDatabase::class.java)
        val db: SupportSQLiteDatabase = appDatabase.openHelper.readableDatabase // Usamos readableDatabase para operaciones de lectura

        db.query(sql).use { cursor -> // Usamos 'use' para asegurar que el cursor se cierre automáticamente
            val columnCount = cursor.columnCount
            if (columnCount <= 0) {
                resultadoEjecucionSQL.filas = emptyList()
                resultadoEjecucionSQL.titulos = emptyList()

                return resultadoEjecucionSQL
            } // Evitamos crear arrays vacíos innecesariamente

            resultadoEjecucionSQL.titulos = (0 until columnCount).map { cursor.getColumnName(it) }
            val results = mutableListOf<List<String>>()
            while (cursor.moveToNext()) {
                val row = (0 until columnCount).map { columnIndex ->
                    when (cursor.getType(columnIndex)) {
                        Cursor.FIELD_TYPE_NULL -> "NULL"
                        Cursor.FIELD_TYPE_INTEGER -> cursor.getInt(columnIndex).toString()
                        Cursor.FIELD_TYPE_FLOAT -> cursor.getFloat(columnIndex).toString()
                        Cursor.FIELD_TYPE_STRING -> cursor.getString(columnIndex)
                        Cursor.FIELD_TYPE_BLOB -> "Blob"
                        else -> "Unknown"
                    }
                }
                results.add(row)
            }
            resultadoEjecucionSQL.filas = results
            return resultadoEjecucionSQL
        }
    }

    fun mostrarContenido(sql: String = "SELECT * FROM $TABLA") {
        val appDatabase: AppDatabase by inject(AppDatabase::class.java)
        val db: SupportSQLiteDatabase = appDatabase.openHelper.readableDatabase // Usamos readableDatabase para lectura

        db.query(sql).use { cursor ->
            val columnCount = cursor.columnCount
            if (columnCount <= 0) return // No hay nada que mostrar

            val columnNames = (0 until columnCount).map { cursor.getColumnName(it) }
            val results = mutableListOf<List<String>>()
            val columnWidths = IntArray(columnCount)

            while (cursor.moveToNext()) {
                val row = (0 until columnCount).map { columnIndex ->
                    val value = when (cursor.getType(columnIndex)) {
                        Cursor.FIELD_TYPE_NULL -> "NULL"
                        Cursor.FIELD_TYPE_INTEGER -> cursor.getInt(columnIndex).toString()
                        Cursor.FIELD_TYPE_FLOAT -> cursor.getFloat(columnIndex).toString()
                        Cursor.FIELD_TYPE_STRING -> cursor.getString(columnIndex)
                        Cursor.FIELD_TYPE_BLOB -> "Blob"
                        else -> "Unknown"
                    }
                    if (value.length > columnWidths[columnIndex]) {
                        columnWidths[columnIndex] = value.length
                    }
                    value
                }
                results.add(row)
            }

            val totalWidth = columnWidths.sumOf { it + 2 } // Sumamos los anchos con el padding

            App.log.linea(totalWidth)
            App.log.c("SQL: $sql")

            val header = columnNames.mapIndexed { index, name ->
                name.padEnd(columnWidths[index] + 2)
            }.joinToString("")
            App.log.d(header)

            results.forEach { row ->
                val line = row.mapIndexed { index, value ->
                    value.padEnd(columnWidths[index] + 2)
                }.joinToString("")
                App.log.d(line)
            }

            App.log.d("")
            App.log.d("- ${results.size} filas - ")
            App.log.linea(totalWidth)
        }
    }
}