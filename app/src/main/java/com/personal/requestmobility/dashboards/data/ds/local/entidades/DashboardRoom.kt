package com.personal.requestmobility.dashboards.data.ds.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.personal.requestmobility.core.room.IRoom
import com.personal.requestmobility.core.utils.Utils
import com.personal.requestmobility.core.utils._toJson
import com.personal.requestmobility.core.utils._toObjectFromJson
import com.personal.requestmobility.dashboards.domain.entidades.Dashboard
import com.personal.requestmobility.dashboards.domain.entidades.KpiPaneles

@Entity(tableName = "Dashboard")
class DashboardRoom(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val nombre: String = "",
    val logo: String = "",
    val descripcion: String = "",
    val paneles: String = ""
) : IRoom

fun DashboardRoom.toDashboard(): Dashboard {
    val type = object : TypeToken<List<KpiPaneles>>() {}.type
    return Dashboard(
        id = this.id,
        nombre = this.nombre,
        logo = this.logo,
        descripcion = this.descripcion,
        paneles = Gson().fromJson<List<KpiPaneles>>(this.paneles, type)
    )
}

fun DashboardRoom.fromDashboard(dashboard: Dashboard): DashboardRoom = DashboardRoom(
    id = dashboard.id, // Room maneja la autogeneración si id es 0 en la inserción.
    nombre = dashboard.nombre,
    logo = dashboard.logo,
    descripcion = dashboard.descripcion,
    paneles = _toJson(dashboard.paneles)

)

