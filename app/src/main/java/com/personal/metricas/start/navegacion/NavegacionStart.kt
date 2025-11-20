package com.personal.metricas.start.navegacion

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.personal.metricas.dashboards.navegacion.goto
import com.personal.metricas.menu.screen.HerramientasScreen
import com.personal.metricas.menu.screen.HomeScreen
import com.personal.metricas.menu.screen.ScreenHerramientasInicial
import com.personal.metricas.organizaciones.ui.lista.ScreenListaOrganizacionesPlanificacion
import com.personal.metricas.sincronizacion.ui.lista.ListaOrganizacinesSincronizar
import com.personal.metricas.sincronizacion.ui.lista.StartOrganizaciones

fun NavGraphBuilder.NavegacionStart(navController: NavController) {
	composable<ScreenStart> {
		StartOrganizaciones(){
			navegacion ->
			goto(navegacion, navController)
		}
	}


	composable<ScreenStartHerramientas> {
		ScreenHerramientasInicial() { navegacion ->
			goto(navegacion, navController)
		}
	}


	composable<ScreenStartPlanificacion> {
		ScreenListaOrganizacionesPlanificacion() { navegacion ->
			goto(navegacion, navController)
		}
	}

}
