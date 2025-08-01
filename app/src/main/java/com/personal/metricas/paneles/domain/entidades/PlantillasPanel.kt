package com.personal.metricas.paneles.domain.entidades

import com.personal.metricas.R

sealed class PlantillasPanel(val id: Int, val nombre: String, val icono: Int, val configuracion: PanelConfiguracion) {

	data object SinSeleccionar : PlantillasPanel(0, "Sin seleccionar", R.drawable.logo,
												 PanelConfiguracion())


	data object IndicadorVertical : PlantillasPanel(1, "INDICADOR VERTICAL", R.drawable.indicadorv,
													PanelConfiguracion(orientacion = PanelOrientacion.VERTICAL,
																	   tipo = PanelTipoGrafica.IndicadorVertical(),
																	   plantilla = 1,
																	   limiteElementos = 1,
																	   mostrarEtiquetas = false,
																	   agruparResto = false,
																	   target = 0f,
																	   ordenado = true,
																	   espacioGrafica = "100",
																	   espacioTabla = "0",
																	   ocuparTodoEspacio = true,
																	   width = "500",
																	   height = "250",
																	   mostrarGrafica = true,
																	   mostrarTabla = false,
																	   mostrarTituloTabla = true,
																	   agruparValores = false,
																	   columnaX = 0,
																	   columnaY = 1,
																	   colores = EsquemaColores.Paletas.NORMAL.valor,
																	   ajustarContenidoAncho = false,
																	   indicadorColor = false,
																	   filasColor = false,
																	   condiciones = emptyList(),
																	   condicionesCeldas = emptyList(),
																	   permiteNotas = false)

	)


	data object IndicadorHorizontal : PlantillasPanel(2, "INDICADOR HORIZONTAL", R.drawable.indicadorh,
													  PanelConfiguracion(orientacion = PanelOrientacion.VERTICAL,
																		 tipo = PanelTipoGrafica.IndicadorHorizontal(),
																		 plantilla = 2,
																		 limiteElementos = 1,
																		 mostrarEtiquetas = false,
																		 agruparResto = false,
																		 target = 0f,
																		 ordenado = true,
																		 espacioGrafica = "100",
																		 espacioTabla = "0",
																		 ocuparTodoEspacio = true,
																		 width = "500",
																		 height = "250",
																		 mostrarGrafica = true,
																		 mostrarTabla = false,
																		 mostrarTituloTabla = true,
																		 agruparValores = false,
																		 columnaX = 0,
																		 columnaY = 1,
																		 colores = EsquemaColores.Paletas.FOSFORITOS.valor,
																		 ajustarContenidoAncho = false,
																		 indicadorColor = false,
																		 filasColor = false,
																		 condiciones = emptyList(),
																		 condicionesCeldas = emptyList(),
																		 permiteNotas = false))

	data object SoloTabla : PlantillasPanel(3, "SOLO TABLA", R.drawable.tabla,
											PanelConfiguracion(orientacion = PanelOrientacion.VERTICAL,
															   //tipo = PanelTipoGrafica.IndicadorHorizontal(),
															   plantilla = 3,
															   limiteElementos = 0,
															   mostrarEtiquetas = true,
															   agruparResto = false,
															   target = 0f,
															   ordenado = true,
															   espacioGrafica = "0",
															   espacioTabla = "100",
															   ocuparTodoEspacio = true,
															   width = "500",
															   height = "500",
															   mostrarGrafica = false,
															   mostrarTabla = true,
															   mostrarTituloTabla = true,
															   agruparValores = false,
															   columnaX = 0,
															   columnaY = 1,
															   colores = EsquemaColores.Paletas.PERS.valor,
															   ajustarContenidoAncho = false,
															   indicadorColor = true,
															   filasColor = true,
															   condiciones = emptyList(),
															   condicionesCeldas = emptyList(),
															   permiteNotas = true))


