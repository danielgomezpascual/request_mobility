package com.personal.requestmobility.paneles.data.ds.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.personal.requestmobility.App
import com.personal.requestmobility.core.room.IRoom
import com.personal.requestmobility.core.utils.Utils
import com.personal.requestmobility.core.utils.Utils.esTrue
import com.personal.requestmobility.core.utils.Utils.toSiNo
import com.personal.requestmobility.core.utils._toJson
import com.personal.requestmobility.core.utils._toObjectFromJson
import com.personal.requestmobility.kpi.domain.entidades.Kpi
import com.personal.requestmobility.kpi.domain.interactors.ObtenerKpiCU
import com.personal.requestmobility.paneles.domain.entidades.Panel
import com.personal.requestmobility.paneles.domain.entidades.PanelConfiguracion
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.java.KoinJavaComponent.inject


@Entity(tableName = "Paneles")
data class PanelesRoom(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val titulo: String = "",
    val descripcion: String = "",
    val configuracion: String = "",
    val idKpi: Int = 0,
	val autogenerado : String = "N"
                      ) : IRoom


suspend fun PanelesRoom.toPanel(): Panel {
	
	var configuracionRoom = _toObjectFromJson<PanelConfiguracionRoom>(this.configuracion)
							?: PanelConfiguracionRoom()
	
	val obtenerKpi: ObtenerKpiCU = getKoin().get()
	

	val panel = Panel(
			id = this.id,
			titulo = this.titulo,
			descripcion = this.descripcion,
			configuracion = configuracionRoom.toConfiguracion(),
			kpi = obtenerKpi.obtener(this.idKpi),
			autogenerado = esTrue(this.autogenerado, "Y", false)
			)

	
	return panel
	
	
}


fun PanelesRoom.fromPanel(panel: Panel) = PanelesRoom(
		id = panel.id,
		titulo = panel.titulo,
		descripcion = panel.descripcion,
		configuracion = _toJson(PanelConfiguracionRoom.fromConfiguracion(panel.configuracion)),
		idKpi = panel.kpi.id,
		autogenerado = Utils.toSiNo(panel.autogenerado)
		)

