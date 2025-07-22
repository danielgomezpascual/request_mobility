package com.personal.metricas.core.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.personal.metricas.dashboards.navegacion.NavegacionDashboard
import com.personal.metricas.endpoints.navegacion.NavegacionEndPoints
import com.personal.metricas.kpi.navegacion.NavegacionKpis
import com.personal.metricas.menu.navegacion.NavegavionMenu
import com.personal.metricas.menu.navegacion.ScreenMenu
import com.personal.metricas.paneles.navegacion.NavegacionPaneles
import com.personal.metricas.sincronizacion.ui.navegacion.NavegacionSincronizacion
import com.personal.metricas.transacciones.navegacion.NavegacionTransacciones

@Composable
fun NavegacionGuia() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ScreenMenu) {
        NavegavionMenu(navController)
        NavegacionTransacciones(navController)
        NavegacionKpis(navController)
        NavegacionPaneles(navController)
        NavegacionSincronizacion(navController)
        NavegacionDashboard(navController)
        NavegacionEndPoints(navController)
    }
}