	data object BarrasFinasVertivales : PlantillasPanel(4, "Barras Finas Verticales + Tabla", R.drawable.lineasfinas,
														PanelConfiguracion(orientacion = PanelOrientacion.VERTICAL,
																		   tipo = PanelTipoGrafica.BarrasFinasVerticales(),
																		   plantilla = 4,
																		   limiteElementos = 10,
																		   mostrarEtiquetas = true,
																		   agruparResto = false,
																		   target = 0f,
																		   ordenado = true,
																		   espacioGrafica = "60",
																		   espacioTabla = "40",
																		   ocuparTodoEspacio = true,
																		   width = "500",
																		   height = "500",
																		   mostrarGrafica = true,
																		   mostrarTabla = true,
																		   mostrarTituloTabla = true,
																		   agruparValores = false,
																		   columnaX = 0,
																		   columnaY = 1,
																		   colores = EsquemaColores.Paletas.NORMAL.valor,
																		   ajustarContenidoAncho = false,
																		   indicadorColor = true,
																		   filasColor = true,
																		   condiciones = emptyList(),
																		   condicionesCeldas = emptyList(),
																		   permiteNotas = true))


	data object BarrasAnchasVertivales : PlantillasPanel(5, "Barras Anchas Verticales + Tabla", R.drawable.linasanchas,
														 PanelConfiguracion(orientacion = PanelOrientacion.VERTICAL,
																			tipo = PanelTipoGrafica.BarrasAnchasVerticales(),
																			plantilla = 5,
																			limiteElementos = 10,
																			mostrarEtiquetas = true,
																			agruparResto = false,
																			target = 0f,
																			ordenado = true,
																			espacioGrafica = "60",
																			espacioTabla = "40",
																			ocuparTodoEspacio = true,
																			width = "500",
																			height = "500",
																			mostrarGrafica = true,
																			mostrarTabla = true,
																			mostrarTituloTabla = true,
																			agruparValores = false,
																			columnaX = 0,
																			columnaY = 1,
																			colores = EsquemaColores.Paletas.NORMAL.valor,
																			ajustarContenidoAncho = false,
																			indicadorColor = true,
																			filasColor = true,
																			condiciones = emptyList(),
																			condicionesCeldas = emptyList(),
																			permiteNotas = true))


	data object Circular : PlantillasPanel(6, "Circular + Tabla", R.drawable.circular,
										   PanelConfiguracion(orientacion = PanelOrientacion.VERTICAL,
															  tipo = PanelTipoGrafica.Circular(),
															  plantilla = 6,
															  limiteElementos = 8,
															  mostrarEtiquetas = true,
															  agruparResto = false,
															  target = 0f,
															  ordenado = true,
															  espacioGrafica = "60",
															  espacioTabla = "40",
															  ocuparTodoEspacio = true,
															  width = "500",
															  height = "500",
															  mostrarGrafica = true,
															  mostrarTabla = true,
															  mostrarTituloTabla = true,
															  agruparValores = false,
															  columnaX = 0,
															  columnaY = 1,
															  colores = EsquemaColores.Paletas.NORMAL.valor,
															  ajustarContenidoAncho = false,
															  indicadorColor = true,
															  filasColor = true,
															  condiciones = emptyList(),
															  condicionesCeldas = emptyList(),
															  permiteNotas = true))


	data object Anillo : PlantillasPanel(7, "Anillo + Tabla", R.drawable.anillo,
										 PanelConfiguracion(orientacion = PanelOrientacion.VERTICAL,
															tipo = PanelTipoGrafica.Anillo(),
															plantilla = 7,
															limiteElementos = 8,
															mostrarEtiquetas = true,
															agruparResto = false,
															target = 0f,
															ordenado = true,
															espacioGrafica = "60",
															espacioTabla = "40",
															ocuparTodoEspacio = true,
															width = "500",
															height = "500",
															mostrarGrafica = true,
															mostrarTabla = true,
															mostrarTituloTabla = true,
															agruparValores = false,
															columnaX = 0,
															columnaY = 1,
															colores = EsquemaColores.Paletas.NORMAL.valor,
															ajustarContenidoAncho = false,
															indicadorColor = true,
															filasColor = true,
															condiciones = emptyList(),
															condicionesCeldas = emptyList(),
															permiteNotas = true))

