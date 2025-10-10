package com.personal.metricas.inicializador.domain

import com.personal.metricas.App
import com.personal.metricas.core.composables.tabla.Columnas
import com.personal.metricas.core.data.ds.remote.network.retrofit.request.Entornos
import com.personal.metricas.core.utils.Parametro
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.dashboards.ui.entidades.Etiquetas
import com.personal.metricas.endpoints.ui.entidades.EndPointUI
import com.personal.metricas.inicializador.domain.comunes.KpisComunes
import com.personal.metricas.inicializador.domain.sqls.SQL
import com.personal.metricas.kpi.ui.entidades.KpiUI
import com.personal.metricas.paneles.domain.entidades.Alarmas
import com.personal.metricas.paneles.domain.entidades.Condiciones
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel
import com.personal.metricas.paneles.ui.entidades.PanelUI

class InitDashboardOrganizaciones(
	private val operaciones: InicializadorOperaciones,
	private val comunes: KpisComunes,
) {


	suspend fun init() {

		//endpoint de recarga de transacciones
		var listaPametrosEP: List<Parametro> = Parametros.dameParametrosPorDefectoMobility()
		listaPametrosEP = listaPametrosEP.plus(Parametro("P_ORGANIZATION_ID", "#ORGANIZATION_ID", "", false))
		val endPoint = EndPointUI(
			nombre = "Recargar datos)",
			descripcion = "Obtener Trx",
			url = "${Entornos.get(App.ENTORNO).url}GetSolicitudes",
			parametros = Parametros(listaPametrosEP),
			tabla = "Transacciones",
			nodoIdentificadorFila = "Solicitudes",
			eliminarDatos = false)
		val panelEndPointSolicitudes = operaciones.guardarEndPoint(endPoint)



		operaciones.guardarDashboard(nombre = "ORG #ORGANIZATION_ID #ORGANIZATION_CODE \n#ORGANIZATION_NAME",
									 listOf<PanelUI>(
										 panelEndPointSolicitudes,
										 comunes.obtenerPanelTransaccionesErrores(filtroOrganizacion = true),
										 comunes.obtenerPanelTransaccionesDiarias(filtroOrganizacion = true),
										 comunes.obtenerPanelTransaccionesHistorico(filtroOrganizacion = true),

										 ),

									 kpiOrigen = comunes.crearKpiOrganizaciones(),
									 etiqueta = Etiquetas.EtiquetaValor("ORGS"),
									 color = -2354116

		)

	}
}



