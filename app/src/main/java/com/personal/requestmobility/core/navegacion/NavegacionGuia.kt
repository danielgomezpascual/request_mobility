package com.personal.requestmobility.core.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.personal.requestmobility.dashboards.navegacion.NavegacionDashboard
import com.personal.requestmobility.kpi.navegacion.NavegacionKpis
import com.personal.requestmobility.menu.navegacion.NavegavionMenu
import com.personal.requestmobility.menu.navegacion.ScreenMenu
import com.personal.requestmobility.transacciones.navegacion.NavegacionTransacciones

@Composable
fun NavegacionGuia() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ScreenMenu) {
        NavegavionMenu(navController)
        NavegacionTransacciones(navController)
        NavegacionKpis(navController)
        NavegacionDashboard(navController)
    }
}