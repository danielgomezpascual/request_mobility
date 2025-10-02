package com.personal.metricas.paneles.data.ds.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.api.EndpointOrBuilder
import com.personal.metricas.core.room.IRoom
import com.personal.metricas.core.utils.Utils
import com.personal.metricas.core.utils.Utils.esTrue
import com.personal.metricas.core.utils._toJson
import com.personal.metricas.core.utils._toObjectFromJson
import com.personal.metricas.core.utils.if3
import com.personal.metricas.dashboards.domain.entidades.Dashboard
import com.personal.metricas.dashboards.domain.interactors.ObtenerDashboardCU
import com.personal.metricas.dashboards.domain.interactors.ObtenerDashboardsCU
import com.personal.metricas.endpoints.domain.entidades.EndPoint
import com.personal.metricas.endpoints.domain.interactors.ObtenerEndPointCU
import com.personal.metricas.kpi.domain.entidades.Kpi
import com.personal.metricas.kpi.domain.interactors.ObtenerKpiCU
import com.personal.metricas.kpi.ui.composables.KpiListItem
import com.personal.metricas.menu.Features
import com.personal.metricas.paneles.domain.entidades.Conector
import com.personal.metricas.paneles.domain.entidades.Panel
import com.personal.metricas.paneles.domain.entidades.TiposPanel
import org.koin.java.KoinJavaComponent.getKoin


@Entity(tableName = "Paneles")
data class PanelesRoom(
	@PrimaryKey(autoGenerate = true)
	var id: Int = 0,
	val titulo: String = "",
	val descripcion: String = "",
	val configuracion: String = "",
	val autogenerado: String = "N",
	val tipoPanel: Int = 0,
	val idKpi: Int = 0,
	val idEndPoint: Int = 0,
	val color: Int = 0,
	val conector: String = ""
) : IRoom


suspend fun PanelesRoom.toPanel(): Panel {

	var configuracionRoom = _toObjectFromJson<PanelConfiguracionRoom>(this.configuracion)
							?: PanelConfiguracionRoom()


	val tipo = this.tipoPanel
	val obtenerKpi: ObtenerKpiCU = getKoin().get()

	val obtenerEndPoint: ObtenerEndPointCU = getKoin().get()
	//val obtenerDashboard: ObtenerDashboardCU = getKoin().get()

	var kpi: Kpi = Kpi()
	var endPoint: EndPoint = EndPoint()
	var conector: Conector = Conector()
	//var dashboard: Dashboard = Dashboard()

	when (tipo) {
		0 -> kpi = obtenerKpi.obtener(this.idKpi) //KPI
		1 -> endPoint = obtenerEndPoint.obtener(this.idEndPoint)//END - PONT
		2 -> endPoint = endPoint //TEXTO
		3 ->{
			kpi = obtenerKpi.obtener(this.idKpi) //KPI
			conector = _toObjectFromJson<Conector>(this.conector) ?: Conector()
		}
	}





	val panel = Panel(
		id = this.id,
		titulo = this.titulo,
		descripcion = this.descripcion,
		configuracion = configuracionRoom.toConfiguracion(),
		tipoPanel = TiposPanel.dameTipoPanel(this.tipoPanel),
		kpi =kpi,
		endPoint = endPoint,
		autogenerado = esTrue(this.autogenerado, "Y", false), 
		color =  this.color,
		conector = conector

	)


	return panel


}


fun PanelesRoom.fromPanel(panel: Panel) = PanelesRoom(
	id = panel.id,
	titulo = panel.titulo,
	descripcion = panel.descripcion,
	configuracion = _toJson(PanelConfiguracionRoom.fromConfiguracion(panel.configuracion)),
	idKpi = panel.kpi.id,
	autogenerado = Utils.toSiNo(panel.autogenerado),
	idEndPoint = panel.endPoint.id,
	tipoPanel = TiposPanel.dameIndicePanel(panel.tipoPanel),
	color = panel.color,
	conector = _toJson(panel.conector)
)

