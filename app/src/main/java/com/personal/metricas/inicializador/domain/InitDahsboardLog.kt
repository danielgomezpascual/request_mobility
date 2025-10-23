package com.personal.metricas.inicializador.domain

import com.personal.metricas.core.composables.tabla.Columnas
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.dashboards.ui.entidades.DashboardUI
import com.personal.metricas.dashboards.ui.entidades.Etiquetas
import com.personal.metricas.inicializador.domain.comunes.KpisComunes
import com.personal.metricas.inicializador.domain.sqls.SQL
import com.personal.metricas.kpi.ui.entidades.KpiUI
import com.personal.metricas.kpi.ui.entidades.toKpi
import com.personal.metricas.notas.domain.NotasManager
import com.personal.metricas.paneles.domain.entidades.Alarmas
import com.personal.metricas.paneles.domain.entidades.Condiciones
import com.personal.metricas.paneles.domain.entidades.Conector
import com.personal.metricas.paneles.domain.entidades.EsquemaColores
import com.personal.metricas.paneles.domain.entidades.FuncionesCondicionesCeldaManager
import com.personal.metricas.paneles.domain.entidades.Panel
import com.personal.metricas.paneles.domain.entidades.PanelConfiguracion
import com.personal.metricas.paneles.domain.entidades.PanelData
import com.personal.metricas.paneles.domain.entidades.PanelTipoGrafica
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel
import com.personal.metricas.paneles.domain.entidades.TIPO_CONECTOR
import com.personal.metricas.paneles.domain.entidades.TiposPanel
import com.personal.metricas.paneles.ui.entidades.ColoresSeleccion
import com.personal.metricas.paneles.ui.entidades.PanelUI
import com.personal.metricas.paneles.ui.entidades.fromPanel

class InitDahsboardLog(
	private val operaciones: InicializadorOperaciones,
	private val comunes: KpisComunes,
) {
	suspend fun crearDashboard() {


		val dh = operaciones.guardarDashboard(nombre = "Log",
											  listOf<PanelUI>(
												  dameHorasTransacciones(),
												  dameHistoricoLog(),
												  dameTipoSincronziacion(),

												  ),
											  etiqueta = Etiquetas.EtiquetaValor("Log"),
											  home = true,
											  color = -16744448
		)
	}


	suspend fun dameHorasTransacciones(): PanelUI {


		var sql = """
						SELECT
							STRFTIME('%H', CREATION_DATE) AS Hora,
							COUNT(MOB_REQUEST_ID) AS 'Trx'
						FROM
							TRANSACCIONES
									
						GROUP BY
							Hora
						ORDER BY
							1 ASC
					""".trimIndent()


		val kpiHorasTransacciones = KpiUI(
			titulo = "Transacciones por horas",
			descripcion = "Estimación de procesamiento de transacciones por horas (Trabajo de TRX real).",
			origen = "",
			sql = sql,
			dinamico = true,
			parametros = Parametros()
		)





		return operaciones.crearPanel(kpiHorasTransacciones,
									  true,
									  PlantillasPanel.from(PlantillasPanel.TT.BarrasFinasVertivales.valor).configuracion.copy(
										  colores = EsquemaColores().getFijo().id,
										  limiteElementos = 0,
										  indicadorColor = true,
										  ajustarContenidoAncho = true,
									  ))


	}

	suspend fun dameHistoricoLog(): PanelUI {


		var _k = KpiUI(
			titulo = "Log Sincronziaciones",
			descripcion = "Sincronizaciones",
			origen = "",
			sql = SQL.LOG_SINCRONZIACIONES,
			dinamico = true,
			parametros = Parametros()
		)

		var k = operaciones.guardarKpi(_k)


		val SINC_MANUAL: Condiciones = Condiciones(id = 1,
												columna = Columnas(nombre = "TIPO"),
												color = 6,
												condicionCelda = FuncionesCondicionesCeldaManager.SIN_DEFINIR,
												predicado = "== 'MANUAL'",
												descripion =  "",
												alarma = Alarmas())
		val listaCondicionesSincro: List<Condiciones> = listOf<Condiciones>(SINC_MANUAL)



		return operaciones.crearPanel(k,
									  false,
									  PlantillasPanel.from(PlantillasPanel.TT.SoloTabla.valor).configuracion.copy(
										  colores = EsquemaColores().get(EsquemaColores.PERS_AMARILLA).id,
										  limiteElementos = 0,
										  indicadorColor = false,
										  condicionesCeldas = listaCondicionesSincro,
										  condiciones =listaCondicionesSincro,
										  ajustarContenidoAncho = true,
									  ))

	}

	suspend fun dameTipoSincronziacion(): PanelUI {


		var _k = KpiUI(
			titulo = "Sincronziaciones",
			descripcion = "Sincronizaciones",
			origen = "",
			sql = SQL.LOG_CONTEO_SINCRONZIACIONES,
			dinamico = true,
			parametros = Parametros()
		)

		var k = operaciones.guardarKpi(_k)


		return operaciones.crearPanel(k,
									  false,
									  PlantillasPanel.from(PlantillasPanel.TT.Circular.valor).configuracion.copy(
										  colores = EsquemaColores().get(EsquemaColores.PERS_AMARILLA).id,
										  limiteElementos = 0,
										  indicadorColor = false,
										  ajustarContenidoAncho = true,
									  ))

	}
}


