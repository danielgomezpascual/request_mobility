package com.personal.requestmobility.dashboards.data.ds.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.personal.requestmobility.core.room.IRoom
import com.personal.requestmobility.dashboards.domain.entidades.Dashboard

@Entity(tableName = "Dashboard")
class DashboardRoom(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val nombre: String = "",
    val descripcion: String = ""
): IRoom

fun DashboardRoom.toDashboard(): Dashboard = Dashboard(
    id = this.id,
    nombre = this.nombre,
    descripcion = this.descripcion
)

// Siguiendo el patrón del ejemplo: DashboardRoom().fromDashboard(dashboard)
fun DashboardRoom.fromDashboard(dashboard: Dashboard): DashboardRoom = DashboardRoom(
    id = dashboard.id, // Room maneja la autogeneración si id es 0 en la inserción.
    nombre = dashboard.nombre,
    descripcion = dashboard.descripcion
)