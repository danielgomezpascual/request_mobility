package com.personal.metricas.menu.navegacion

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.personal.metricas.dashboards.navegacion.goto
import com.personal.metricas.menu.screen.HerramientasScreen
import com.personal.metricas.menu.screen.HomeScreen

fun NavGraphBuilder.NavegavionMenu(navController: NavController) {
	composable<ScreenMenu> {
		HomeScreen { navegacion ->
			goto(navegacion, navController)
		}
	}


	composable<ScreenHerramientas> {
		HerramientasScreen() { navegacion ->
			goto(navegacion, navController)
		}
	}
	/*  HomeScreen() { modulo ->
		  when (modulo) {
			  Modulos.Transacciones -> navController.navigate(DockTransacciones)
			  Modulos.Kpis -> navController.navigate(ScreenListadoKpis)
			  Modulos.Dashboards -> navController.navigate(ListadoDashboards)
			  Modulos.Cuadricula -> navController.navigate(CuadriculaDashboards)
			  Modulos.Paneles ->  navController.navigate(ScreenListadoPaneles)
		  }
	  }*/
}
