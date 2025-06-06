package com.personal.requestmobility.dashboards.data.ds.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.personal.requestmobility.App
import com.personal.requestmobility.core.room.IRoom
import com.personal.requestmobility.core.utils.Utils.esTrue
import com.personal.requestmobility.core.utils.Utils.toSiNo
import com.personal.requestmobility.core.utils._toJson
import com.personal.requestmobility.dashboards.domain.entidades.Dashboard
import com.personal.requestmobility.kpi.domain.interactors.ObtenerKpiCU
import com.personal.requestmobility.paneles.domain.entidades.Panel
import com.personal.requestmobility.paneles.domain.interactors.ObtenerPanelCU
import org.koin.java.KoinJavaComponent.getKoin

@Entity(tableName = "Dashboard")
class DashboardRoom(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val nombre: String = "",
    val home: String  = "N",
    val logo: String = "",
    val descripcion: String = "",
    val paneles: String = ""
) : IRoom

suspend fun DashboardRoom.toDashboard(): Dashboard {

    val type = object : TypeToken<List<Panel>>() {}.type
    val listaPaneles: List<Panel> = Gson().fromJson<List<Panel>>(this.paneles, type)

    val obtenerPanel : ObtenerPanelCU =   getKoin().get()
    val obtenerKpi : ObtenerKpiCU =   getKoin().get()

    var listaPanelesActualizado = listOf<Panel>()
    listaPaneles.sortedBy { it.orden }.forEach { panel ->
        //actualizamos el panel
        val panelActualizado = obtenerPanel.obtener(panel.id).copy( seleccionado =  panel.seleccionado , orden = panel.orden)

        App.log.d("***" + panelActualizado.titulo + " -  " + panelActualizado.orden)
        //actualizamos el kpi dle panel
        val idKpi = panelActualizado.kpi.id
        val kpiActualizado = obtenerKpi.obtener(idKpi)

        val p = panelActualizado.copy(kpi = kpiActualizado)

        listaPanelesActualizado = listaPanelesActualizado.plus(p)

    }

    return Dashboard(
        id = this.id,
        nombre = this.nombre,
        home =  esTrue(valor = this.home.toString()),
        logo = this.logo,
        descripcion = this.descripcion,
        paneles = listaPanelesActualizado
    )
}

fun DashboardRoom.fromDashboard(dashboard: Dashboard): DashboardRoom = DashboardRoom(
    id = dashboard.id, // Room maneja la autogeneración si id es 0 en la inserción.
    nombre = dashboard.nombre,
    home = toSiNo(dashboard.home),
    logo = dashboard.logo,
    descripcion = dashboard.descripcion,
    paneles = _toJson(dashboard.paneles)

)

