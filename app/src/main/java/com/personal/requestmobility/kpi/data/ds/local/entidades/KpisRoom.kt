package com.personal.requestmobility.kpi.data.ds.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.personal.requestmobility.core.room.IRoom
import com.personal.requestmobility.kpi.domain.entidades.Kpi


@Entity(tableName = "Kpis")
data class KpisRoom(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val titulo: String = "",
    val descripcion: String = "",
    val sql: String = "",
    val configuracion: String = ""
) : IRoom


fun KpisRoom.toKpi() = Kpi(
    id = this.id,
    titulo = this.titulo,
    descripcion = this.descripcion,
    sql = this.sql,
    //configuracion = this.configuracion
)


fun KpisRoom.fromKPI(kpi: Kpi)
     = KpisRoom(
        id = kpi.id,
        titulo = kpi.titulo,
        descripcion = kpi.descripcion,
        sql = kpi.sql,
        //configuracion = kpi.configuracion
    )

