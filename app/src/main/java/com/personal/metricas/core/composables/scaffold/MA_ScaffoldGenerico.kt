package com.personal.metricas.core.composables.scaffold

import MA_IconBottom
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.personal.metricas.App
import com.personal.metricas.core.composables.componentes.Cabecera
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.imagenes.MA_Icono
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.utils.Preferencias
import com.personal.metricas.menu.Features


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MA_ScaffoldGenerico(
	tituloScreen: TituloScreen,
	navegacion: (EventosNavegacion) -> Unit,
	accionesSuperiores: @Composable () -> Unit,
	contenido: @Composable () -> Unit,

	) {


	Scaffold(
		containerColor = Color(red = 227, green = 225, blue = 225, alpha = 100),
		topBar = {

			Box(modifier = Modifier
				.padding(vertical = 6.dp)
			) {
				Column {
					Cabecera(tituloScreen, navegacion, accionesSuperiores)

				}
			}


		}, bottomBar = {

			BottomAppBar() {
				Row(

					modifier = Modifier.fillMaxWidth().height(40.dp),
					horizontalArrangement = Arrangement.SpaceEvenly,
					verticalAlignment = Alignment.Bottom


				) {
					if (App.sharedPrerfences.get<Boolean>(Preferencias.ACCESO_SINCRONIZACION, true)) {
						MA_IconBottom(
							//   modifier = Modifier.weight(1f),
							icon = Features.Sincronizar().icono,
							labelText ="",
							seleccionado = false,
							destacado = false,
							onClick = { navegacion(EventosNavegacion.Sincronizacion) }
						)
					}

					/*
										MA_IconBottom(
											//   modifier = Modifier.weight(1f),
											icon = Features.EndPoints().icono,
											labelText = Features.EndPoints().texto,
											seleccionado = false,
											destacado = false,
											onClick = { navegacion(EventosNavegacion.MenuEndPoints) }
										)
					*/


					val haptic = LocalHapticFeedback.current

					MA_Icono(
						   modifier = Modifier.size(36.dp).combinedClickable(

							   interactionSource = remember { MutableInteractionSource() },
							   indication = null, // Opcional: quita el efecto ripple para gestionarlo tú
							   onClick = {
								   navegacion(EventosNavegacion.CuadriculaDashboard)
							   },
							   onLongClick = {
								   // Realizamos una vibración para notificar al usuario del long press
								   haptic.performHapticFeedback(HapticFeedbackType.LongPress)
								   navegacion(EventosNavegacion.HomeApp)
							   }
						   ),

						icono = Features.Cuadriculas().icono,

						//labelText = Features.Cuadriculas().texto,
						/*seleccionado = true,
						destacado = false,*/
						/*onClick = {  }*/
					)

					if (App.sharedPrerfences.get<Boolean>(Preferencias.ACCESO_HERRAMIENTAS, false)) {
						MA_IconBottom(
							//   modifier = Modifier.weight(1f),
							icon = Features.Herramientas().icono,
							labelText ="",
							seleccionado = false,
							destacado = false,
							onClick = { navegacion(EventosNavegacion.MenuHerramientas) }
						)
					}


				}


			}
		}) { paddingValues ->

		Box(Modifier.padding(paddingValues)) {
			contenido()
		}
	}
}