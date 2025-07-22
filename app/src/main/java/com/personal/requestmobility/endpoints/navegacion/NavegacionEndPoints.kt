package com.personal.requestmobility.endpoints.navegacion

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.personal.requestmobility.core.navegacion.EventosNavegacion
import com.personal.requestmobility.dashboards.navegacion.goto
import com.personal.requestmobility.endpoints.ui.screen.detalle.DetalleEndPointScreen
import com.personal.requestmobility.endpoints.ui.screen.listado.EndPointsListadoScreen
import com.personal.requestmobility.kpi.navegacion.ScreenDetalleKpi
import com.personal.requestmobility.kpi.navegacion.ScreenListadoKpis

import com.personal.requestmobility.kpi.ui.screen.detalle.DetalleKpiScreen
import com.personal.requestmobility.kpi.ui.screen.listado.KpisListadoScreen
import com.personal.requestmobility.menu.navegacion.ScreenMenu

fun NavGraphBuilder.NavegacionEndPoints(navController: NavController) {

    composable<ScreenListadoEndPoints> {
        EndPointsListadoScreen  { navegacion ->
            goto(navegacion, navController)

        }
    }
    composable<ScreenDetalleEndPoints> { bk ->

        val screenDetalle: ScreenDetalleEndPoints = bk.toRoute()
        DetalleEndPointScreen (screenDetalle.id) { respuestaAccion ->
            // vuelve a la indicada...
            navController.navigate(ScreenListadoEndPoints) {
                popUpTo<ScreenListadoEndPoints>() { inclusive = true }
            }
        }
    }
}