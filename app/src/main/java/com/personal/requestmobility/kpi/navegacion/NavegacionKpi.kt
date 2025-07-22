package com.personal.requestmobility.kpi.navegacion

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.personal.requestmobility.dashboards.navegacion.goto
import com.personal.requestmobility.kpi.ui.screen.detalle.DetalleKpiScreen
import com.personal.requestmobility.kpi.ui.screen.listado.KpisListadoScreen

fun NavGraphBuilder.NavegacionKpis(navController: NavController) {

	composable<ScreenListadoKpis> {
		KpisListadoScreen() { navegacion ->
			goto(navegacion, navController)

		}
	}
	composable<ScreenDetalleKpi> { bk ->

		val screenDetalle: ScreenDetalleKpi = bk.toRoute()
		DetalleKpiScreen(screenDetalle.id) { respuestaAccion ->
			// vuelve a la indicada...
			navController.navigate(ScreenListadoKpis) {
				popUpTo<ScreenListadoKpis>() { inclusive = true }
			}
		}
	}
}