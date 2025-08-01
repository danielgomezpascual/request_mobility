package com.personal.metricas.paneles.domain.entidades

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class PanelConfiguracion(
	val orientacion: PanelOrientacion = PanelOrientacion.VERTICAL,
	val plantilla: Int = 0,
	val tipo: PanelTipoGrafica = PanelTipoGrafica.BarrasAnchasVerticales(),
	val titulo: String = "",
	val descripcion: String = "",
	val limiteElementos: Int = 0,
	val mostrarEtiquetas: Boolean = true,
	val agruparResto: Boolean = true,
	val target: Float = 0f,
	val ordenado: Boolean = false,
	val espacioGrafica: String = "40",
	val espacioTabla: String = "60",
	val ocuparTodoEspacio: Boolean = true,
	val width: String = "500",
	val height:  String = "600",
	val mostrarGrafica: Boolean = true,
	val mostrarTabla: Boolean = true,
	val mostrarTituloTabla: Boolean = true,
	val agruparValores: Boolean = true,
	val columnaX: Int = 0,
	val columnaY: Int = 1,
	val colores: Int = EsquemaColores.Paletas.NORMAL.valor,
	var ajustarContenidoAncho: Boolean = false,
	var indicadorColor: Boolean = true,
	var filasColor: Boolean = true,
	val condiciones: List<Condiciones> = listOf<Condiciones>(),
	val condicionesCeldas: List<Condiciones> = listOf<Condiciones>(),
	val permiteNotas: Boolean = true
							 )