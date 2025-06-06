package com.personal.requestmobility.menu.navegacion

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.personal.danility2.usuarios.navegacion.DockTransacciones
import com.personal.requestmobility.dashboards.navegacion.CuadriculaDashboards
import com.personal.requestmobility.dashboards.navegacion.ListadoDashboards
import com.personal.requestmobility.dashboards.navegacion.goto
import com.personal.requestmobility.kpi.navegacion.ScreenListadoKpis

import com.personal.requestmobility.menu.screen.HomeScreen
import com.personal.requestmobility.menu.screen.HomeVM
import com.personal.requestmobility.paneles.navegacion.ScreenListadoPaneles

fun NavGraphBuilder.NavegavionMenu(navController: NavController) {
    composable<ScreenMenu> {
        HomeScreen  { navegacion ->
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
