package com.personal.metricas.inicializador.domain.comunes

import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.inicializador.domain.CONDICIONES_PANELES
import com.personal.metricas.inicializador.domain.InicializadorOperaciones
import com.personal.metricas.inicializador.domain.sqls.SQL
import com.personal.metricas.kpi.ui.entidades.KpiUI
import com.personal.metricas.paneles.domain.entidades.Condiciones
import com.personal.metricas.paneles.domain.entidades.EsquemaColores
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel
import com.personal.metricas.paneles.ui.entidades.PanelUI

class KpisComunes(private val operaciones: InicializadorOperaciones) {


	var kpiTransaccionesDiarias: KpiUI = KpiUI()
	var kpiTransaccionesHistoricos: KpiUI = KpiUI()
	var kpiRatioOkError: KpiUI = KpiUI()


	suspend fun crearKpiOrganizaciones(): KpiUI {
		val kpiOrganizaciones = (KpiUI(
			titulo = "Organizaciones",
			descripcion = "Organizaciones en el sistema",
			origen = "",
			sql = SQL.ORGANIZACIONES_TRANSACCIONES,
			dinamico = false,
			parametros = Parametros()))
		val k = operaciones.guardarKpi(kpiOrganizaciones)
		return k
	}


	suspend fun crearKpiLectoras(): KpiUI {
		val kpiOrganizaciones = (KpiUI(
			titulo = "Lectoras",
			descripcion = "Lectoras del sistema",
			origen = "",
			sql = SQL.LECTORAS_TRANSACCIONES,
			dinamico = false,
			parametros = Parametros()))
		val k = operaciones.guardarKpi(kpiOrganizaciones)
		return k
	}


	suspend fun obtenerPanelTransaccionesHistorico(filtroOrganizacion: Boolean = false, filtroLectora: Boolean = false): PanelUI {
		var _kpiTransaccionesHistoricos = KpiUI(
			titulo = "Histórico",
			descripcion = "Histórico de transacciones",
			origen = "",
			sql = SQL.INFO_TRANSACCIONES_HISTORICO,
			dinamico = true,
			parametros = Parametros()
		)
		kpiTransaccionesHistoricos = operaciones.guardarKpi(_kpiTransaccionesHistoricos)

		val panelTransaccionesHistorico = operaciones.crearPanel(kpiTransaccionesHistoricos,
																 false,
																 PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor).configuracion.copy(
																	 indicadorColor = true,
																	 condicionesCeldas = CONDICIONES_PANELES.listaCondicionesBanderas,
																	 condiciones = CONDICIONES_PANELES.listaCondicionesErr,
																	 ajustarContenidoAncho = false,
																	 filtroOrganizacion = filtroOrganizacion,
																	 filtroLectora = filtroLectora

																 ))

