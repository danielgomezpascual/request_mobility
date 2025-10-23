package com.personal.metricas.inicializador.domain

import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.dashboards.ui.entidades.DashboardUI
import com.personal.metricas.dashboards.ui.entidades.Etiquetas
import com.personal.metricas.inicializador.domain.comunes.KpisComunes
import com.personal.metricas.inicializador.domain.sqls.SQL
import com.personal.metricas.kpi.ui.entidades.KpiUI
import com.personal.metricas.kpi.ui.entidades.toKpi
import com.personal.metricas.paneles.domain.entidades.Conector
import com.personal.metricas.paneles.domain.entidades.EsquemaColores
import com.personal.metricas.paneles.domain.entidades.Panel
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel
import com.personal.metricas.paneles.domain.entidades.TIPO_CONECTOR
import com.personal.metricas.paneles.domain.entidades.TiposPanel
import com.personal.metricas.paneles.ui.entidades.PanelUI
import com.personal.metricas.paneles.ui.entidades.fromPanel

class InitDahsboardGeneral(
	private val operaciones: InicializadorOperaciones,
	private val comunes: KpisComunes,
) {
	suspend fun crearGeneral(dashboardOrganizaciones: DashboardUI) {


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



				val panelConector: PanelUI =
					PanelUI().fromPanel(panel.copy(
						kpi = kpiOrganizacionesTrx.toKpi(),
						tipoPanel = TiposPanel.PANEL_CONECTOR,
						titulo = "#ORGANIZATION_CODE #ORGANIZATION_NAME",
						descripcion = "DESCRIP CONECTOR",
						conector = Conector(tipo = TIPO_CONECTOR.CONECTAR_DASHBOARD,
											 identificador = dashboardOrganizaciones.id,
											 descripcion = dashboardOrganizaciones.nombre)
					))





				val pan = operaciones.guardarPanel(panelConector)



		val dh = operaciones.guardarDashboard(nombre = "General",
											  listOf<PanelUI>(
												  pan,

												  comunes.obtenerPanelTransaccionesEstado(),
												  comunes.obtenerPanelTransaccionesOK(),
												  comunes.obtenerPanelTransaccionesError(),

												  comunes.obtenerPanelTransaccionesErrores(),
												  comunes.obtenerPanelTransaccionesDiarias(),
												  comunes.obtenerPanelTransaccionesHistorico(),
												  comunes.obtenerPanelConteoTransacciones(),
												  comunes.obtenerPanelTransaccionesPorHoras(),
												  obtenerTransaccionesPorOrganiacion()


											  ),
											  etiqueta = Etiquetas.EtiquetaValor("General"),
											  home = true,
											  color = -16744448
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
										   PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor).configuracion.copy(
											   colores = EsquemaColores().get(EsquemaColores.MUTICOLOR).id,
											   limiteElementos = 0,
											   indicadorColor = false,
											   ajustarContenidoAncho = true,
											   filtroOrganizacion = filtroOrganizacion,
											   filtroLectora = filtroLectora


										   ))
		return panel


	}

}