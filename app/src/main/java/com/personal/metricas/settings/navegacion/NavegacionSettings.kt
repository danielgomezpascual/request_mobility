package com.personal.metricas.settings.navegacion

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.personal.metricas.dashboards.navegacion.goto
import com.personal.metricas.settings.ui.SettingsScreen

fun NavGraphBuilder.NavegacionSettings(navController: NavController) {

	composable<ScreenSettings> {
		SettingsScreen() { navegacion ->
			goto(navegacion, navController)

		}
	}

}