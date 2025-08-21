package com.personal.metricas.core.composables.scaffold

import MA_IconBottom
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.personal.metricas.App
import com.personal.metricas.core.composables.componentes.Cabecera
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.utils.Preferencias
import com.personal.metricas.menu.Features


@OptIn(ExperimentalMaterial3Api::class)
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
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.Center,
					verticalAlignment = Alignment.Bottom

				) {
					if (App.sharedPrerfences.get<Boolean>(Preferencias.ACCESO_SINCRONIZACION, true)) {
						MA_IconBottom(
							//   modifier = Modifier.weight(1f),
							icon = Features.Sincronizar().icono,
							labelText = Features.Sincronizar().texto,
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

					MA_IconBottom(
						//   modifier = Modifier.weight(1f),
						icon = Features.Cuadriculas().icono,
						labelText = Features.Cuadriculas().texto,
						seleccionado = true,
						destacado = false,
						onClick = { navegacion(EventosNavegacion.CuadriculaDashboard) }
					)

					if (App.sharedPrerfences.get<Boolean>(Preferencias.ACCESO_HERRAMIENTAS, false)) {
						MA_IconBottom(
							//   modifier = Modifier.weight(1f),
							icon = Features.Herramientas().icono,
							labelText = Features.Herramientas().texto,
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