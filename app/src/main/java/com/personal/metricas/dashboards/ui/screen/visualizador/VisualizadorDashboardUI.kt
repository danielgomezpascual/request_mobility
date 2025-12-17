package com.personal.metricas.dashboards.ui.screen.visualizador

import MA_IconBottom
import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.metricas.App
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.labels.MA_LabelEtiqueta
import com.personal.metricas.core.composables.labels.MA_LabelExtendido
import com.personal.metricas.core.composables.labels.MA_LabelLeyenda
import com.personal.metricas.core.composables.labels.MA_LabelMini
import com.personal.metricas.core.composables.labels.MA_Titulo
import com.personal.metricas.core.composables.labels.MA_LabelNormal
import com.personal.metricas.core.composables.labels.MA_Titulo2
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import com.personal.metricas.core.composables.scaffold.MA_ScaffoldGenerico
import com.personal.metricas.notas.domain.NotasManager
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.screen.ErrorScreen
import com.personal.metricas.core.screen.LoadingScreen
import com.personal.metricas.core.utils.K
import com.personal.metricas.core.utils.if3
import com.personal.metricas.dashboards.ui.screen.visualizador.VisualizadorDashboardVM.UIState
import com.personal.metricas.menu.Features
import com.personal.metricas.paneles.domain.entidades.PanelData
import com.personal.metricas.paneles.ui.componente.MA_Panel
import com.personal.metricas.paneles.ui.entidades.PanelUI
import org.koin.androidx.compose.koinViewModel

@Composable
fun VisualizadorDashboardUI(
	identificador: Int,
	paramtrosJSON: String,
	viewModel: VisualizadorDashboardVM = koinViewModel(),
	navegacion: (EventosNavegacion) -> Unit,
) {

	LaunchedEffect(Unit) {
		viewModel.onEvent(VisualizadorDashboardVM.Eventos.Carga(identificador, paramtrosJSON))
	}


	val uiState by viewModel.uiState.collectAsState()
	when (uiState) {
		is UIState.Error   -> ErrorScreen((uiState as UIState.Error).message)
		UIState.Loading    -> LoadingScreen()
		is UIState.Success -> Success(viewModel, (uiState as UIState.Success), navegacion)
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Success(
	viewModel: VisualizadorDashboardVM,
	uiState: UIState.Success,
	navegacion: (EventosNavegacion) -> Unit,
) {


	MA_ScaffoldGenerico(
		tituloScreen = TituloScreen.DashboardLista,
		navegacion = navegacion,
		accionesSuperiores = {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.End,
				verticalAlignment = Alignment.Top
			) {
				MA_IconBottom(icon = Features.Nuevo().icono, color = Features.Nuevo().color) { navegacion(EventosNavegacion.NuevoDashboard) }
			}
		},
		contenido = {
			VisualizarDashboard(uiState)
		}
	)


}

@Composable
fun VisualizarDashboard(uiState: UIState.Success) {
	// Extraemos la clase de ancho
	val widthSizeClass = App.windowSizeClass.widthSizeClass

	val columns = when (widthSizeClass) {
		WindowWidthSizeClass.Compact -> 1
		WindowWidthSizeClass.Medium -> 2
		WindowWidthSizeClass.Expanded -> 3
		else -> 1
	}
	
	App.numColumnas = columns

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
	) {


		Column(
			modifier = Modifier
				.fillMaxWidth()
				.background(color = Color(255, 253, 231, 255)) // Original background
				.padding(horizontal = 12.dp, vertical = 2.dp) // Improved padding
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically, 
				modifier = Modifier.fillMaxWidth()
			) {
				MA_Titulo2(
					valor = uiState.dashboardUI.nombre,
					modifier = Modifier.weight(1f)
				)

				MA_LabelMini(
					alineacion = TextAlign.End,
					valor = "Sync: ${App.sharedPrerfences.get(K.ULTIMA_SINCRONIZACION, "Sin datos")}",
					size = 10.sp
				)
			}
			
			/*if (uiState.dashboardUI.descripcion.isNotEmpty()) {
				Spacer(modifier = Modifier.height(4.dp))
				MA_LabelNormal(
					valor = uiState.dashboardUI.descripcion,
					modifier = Modifier.fillMaxWidth()
				)
			}*/
		}


		// Grid unificado
		LazyVerticalGrid(
			columns = GridCells.Fixed(columns),
			contentPadding = PaddingValues(16.dp),
			verticalArrangement = Arrangement.spacedBy(16.dp),
			horizontalArrangement = Arrangement.spacedBy(16.dp),
			modifier = Modifier.weight(1f)
		) {
			val paneles = uiState.paneles.filter { it.seleccionado }
			
			items(
				items = paneles,
				span = { panelUI ->
					val spanCount = if3(panelUI.configuracion.celdasPantallasGrandes > columns, columns, panelUI.configuracion.celdasPantallasGrandes)
					GridItemSpan(spanCount)
				}
			) { panelUI ->
				val notasManager = NotasManager.instancia()
				val panelData = PanelData.fromPanelUI(
					panelUI,
					notasManager,
					uiState.dashboardUI.parametros
				)
				MA_Panel(panelData = panelData)
			}
		}
	}
}


