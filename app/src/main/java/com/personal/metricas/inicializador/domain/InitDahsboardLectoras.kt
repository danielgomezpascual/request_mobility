package com.personal.metricas.inicializador.domain

import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.dashboards.ui.entidades.DashboardUI
import com.personal.metricas.dashboards.ui.entidades.Etiquetas
import com.personal.metricas.inicializador.domain.comunes.KpisComunes
import com.personal.metricas.inicializador.domain.sqls.SQL
import com.personal.metricas.kpi.ui.entidades.KpiUI
import com.personal.metricas.paneles.domain.entidades.EsquemaColores
import com.personal.metricas.paneles.domain.entidades.Panel
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel
import com.personal.metricas.paneles.ui.entidades.PanelUI

class InitDahsboardLectoras(
	private val operaciones: InicializadorOperaciones,
	private val comunes: KpisComunes,
) {
	suspend fun dashboardLectoar(): DashboardUI {


		var panel: Panel = Panel()
		/*	if (endPointUI.id > 0) {
				panel = obtenerPanelCU.obtenerPorEndPoint(endPointUI.id)
			}*/
		val _kpiOrganizacionesTrx = KpiUI(
			titulo = "Oraganizacion TRX",
			descripcion = "Oraganizacion TRX",
			origen = "",
			sql = SQL.ORGANIZACIONES_TRANSACCIONES,
			dinamico = false,
			parametros = Parametros())
		val kpiOrganizacionesTrx = operaciones.guardarKpi(_kpiOrganizacionesTrx)




		return  operaciones.guardarDashboard(nombre = "#LECTORA_FISICA_ID (#ORGANIZATION_CODE)",
									 listOf<PanelUI>(


										 comunes.obtenerPanelTransaccionesEstado(filtroLectora = true),
										 comunes.obtenerPanelTransaccionesOK(filtroLectora = true),
										 comunes.obtenerPanelTransaccionesError(filtroLectora = true),

										 comunes.obtenerPanelTransaccionesErrores(filtroLectora = true),
										 comunes.obtenerPanelTransaccionesDiarias(filtroLectora = true),
										 comunes.obtenerPanelTransaccionesHistorico(filtroLectora = true),
										 comunes.obtenerPanelConteoTransacciones(filtroLectora = true),
										 comunes.obtenerPanelTransaccionesPorHoras(filtroLectora = true),
										 comunes.obtenerPanelVersionesTransacciones(filtroLectora = true)

									 ),

									 kpiOrigen = comunes.crearKpiLectoras(),
									 etiqueta = Etiquetas.EtiquetaValor("PDA"),
									 color = -2354116

		)
	}

	suspend fun obtenerTransaccionesPorOrganiacion(filtroOrganizacion: Boolean = false, filtroLectora: Boolean = false): PanelUI {
		val _kpi = KpiUI(
			titulo = "Transacciones por organizacion",
			descripcion = "Transacciones relizadas por organizacion",
			origen = "",
			sql = SQL.CONTEO_TRANSACCIONES_POR_ORGANIZACION,
			dinamico = true,
			parametros = Parametros()
		)

		val kpiGenerado = operaciones.guardarKpi(_kpi)


		val panel = operaciones.crearPanel(kpiGenerado,
										   false,
										   PlantillasPanel.Companion.from(PlantillasPanel.TT.SoloTabla.valor).configuracion.copy(
											   colores = EsquemaColores().get(EsquemaColores.Companion.MUTICOLOR).id,
											   limiteElementos = 0,
											   indicadorColor = false,
											   ajustarContenidoAncho = true,
											   filtroOrganizacion = filtroOrganizacion,
											   filtroLectora = filtroLectora


										   ))
		return panel


	}

}