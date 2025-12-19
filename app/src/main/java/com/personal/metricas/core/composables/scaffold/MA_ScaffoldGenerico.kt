package com.personal.metricas.core.composables.scaffold

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.personal.metricas.App
import com.personal.metricas.core.composables.componentes.Cabecera
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.imagenes.MA_Icono
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.utils.Preferencias
import com.personal.metricas.core.utils._toJson
import com.personal.metricas.dashboards.domain.interactors.ObtenerDashboardsAccesoDirectoCU
import com.personal.metricas.dashboards.ui.entidades.DashboardUI
import com.personal.metricas.dashboards.ui.entidades.fromDashboard
import com.personal.metricas.menu.Features
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MA_ScaffoldGenerico(
	tituloScreen: TituloScreen,
	navegacion: (EventosNavegacion) -> Unit,
	accionesSuperiores: @Composable () -> Unit,
	contenido: @Composable () -> Unit,
	mostrarBotonesSuperioresYBarraInferior: Boolean = true
	) {

	val obtenerAccesoDirectoCU: ObtenerDashboardsAccesoDirectoCU = koinInject()
	var accesosDirectos by remember { mutableStateOf(emptyList<DashboardUI>()) }
	
	LaunchedEffect(Unit) {
		obtenerAccesoDirectoCU.execute().collect { lista ->
			accesosDirectos = lista.map { DashboardUI().fromDashboard(it) }
		}
	}


	Scaffold(
		containerColor = Color(red = 245, green = 245, blue = 245, alpha = 100),
		topBar = {

			Box(modifier = Modifier
				.padding(vertical = 6.dp)
			) {
				Column {
					if (mostrarBotonesSuperioresYBarraInferior) {
						Cabecera(tituloScreen, navegacion, accionesSuperiores)
					}

				}
			}


		}, bottomBar = {

			if (mostrarBotonesSuperioresYBarraInferior){
			BottomAppBar(
				modifier = Modifier.height(120.dp),
				containerColor = Color(174, 213, 129, 10),
				tonalElevation = 50.dp
			) {
				Row(
					modifier = Modifier.fillMaxWidth().height(80.dp),
					horizontalArrangement = Arrangement.SpaceAround,
					verticalAlignment = Alignment.CenterVertically


				) {
					if (App.sharedPrerfences.get<Boolean>(Preferencias.ACCESO_SINCRONIZACION, true)) {
						// Blue Theme for Sync
						ColoredNavItem(
							icon = Features.Sincronizar().icono,
							backgroundColor = Color(0xFFE3F2FD), // Light Blue
							iconColor = Color(0xFF1565C0), // Dark Blue
							onClick = { navegacion(EventosNavegacion.Sincronizacion) }
						)
					}

					val haptic = LocalHapticFeedback.current

					// Gold/Amber Theme for Dashboard
					ColoredNavItem(
						icon = Features.Cuadriculas().icono,
						backgroundColor = Color(0xFFFFF8E1), // Light Amber
						iconColor = Color(0xFFD84315), // Deep Orange/Brownish
						onClick = { navegacion(EventosNavegacion.CuadriculaDashboard) },
						onLongClick = {
							haptic.performHapticFeedback(HapticFeedbackType.LongPress)
							navegacion(EventosNavegacion.HomeApp)
						}
					)

					// Dynamic Dashboard Shortcuts
					accesosDirectos.forEach { ds ->
						ColoredNavItem(
							icon = Features.Dashboard().icono,
							backgroundColor = Color(ds.color).copy(alpha = 0.1f),
							iconColor = Color(ds.color),
							onClick = {
								navegacion(
									EventosNavegacion.VisualizadorDashboard(
										ds.id,
										_toJson(ds.parametros)
									)
								)
							}
						)
					}

					if (App.sharedPrerfences.get<Boolean>(Preferencias.ACCESO_HERRAMIENTAS, false)) {
						// Red/Pink Theme for Tools
						ColoredNavItem(
							icon = Features.Herramientas().icono,
							backgroundColor = Color(0xFFFFEBEE), // Light Red
							iconColor = Color(0xFFC62828), // Dark Red
							onClick = { navegacion(EventosNavegacion.MenuHerramientas) }
						)
					}


				}


			}

				}

		}) { paddingValues ->

		Box(Modifier.padding(paddingValues)) {
			contenido()
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ColoredNavItem(
	icon: ImageVector,
	backgroundColor: Color,
	iconColor: Color,
	onClick: () -> Unit,
	onLongClick: (() -> Unit)? = null
) {
	Box(
		modifier = Modifier
			.padding(4.dp)

			.fillMaxHeight()

		.size(80.dp)
			.clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
			.background(backgroundColor)
			.combinedClickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null, 
				onClick = onClick,
				onLongClick = onLongClick
			),
		contentAlignment = Alignment.Center
	) {
		MA_Icono(
			icono = icon,
			color = iconColor,
			modifier = Modifier.size(28.dp)
		)
	}
}
