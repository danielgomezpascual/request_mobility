package com.personal.metricas.inicializador

import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.dashboards.domain.interactors.GuardarDashboardCU
import com.personal.metricas.inicializador.domain.InicializadorManager
import com.personal.metricas.inicializador.domain.InicializadorOperaciones
import com.personal.metricas.kpi.domain.interactors.GuardarKpiCU
import com.personal.metricas.paneles.domain.interactors.GuardarPanelCU
import org.koin.dsl.module

val modulosInicializador = module {

	single<InicializadorOperaciones> {

		InicializadorOperaciones(
			guardarKpis = get<GuardarKpiCU>(),
			guardarPaneles = get<GuardarPanelCU>(),
			guardarDashboard = get<GuardarDashboardCU>()
		)
	}

	single<InicializadorManager> {

		InicializadorManager(
			operaciones = get<InicializadorOperaciones>(),
			dialog = get<DialogManager>()
		)
	}


}