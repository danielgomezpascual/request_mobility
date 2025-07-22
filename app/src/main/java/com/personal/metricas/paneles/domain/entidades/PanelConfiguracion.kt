package com.personal.metricas.paneles.domain.entidades

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class PanelConfiguracion(
	val orientacion: PanelOrientacion = PanelOrientacion.VERTICAL,
	val tipo: PanelTipoGrafica = PanelTipoGrafica.BarrasAnchasVerticales(),
	val titulo: String = "",
	val descripcion: String = "",
	val limiteElementos: Int = 0,
	val mostrarEtiquetas: Boolean = true,
	val agruparResto: Boolean = true,
	val target: Float = 0f,
	val ordenado: Boolean = false,
	val espacioGrafica: Float = 0.4f,
	val espacioTabla: Float = 0.6f,
	val ocuparTodoEspacio: Boolean = true,
	val width: Dp = 500.dp,
	val height: Dp = 600.dp,
	val mostrarGrafica: Boolean = true,
	val mostrarTabla: Boolean = true,
	val mostrarTituloTabla: Boolean = true,
	val agruparValores: Boolean = true,
	val columnaX: Int = 0,
	val columnaY: Int = 1,
	val colores: Int = 0,
	var ajustarContenidoAncho: Boolean = true,
	var indicadorColor: Boolean = true,
	var filasColor: Boolean = true,
	val condiciones: List<Condiciones> = listOf<Condiciones>(),
	val condicionesCeldas: List<Condiciones> = listOf<Condiciones>(),
							 )