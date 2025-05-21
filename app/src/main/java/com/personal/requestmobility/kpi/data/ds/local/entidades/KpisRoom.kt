package com.personal.requestmobility.kpi.data.ds.local.entidades

import androidx.compose.material3.rememberTooltipState
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.componentes.panel.PanelConfiguracion
import com.personal.requestmobility.core.composables.componentes.panel.PanelOrientacion
import com.personal.requestmobility.core.room.IRoom
import com.personal.requestmobility.core.utils._toJson
import com.personal.requestmobility.core.utils._toObjectFromJson
import com.personal.requestmobility.core.utils.if3
import com.personal.requestmobility.kpi.domain.entidades.Kpi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Entity(tableName = "Kpis")
data class KpisRoom(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val titulo: String = "",
    val descripcion: String = "",
    val sql: String = "",
    val configuracion: String = ""
) : IRoom


fun KpisRoom.toKpi() : Kpi{

    val configuracion = _toObjectFromJson<PanelConfiguracion>(this.configuracion)?: PanelConfiguracion()

    App.log.d(configuracion.orientacion)
    //configuracion.orientacion = if3(configuracion.orientacion.equals("HORIZONTAL"), PanelOrientacion.HORIZONTAL, PanelOrientacion.VERTICAL)

return  Kpi(
        id = this.id,
        titulo = this.titulo,
        descripcion = this.descripcion,
        sql = this.sql,
        configuracion = configuracion)
}




fun KpisRoom.fromKPI(kpi: Kpi)
     = KpisRoom(
        id = kpi.id,
        titulo = kpi.titulo,
        descripcion = kpi.descripcion,
        sql = kpi.sql,
        configuracion = _toJson(kpi.configuracion)
    )