		return panelTransaccionesHistorico

	}

	suspend fun obtenerPanelTransaccionesErrores(filtroOrganizacion: Boolean = false, filtroLectora: Boolean = false): PanelUI {
		val _kpiRatioOkError = KpiUI(
			titulo = "Ratios",
			descripcion = "Ratios de errores sobre las transacciones corectas",
			origen = "",
			sql = SQL.RATIO_OK_ERROR,
			dinamico = true,
			parametros = Parametros())
		kpiTransaccionesDiarias = operaciones.guardarKpi(_kpiRatioOkError)


		val panelErroresRatio = operaciones.crearPanel(
			kpiTransaccionesDiarias,
			false,
			PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor)
				.configuracion.copy(
					colores = 7,
					indicadorColor = false,
					condiciones = listOf<Condiciones>(CONDICIONES_PANELES.COND_RATIO_ERRORES),
					ajustarContenidoAncho = false,
					width = "500", height = "600",
					filtroOrganizacion = filtroOrganizacion,
					filtroLectora = filtroLectora
				))
		return panelErroresRatio
	}

	suspend fun obtenerPanelTransaccionesDiarias(filtroOrganizacion: Boolean = false, filtroLectora: Boolean = false): PanelUI {
		val _kpiTransaccionesDiarias = KpiUI(
			titulo = "Transacciones",
			descripcion = "Transacciones realizadas hoy",
			origen = "",
			sql = SQL.INFO_TRANSACCIONES_HOY,
			dinamico = true,
			parametros = Parametros()
		)

		kpiTransaccionesDiarias = operaciones.guardarKpi(_kpiTransaccionesDiarias)


		val panelTransaccionesDiarias = operaciones.crearPanel(kpiTransaccionesDiarias,
															   false,
															   PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor).configuracion.copy(
																   indicadorColor = false,
																   condicionesCeldas = CONDICIONES_PANELES.listaCondicionesBanderas,
																   condiciones = CONDICIONES_PANELES.listaCondicionesErr,
																   ajustarContenidoAncho = false,
																   filtroOrganizacion = filtroOrganizacion,
																   filtroLectora = filtroLectora


															   ))
		return panelTransaccionesDiarias


	}


	suspend fun obtenerPanelConteoTransacciones(filtroOrganizacion: Boolean = false, filtroLectora: Boolean = false): PanelUI {
		val _kpiConteoTransacciones = KpiUI(
			titulo = "Transacciones",
			descripcion = "Conteo de Transacciones diario",
			origen = "",
			sql = SQL.CONTEO_TRANSACCIONES,
			dinamico = true,
			parametros = Parametros()
		)

		val kpiConteoTransacciones = operaciones.guardarKpi(_kpiConteoTransacciones)


		val panelConteoTransaccionesDiaria = operaciones.crearPanel(kpiConteoTransacciones,
																	false,
																	PlantillasPanel.from(PlantillasPanel.TT.Lineas.valor).configuracion.copy(
																		indicadorColor = false,
																		condicionesCeldas = CONDICIONES_PANELES.listaCondicionesBanderas,
																		condiciones = CONDICIONES_PANELES.listaCondicionesErr,
																		ajustarContenidoAncho = true,
																		filtroOrganizacion = filtroOrganizacion,
																		filtroLectora = filtroLectora


																	))
		return panelConteoTransaccionesDiaria


	}


	suspend fun obtenerPanelTransaccionesPorHoras(filtroOrganizacion: Boolean = false, filtroLectora: Boolean = false): PanelUI {
		val _kpiConteoTransacciones = KpiUI(
			titulo = "Transacciones",
			descripcion = "Conteo de Transacciones diario",
			origen = "",
			sql = SQL.TRANSACCIONES_POR_HORAS,
			dinamico = true,
			parametros = Parametros()
		)

		val kpiConteoTransacciones = operaciones.guardarKpi(_kpiConteoTransacciones)


		val panelConteoTransaccionesDiaria = operaciones.crearPanel(kpiConteoTransacciones,
																	false,
																	PlantillasPanel.from(PlantillasPanel.TT.BarrasFinasVertivales.valor).configuracion.copy(
																		limiteElementos = 0,
																		indicadorColor = false,
																		condicionesCeldas = CONDICIONES_PANELES.listaCondicionesBanderas,
																		condiciones = CONDICIONES_PANELES.listaCondicionesErr,
																		ajustarContenidoAncho = true,
																		filtroOrganizacion = filtroOrganizacion,
																		filtroLectora = filtroLectora


																	))
		return panelConteoTransaccionesDiaria


	}




	suspend fun obtenerPanelTransaccionesOK(filtroOrganizacion: Boolean = false, filtroLectora: Boolean = false): PanelUI {
		val _kpiConteoTransacciones = KpiUI(
			titulo = "Tipo Transacciones OK",
			descripcion = "Transacciones procesadas correctamente",
			origen = "",
			sql = SQL.TIPOS_TRANSACCIONES_OK,
			dinamico = true,
			parametros = Parametros()
		)

		val kpiConteoTransacciones = operaciones.guardarKpi(_kpiConteoTransacciones)


		val panelConteoTransaccionesDiaria = operaciones.crearPanel(kpiConteoTransacciones,
																	false,
																	PlantillasPanel.from(PlantillasPanel.TT.PanelesHorizontales.valor).configuracion.copy(
																		colores = EsquemaColores().get(EsquemaColores.PERS_VERDE).id,
																		limiteElementos = 0,
																		indicadorColor = false,
																		ajustarContenidoAncho = true,
																		filtroOrganizacion = filtroOrganizacion,
																		filtroLectora = filtroLectora


																	))
		return panelConteoTransaccionesDiaria


	}

	suspend fun obtenerPanelTransaccionesError(filtroOrganizacion: Boolean = false, filtroLectora: Boolean = false): PanelUI {
		val _kpiConteoTransacciones = KpiUI(
			titulo = "Tipo Transacciones ERROR",
			descripcion = "Transacciones procesadas ERRONEAMENTE",
			origen = "",
			sql = SQL.TIPOS_TRANSACCIONES_ERROR,
			dinamico = true,
			parametros = Parametros()
		)

		val kpiConteoTransacciones = operaciones.guardarKpi(_kpiConteoTransacciones)


		val panelConteoTransaccionesDiaria = operaciones.crearPanel(kpiConteoTransacciones,
																	false,
																	PlantillasPanel.from(PlantillasPanel.TT.PanelesHorizontales.valor).configuracion.copy(
																		colores = EsquemaColores().get(EsquemaColores.PERS_ROJA).id,
																		limiteElementos = 0,
																		indicadorColor = false,
																		ajustarContenidoAncho = true,
																		filtroOrganizacion = filtroOrganizacion,
																		filtroLectora = filtroLectora


																	))
		return panelConteoTransaccionesDiaria


	}

	suspend fun obtenerPanelTransaccionesEstado(filtroOrganizacion: Boolean = false, filtroLectora: Boolean = false): PanelUI {
		val _kpiConteoTransacciones = KpiUI(
			titulo = "Tipo Transacciones ",
			descripcion = "Transacciones procesadas",
			origen = "",
			sql = SQL.CONTEO_ESTADO_TRNSACCIONES,
			dinamico = true,
			parametros = Parametros()
		)

		val kpiConteoTransacciones = operaciones.guardarKpi(_kpiConteoTransacciones)


		val panelConteoTransaccionesDiaria = operaciones.crearPanel(kpiConteoTransacciones,
																	false,
																	PlantillasPanel.from(PlantillasPanel.TT.PanelesHorizontales.valor).configuracion.copy(
																		colores = EsquemaColores().get(EsquemaColores.PERS_AMARILLA).id,
																		limiteElementos = 0,
																		indicadorColor = false,
																		ajustarContenidoAncho = true,
																		filtroOrganizacion = filtroOrganizacion,
																		filtroLectora = filtroLectora


																	))
		return panelConteoTransaccionesDiaria


	}



	suspend fun obtenerPanelVersionesTransacciones(filtroOrganizacion: Boolean = false, filtroLectora: Boolean = false): PanelUI {
		val _k = KpiUI(
			titulo = "Versiones",
			descripcion = "Versiones utilziadas ",
			origen = "",
			sql = SQL.CONTEO_TRX_VERSION,
			dinamico = true,
			parametros = Parametros()
		)

		val k = operaciones.guardarKpi(_k)

		val p = operaciones.crearPanel(k,
									   false,
									   PlantillasPanel.from(PlantillasPanel.TT.Circular.valor).configuracion.copy(
										   colores = EsquemaColores().get(EsquemaColores.MUTICOLOR).id,
										   limiteElementos = 0,
										   indicadorColor = true,
										   ajustarContenidoAncho = true,
										   filtroOrganizacion = filtroOrganizacion,
										   filtroLectora = filtroLectora


									   ))
		return p
	}
		suspend fun createGenerico(titulo:String,sql: String, plantilla: Int, colores:Int   = EsquemaColores.MUTICOLOR,
								   celdas: Int = 1,
								   filtroOrganizacion: Boolean = false, filtroLectora: Boolean = false): PanelUI {
			val _k = KpiUI(
				titulo = titulo,
				descripcion = "",
				origen = "",
				sql = sql,
				dinamico = true,
				parametros = Parametros()
			)

			val k = operaciones.guardarKpi(_k)

			val p = operaciones.crearPanel(k,
										   false,
										   PlantillasPanel.from(plantilla).configuracion.copy(
											   colores = EsquemaColores().get(colores).id,
											   limiteElementos = 0,
											   celdasPantallasGrandes = celdas,
											   indicadorColor = false,
											   ajustarContenidoAncho = true,
											   filtroOrganizacion = filtroOrganizacion,
											   filtroLectora = filtroLectora

										   ))
			return p

	}





}

