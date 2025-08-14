package com.personal.metricas.firebase.domain.interactors

import com.personal.metricas.App
import com.personal.metricas.dashboards.data.ds.local.dao.DashboardDao
import com.personal.metricas.dashboards.data.ds.local.entidades.DashboardRoom
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
		//todo: Sincronizar (descarga) los endpoints

		//NOTAS
		val notasRoom: List<NotasRoom> = firebase.obtenerDatos<NotasRoom>(Colecciones.NOTAS, identificadorUsuario)
		daoNotas.insert(notasRoom)
	}

}