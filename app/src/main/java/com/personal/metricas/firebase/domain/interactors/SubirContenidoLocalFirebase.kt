package com.personal.metricas.firebase.domain.interactors

import com.personal.metricas.dashboards.data.ds.local.dao.DashboardDao
import com.personal.metricas.endpoints.data.ds.local.dao.EndPointDao
import com.personal.metricas.endpoints.domain.entidades.EndPoint
import com.personal.metricas.firebase.data.entidades.DashboardFirestore
import com.personal.metricas.firebase.data.entidades.EndPointFirestore
import com.personal.metricas.firebase.domain.FirebaseManager
import com.personal.metricas.firebase.data.entidades.KpiFirestore
import com.personal.metricas.firebase.data.entidades.NotasFirestore
import com.personal.metricas.firebase.data.entidades.PanelFirestore
import com.personal.metricas.kpi.data.ds.local.dao.KpisDao
import com.personal.metricas.notas.data.ds.local.dao.NotasDao
import com.personal.metricas.notas.domain.entidades.Notas
import com.personal.metricas.paneles.data.ds.local.dao.PanelesDao

class SubirContenidoLocalFirebase(
	private val firebase: FirebaseManager,
	private val daoKpi: KpisDao,
	private val daoPanel: PanelesDao,
	private val daoDashboard: DashboardDao,
	private val daoNotas: NotasDao,
	private val daoEndPoint: EndPointDao,

) {

	suspend fun uploadFirestore() {
		val identificadorUsuario = firebase.dameIdentidicadorUsuario()

		//val identificadorUsuario = "test_pruebas_dgp"


		eliminarDatosAntiguos(identificadorUsuario)
		uploadKpi(identificadorUsuario)
		uploadPaneles(identificadorUsuario)
		uploadDashboard(identificadorUsuario)
		uploadEndPoints(identificadorUsuario)
		uploadNotas(identificadorUsuario)


		firebase.finalizar()

	}

	private suspend fun eliminarDatosAntiguos(identificadorUsuario: String) {
		firebase.eliminarPorUsuario(Colecciones.KPI, identificadorUsuario)
		firebase.eliminarPorUsuario(Colecciones.PANELES, identificadorUsuario)
		firebase.eliminarPorUsuario(Colecciones.DASHBOARD, identificadorUsuario)
		firebase.eliminarPorUsuario(Colecciones.NOTAS, identificadorUsuario)
		firebase.eliminarPorUsuario(Colecciones.END_POINTS, identificadorUsuario)

	}

	private suspend fun uploadKpi(identificadorUsuario: String) {
		daoKpi.getAll().forEach { kpiRoom ->
			val kpiFirestore = KpiFirestore.Companion.fromRoom(kpiRoom, identificadorUsuario)
			firebase.guardarFirestore(Colecciones.KPI, kpiFirestore)
		}
	}

	private suspend fun uploadPaneles(identificadorUsuario: String) {
		daoPanel.todosPaneles().forEach { panelRoom ->
			val panelFirestore = PanelFirestore.fromRoom(panelRoom, identificadorUsuario)
			firebase.guardarFirestore(Colecciones.PANELES, panelFirestore)
		}
	}

	private suspend fun uploadDashboard(identificadorUsuario: String) {
		daoDashboard.todosDashboards().forEach { dashboardRoom ->
			val dashboardFirestore = DashboardFirestore.fromRoom(dashboardRoom, identificadorUsuario)
			firebase.guardarFirestore(Colecciones.DASHBOARD, dashboardFirestore)
		}
	}


	private suspend fun uploadNotas(identificadorUsuario: String) {
		daoNotas.todasNotas().forEach { notasRoom  ->
			val notasFirestore = NotasFirestore.fromRoom(notasRoom, identificadorUsuario)
			firebase.guardarFirestore(Colecciones.NOTAS, notasFirestore)
		}
	}

	private suspend fun uploadEndPoints(identificadorUsuario: String) {
		daoEndPoint.todosEndPoints().forEach { endpointRoom  ->
			val notasFirestore = EndPointFirestore.fromRoom(endpointRoom, identificadorUsuario)
			firebase.guardarFirestore(Colecciones.END_POINTS, notasFirestore)
		}
	}


}