package com.personal.metricas.firebase.domain.interactors

import com.personal.metricas.App
import com.personal.metricas.dashboards.data.ds.local.dao.DashboardDao
import com.personal.metricas.dashboards.data.ds.local.entidades.DashboardRoom
import com.personal.metricas.endpoints.data.ds.local.dao.EndPointDao
import com.personal.metricas.endpoints.data.ds.local.entidades.EndPointRoom
import com.personal.metricas.endpoints.domain.entidades.EndPoint
import com.personal.metricas.firebase.domain.FirebaseManager
import com.personal.metricas.kpi.data.ds.local.dao.KpisDao
import com.personal.metricas.kpi.data.ds.local.entidades.KpisRoom
import com.personal.metricas.notas.data.ds.local.dao.NotasDao
import com.personal.metricas.notas.data.ds.local.entidades.NotasRoom
import com.personal.metricas.paneles.data.ds.local.dao.PanelesDao
import com.personal.metricas.paneles.data.ds.local.entidades.PanelesRoom

class DescargarContenidoFirestore(
	private val firebase: FirebaseManager,
	private val daoKpi: KpisDao,
	private val daoPanel: PanelesDao,
	private val daoDashboard: DashboardDao,
	private val daoNotas: NotasDao,
	private val daoEndPoint: EndPointDao,
) {

	suspend fun descargar() {

		val identificadorUsuario = firebase.dameIdentidicadorUsuario()

		val kpisRoom: List<KpisRoom> = firebase.obtenerDatos<KpisRoom>(Colecciones.KPI, identificadorUsuario)
		daoKpi.insert(kpisRoom)

		val panelesRoom: List<PanelesRoom> = firebase.obtenerDatos<PanelesRoom>(Colecciones.PANELES, identificadorUsuario)
		daoPanel.insert(panelesRoom)

		val dashboardRoom: List<DashboardRoom> = firebase.obtenerDatos<DashboardRoom>(Colecciones.DASHBOARD, identificadorUsuario)
		daoDashboard.insert(dashboardRoom)

		//ENDOINTS
		val endPointRoom: List<EndPointRoom> = firebase.obtenerDatos<EndPointRoom>(Colecciones.END_POINTS, identificadorUsuario)
		daoEndPoint.insert(endPointRoom)

		//NOTAS
		val notasRoom: List<NotasRoom> = firebase.obtenerDatos<NotasRoom>(Colecciones.NOTAS, identificadorUsuario)
		daoNotas.insert(notasRoom)
	}

}