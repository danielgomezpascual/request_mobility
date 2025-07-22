package com.personal.metricas.endpoints.navegacion

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.personal.metricas.dashboards.navegacion.goto
import com.personal.metricas.endpoints.ui.screen.detalle.DetalleEndPointScreen
import com.personal.metricas.endpoints.ui.screen.listado.EndPointsListadoScreen

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