package com.personal.requestmobility.inicializador

import com.personal.requestmobility.core.composables.dialogos.DialogManager
import com.personal.requestmobility.dashboards.domain.interactors.GuardarDashboardCU
import com.personal.requestmobility.inicializador.domain.InicializadorManager
import com.personal.requestmobility.inicializador.domain.InicializadorOperaciones
import com.personal.requestmobility.kpi.domain.interactors.GuardarKpiCU
import com.personal.requestmobility.paneles.domain.interactors.GuardarPanelCU
import org.koin.dsl.module
import kotlin.math.sin

val modulosInicializador = module{

	single<InicializadorOperaciones>{

		InicializadorOperaciones(
			guardarKpis =  get<GuardarKpiCU>(),
			guardarPaneles = get<GuardarPanelCU>(),
			guardarDashboard = get<GuardarDashboardCU>()
		)
	}

	single<InicializadorManager>{

		InicializadorManager(
			operaciones = get<InicializadorOperaciones>(),
			dialog = get<DialogManager>()
		)
	}


}