	data object Lineas : PlantillasPanel(8, "Grafica Lineal  + Tabla", R.drawable.lineas,
										 PanelConfiguracion(orientacion = PanelOrientacion.VERTICAL,
															tipo = PanelTipoGrafica.Lineas(),
															plantilla = 8,
															limiteElementos = 8,
															mostrarEtiquetas = true,
															agruparResto = false,
															target = 0f,
															ordenado = true,
															espacioGrafica = "60",
															espacioTabla = "40",
															ocuparTodoEspacio = true,
															width = "500",
															height = "500",
															mostrarGrafica = true,
															mostrarTabla = true,
															mostrarTituloTabla = true,
															agruparValores = false,
															columnaX = 0,
															columnaY = 1,
															colores = EsquemaColores.Paletas.NORMAL.valor,
															ajustarContenidoAncho = true,
															indicadorColor = true,
															filasColor = true,
															condiciones = emptyList(),
															condicionesCeldas = emptyList(),
															permiteNotas = true))

	data object PanelesVerticales : PlantillasPanel(9, "Paneles Verticales", R.drawable.indicadorv,
										 PanelConfiguracion(orientacion = PanelOrientacion.VERTICAL,
															tipo = PanelTipoGrafica.IndicadorVertical(),
															plantilla = 9,
															limiteElementos = 0,
															mostrarEtiquetas = true,
															agruparResto = false,
															target = 0f,
															ordenado = true,
															espacioGrafica = "60",
															espacioTabla = "40",
															ocuparTodoEspacio = true,
															width = "500",
															height = "500",
															mostrarGrafica = true,
															mostrarTabla = false,
															mostrarTituloTabla = true,
															agruparValores = false,
															columnaX = 0,
															columnaY = 1,
															colores = EsquemaColores.Paletas.FOSFORITOS.valor,
															ajustarContenidoAncho = false,
															indicadorColor = true,
															filasColor = true,
															condiciones = emptyList(),
															condicionesCeldas = emptyList(),
															permiteNotas = false))

	data object PanelesHorizontales : PlantillasPanel(10, "Paneles Horizontales", R.drawable.indicadorh,
													PanelConfiguracion(orientacion = PanelOrientacion.VERTICAL,
																	   tipo = PanelTipoGrafica.IndicadorHorizontal(),
																	   plantilla = 10,
																	   limiteElementos = 0,
																	   mostrarEtiquetas = true,
																	   agruparResto = false,
																	   target = 0f,
																	   ordenado = true,
																	   espacioGrafica = "60",
																	   espacioTabla = "40",
																	   ocuparTodoEspacio = true,
																	   width = "500",
																	   height = "500",
																	   mostrarGrafica = true,
																	   mostrarTabla = false,
																	   mostrarTituloTabla = true,
																	   agruparValores = false,
																	   columnaX = 0,
																	   columnaY = 1,
																	   colores = EsquemaColores.Paletas.FOSFORITOS.valor,
																	   ajustarContenidoAncho = false,
																	   indicadorColor = true,
																	   filasColor = true,
																	   condiciones = emptyList(),
																	   condicionesCeldas = emptyList(),
																	   permiteNotas = false))



	companion object {
		


		fun dameTipos(): List<PlantillasPanel> = listOf<PlantillasPanel>(
			SinSeleccionar,
			IndicadorVertical,
			IndicadorHorizontal,
			SoloTabla,
			BarrasFinasVertivales,
			Circular,
			Anillo,
			Lineas,
			PanelesHorizontales,
			PanelesVerticales,
			/*IndicadorHorizontal(),
			BarrasAnchasVerticales(),
			BarrasFinasVerticales(),
			Circular(),
			Anillo(),
			Lineas())*/
		)

		fun from(tipo: Int) = when (tipo) {
			0    -> SinSeleccionar
			1    -> IndicadorVertical
			2    -> IndicadorHorizontal
			3    -> SoloTabla
			4    -> BarrasFinasVertivales
			5    -> BarrasAnchasVertivales
			6    -> Circular
			7    -> Anillo
			8    -> Lineas
			9    -> PanelesHorizontales
			10    -> PanelesVerticales
			else -> SinSeleccionar
		}

	}

	enum class TT(var valor:Int){
		SinSeleccionar(0),
		IndicadorVertical(1),
		IndicadorHorizontal(2),
		SoloTabla(3),
		BarrasFinasVertivales(4),
		BarrasAnchasVertivales(5),
		Circular(6),
		Anillo(7),
		Lineas(8),
		PanelesHorizontales(9),
		PanelesVerticales(10),
	}

}




