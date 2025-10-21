package com.personal.metricas.inicializador.domain

import com.personal.metricas.App
import com.personal.metricas.core.data.ds.remote.network.retrofit.request.Entornos
import com.personal.metricas.core.utils.Parametro
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.dashboards.domain.entidades.Dashboard
import com.personal.metricas.dashboards.ui.entidades.DashboardUI
import com.personal.metricas.dashboards.ui.entidades.Etiquetas
import com.personal.metricas.endpoints.ui.entidades.EndPointUI
import com.personal.metricas.inicializador.domain.comunes.KpisComunes
import com.personal.metricas.inicializador.domain.sqls.SQL
import com.personal.metricas.kpi.ui.entidades.KpiUI
import com.personal.metricas.kpi.ui.entidades.toKpi
import com.personal.metricas.paneles.domain.entidades.Conector
import com.personal.metricas.paneles.domain.entidades.EsquemaColores
import com.personal.metricas.paneles.domain.entidades.Panel
import com.personal.metricas.paneles.domain.entidades.PanelConfiguracion
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel
import com.personal.metricas.paneles.domain.entidades.TIPO_CONECTOR
import com.personal.metricas.paneles.domain.entidades.TiposPanel
import com.personal.metricas.paneles.ui.entidades.PanelUI
import com.personal.metricas.paneles.ui.entidades.fromPanel

class InitDashboardOrganizaciones(
	private val operaciones: InicializadorOperaciones,
	private val comunes: KpisComunes,
) {

	suspend fun conectorLectora(ds: DashboardUI): PanelUI{
		var panel: Panel = Panel()
		/*	if (endPointUI.id > 0) {
				panel = obtenerPanelCU.obtenerPorEndPoint(endPointUI.id)
			}*/
		val _kpi = KpiUI(
			titulo = "Lectoras",
			descripcion = "Lectoras",
			origen = "",
			sql = SQL.LECTORAS_TRANSACCIONES,
			dinamico = false,
			parametros = Parametros())
		val kpi = operaciones.guardarKpi(_kpi)



		val panelConector: PanelUI =
			PanelUI().fromPanel(panel.copy(
				configuracion = PanelConfiguracion().copy(filtroOrganizacion = true),
				kpi = kpi.toKpi(),
								tipoPanel = TiposPanel.PANEL_CONECTOR,
								titulo = "#LECTORA_FISICA_ID #ORGANIZATION_CODE",
								descripcion = "DESCRIP CONECTOR",
								conector = Conector(tipo = TIPO_CONECTOR.CONECTAR_DASHBOARD,
									identificador = ds.id,
									descripcion = ds.nombre)
			))





		return operaciones.guardarPanel(panelConector)
	}

	suspend fun generaDashboardOrganizaciones(dashboardLectrora: DashboardUI): DashboardUI {

		//endpoint de recarga de transacciones
		var listaPametrosEP: List<Parametro> = Parametros.dameParametrosPorDefectoMobility()
		listaPametrosEP = listaPametrosEP.plus(Parametro("P_ORGANIZATION_ID", "#ORGANIZATION_ID", "", false))
		val endPoint = EndPointUI(
			nombre = "Recargar datos",
			descripcion = "Obtener Trx",
			url = "${Entornos.get(App.ENTORNO).url}GetSolicitudes",
			parametros = Parametros(listaPametrosEP),
			tabla = "Transacciones",
			nodoIdentificadorFila = "Solicitudes",
			eliminarDatos = false)
		val panelEndPointSolicitudes = operaciones.guardarEndPoint(endPoint)




		return operaciones.guardarDashboard(nombre = "ORG #ORGANIZATION_ID #ORGANIZATION_CODE \n#ORGANIZATION_NAME",
									 listOf<PanelUI>(

										 conectorLectora(dashboardLectrora),
										 panelEndPointSolicitudes,

										 comunes.obtenerPanelTransaccionesEstado(filtroOrganizacion = true),
										 comunes.obtenerPanelTransaccionesOK(filtroOrganizacion = true),
										 comunes.obtenerPanelTransaccionesError(filtroOrganizacion = true),

										 comunes.obtenerPanelTransaccionesErrores(filtroOrganizacion = true),
										 comunes.obtenerPanelTransaccionesDiarias(filtroOrganizacion = true),
										 comunes.obtenerPanelTransaccionesHistorico(filtroOrganizacion = true),
										 comunes.obtenerPanelConteoTransacciones(filtroOrganizacion = true),
										 comunes.obtenerPanelTransaccionesPorHoras(filtroOrganizacion = true),
										 obtenerTransaccionesPorLectora(filtroOrganizacion = true)

										 ),

									 kpiOrigen = comunes.crearKpiOrganizaciones(),
									 etiqueta = Etiquetas.EtiquetaValor("ORGS"),
									 color = -2354116

		)

	}

	suspend fun obtenerTransaccionesPorLectora(filtroOrganizacion: Boolean = false, filtroLectora: Boolean = false): PanelUI {
		val _kpi = KpiUI(
			titulo = "Transacciones por lectora",
			descripcion = "Transacciones relizadas por lectora",
			origen = "",
			sql = SQL.CONTEO_TRANSACCIONES_POR_LECTORA,
			dinamico = true,
			parametros = Parametros()
		)

		val kpiGenerado = operaciones.guardarKpi(_kpi)


		val panel = operaciones.crearPanel(kpiGenerado,
										   false,
										   PlantillasPanel.from(PlantillasPanel.TT.Circular.valor).configuracion.copy(
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



