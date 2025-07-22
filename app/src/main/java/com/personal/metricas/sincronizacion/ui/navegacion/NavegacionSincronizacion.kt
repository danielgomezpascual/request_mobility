package com.personal.metricas.sincronizacion.ui.navegacion

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.personal.metricas.dashboards.navegacion.goto
import com.personal.metricas.sincronizacion.ui.lista.ListaOrganizacinesSincronizar

fun NavGraphBuilder.NavegacionSincronizacion(navController: NavHostController) {

    composable<ScreenOrganizacionesSincronizacion> {
        ListaOrganizacinesSincronizar(){navegacion ->
            goto(navegacion, navController)
        }
    }


}
