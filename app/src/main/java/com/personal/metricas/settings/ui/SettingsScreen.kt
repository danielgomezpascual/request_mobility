package com.personal.metricas.settings.ui


import MA_IconBottom
import MA_Morph
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.firebase.ui.auth.AuthUI
import com.personal.metricas.App
import com.personal.metricas.App.Companion.context
import com.personal.metricas.core.composables.MA_Spacer
import com.personal.metricas.core.composables.card.MA_Card
import com.personal.metricas.core.composables.checks.MA_SwitchNormal
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.labels.MA_Titulo2
import com.personal.metricas.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.screen.ErrorScreen
import com.personal.metricas.core.screen.LoadingScreen
import com.personal.metricas.core.utils.Preferencias
import com.personal.metricas.firebase.domain.FirebaseManager
import com.personal.metricas.menu.Features
import com.personal.metricas.settings.ui.SettingsViewModel.*
import org.koin.androidx.compose.koinViewModel


@Composable
fun SettingsScreen(
	viewModel: SettingsViewModel = koinViewModel(),
	navegacion: (EventosNavegacion) -> Unit,
) {

	val uiState by viewModel.uiState.collectAsState()

	LaunchedEffect(Unit) {
		viewModel.onEvent(Eventos.Cargar)
	}


	when (uiState) {
		is UIState.Error   -> ErrorScreen((uiState as UIState.Error).mensaje)
		UIState.Loading    -> LoadingScreen()
		is UIState.Success -> SucessSettingsScreen(viewModel,
												   uiState as UIState.Success,
												   navegacion)

	}


}

@Composable
fun SucessSettingsScreen(
	viewModel: SettingsViewModel,
	uiState: UIState.Success,
	navegacion: (EventosNavegacion) -> Unit,
) {

	MA_ScaffoldGenerico(
		tituloScreen = TituloScreen.Settings,
		navegacion = navegacion,
		accionesSuperiores = {
		},
		contenido = {
			//val f: SubirContenidoLocalFirebase = getKoin().get()
			//f.uploadFirestore()

			//val f: DescargarContenidoFirestore = getKoin().get()
			//f.descargar()


			val scope = rememberCoroutineScope() // Se mantiene dentro del componente
			var estaTrabajando by remember { mutableStateOf(false) }


			if ((uiState is UIState.Success)) {


				if ((uiState as UIState.Success).trabajando) {
					MA_Morph()
				}


				Column(modifier = Modifier
					.fillMaxSize()
					.verticalScroll(rememberScrollState())) {


					App.log.c("Usuario: ${FirebaseManager().getAuth().currentUser?.isAnonymous}")

					MA_Titulo2(valor = "Identificador Usuario")
					if (FirebaseManager().getAuth().currentUser?.isAnonymous == false) {
						Row() {


							MA_Card(
								modifier = Modifier
									.weight(1f)
									.clickable(
										enabled = true,
										onClick = { viewModel.onEvent(Eventos.SubirFirebase) })
							) {
								Row {
									MA_IconBottom(modifier = Modifier.fillMaxSize(), icon = Features.ImportarFirebase().icono,
												  labelText = Features.ImportarFirebase().texto,
												  color = Features.ImportarFirebase().color) {
									}
								}
							}
							MA_Card(
								modifier = Modifier
									.fillMaxSize()
									.weight(1f)
									.clickable(
										enabled = true,
										onClick = { viewModel.onEvent(Eventos.DescargarFirebase) })
							) {
								Row {
									MA_IconBottom(modifier = Modifier.fillMaxSize(), icon = Features.ExportarFirebase().icono,
												  labelText = Features.ExportarFirebase().texto,
												  color = Features.ExportarFirebase().color) { }


								}

							}
						}
					}
					MA_Card(
						modifier = Modifier
							.fillMaxSize()
							.clickable(
								enabled = true,
								onClick = { })
					) {
						MA_IconBottom(
							modifier = Modifier.fillMaxSize(),
							icon = Features.CerrarSesion().icono,
							labelText = Features.CerrarSesion().texto,
							color = Features.CerrarSesion().color) {
							AuthUI.getInstance()
								.signOut(context)
								.addOnCompleteListener {
									navegacion(EventosNavegacion.HomeApp)
								}
						}
					}





					MA_Titulo2(valor = "Datos almacenados")
					MA_Card {
						Row() {

							MA_Card(modifier = Modifier.weight(1f)) {

								MA_SwitchNormal(titulo = Features.SincronizarAuto().texto,
												valor = uiState.sincronizarAuto,
												icono = Features.SincronizarAuto().icono)
								{ viewModel.onEvent(Eventos.SincronizarAuto(it)) }
							}


							MA_Card(modifier = Modifier.weight(1f)) {
							MA_IconBottom(modifier = Modifier.fillMaxSize(), icon = Features.BorrarDatos().icono,
										  labelText = Features.BorrarDatos().texto,
										  color = Features.BorrarDatos().color) { viewModel.onEvent(Eventos.EliminarDatos) }
								}
						}
					}

					MA_Titulo2(valor = "Accesos")
					MA_Card {
						Column {


							Row(modifier = Modifier.fillMaxWidth()) {
								MA_Card(modifier = Modifier.weight(1f)) {
									MA_SwitchNormal(titulo = Features.AccesosHerramientas().texto,
													valor = uiState.herramientas,
													icono = Features.AccesosHerramientas().icono)
									{ viewModel.onEvent(Eventos.AccesoHerramientas(it)) }
								}
								MA_Spacer()

								MA_Card(modifier = Modifier.weight(1f)) {
									MA_SwitchNormal(titulo = Features.AccesoSettings().texto,
													valor = App.sharedPrerfences.get<Boolean>(Preferencias.ACCESO_AJUSTES, true),
													icono = Features.AccesoSettings().icono) {
										viewModel.onEvent(Eventos.AccesoAjustes(it))
									}
								}

							}
							Row {
								MA_Card(modifier = Modifier.weight(1f)) {
									MA_SwitchNormal(titulo = Features.AccesoSicnronizacion().texto,
													valor = App.sharedPrerfences.get<Boolean>(Preferencias.ACCESO_SINCRONIZACION, true),
													icono = Features.AccesoSicnronizacion().icono) {
										viewModel.onEvent(Eventos.AccesosSincronizacion(it))
									}
								}
								MA_Spacer()
								MA_Card(modifier = Modifier.weight(1f)) {
									MA_SwitchNormal(titulo = Features.Entorno().texto,
													valor = App.sharedPrerfences.get<Boolean>(Preferencias.ENTORNO_PRO, false),
													icono = Features.Entorno().icono) {
										viewModel.onEvent(Eventos.EntornoProduccion(it))
									}
								}
							}
						}
					}


				}
			}
		}
	)

}