package com.personal.metricas.inicializador

import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.dashboards.domain.interactors.GuardarDashboardCU
import com.personal.metricas.endpoints.domain.interactors.GuardarEndPointCU
import com.personal.metricas.inicializador.domain.InicializadorManager
import com.personal.metricas.inicializador.domain.InicializadorOperaciones
import com.personal.metricas.inicializador.domain.InitDahsboardGeneral
import com.personal.metricas.inicializador.domain.InitDashboardOrganizaciones
import com.personal.metricas.inicializador.domain.comunes.KpisComunes
import com.personal.metricas.kpi.domain.interactors.GuardarKpiCU
import com.personal.metricas.paneles.domain.interactors.GuardarPanelCU
import com.personal.metricas.paneles.domain.interactors.ObtenerPanelCU
import org.koin.dsl.module

val modulosInicializador = module {


	single<InicializadorOperaciones> {

		InicializadorOperaciones(
			guardarKpis = get<GuardarKpiCU>(),
			guardarPaneles = get<GuardarPanelCU>(),
			guardarDashboard = get<GuardarDashboardCU>(),
			guardarEndPoint = get<GuardarEndPointCU>(),
			obtenerPanelCU = get<ObtenerPanelCU>(),


			)
	}


	single<KpisComunes> {
		KpisComunes(operaciones = get<InicializadorOperaciones>())
	}

	single<InitDahsboardGeneral> {
		InitDahsboardGeneral(
			operaciones = get<InicializadorOperaciones>(),
			comunes = get<KpisComunes>()
		)
	}
	single<InitDashboardOrganizaciones> {
		InitDashboardOrganizaciones(
			operaciones = get<InicializadorOperaciones>(),
			comunes = get<KpisComunes>()
		)
	}


	single<InicializadorManager> {

		InicializadorManager(
			operaciones = get<InicializadorOperaciones>(),
			initGeneral = get<InitDahsboardGeneral>(),
			initOrganizacioes = get<InitDashboardOrganizaciones>(),
			dialog = get<DialogManager>()
		)
	}
}