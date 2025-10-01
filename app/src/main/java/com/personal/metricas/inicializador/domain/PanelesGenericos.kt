package com.personal.metricas.inicializador.domain

import com.personal.metricas.App
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.kpi.ui.entidades.KpiUI
import com.personal.metricas.notas.domain.NotasManager
import com.personal.metricas.paneles.domain.entidades.PanelConfiguracion
import com.personal.metricas.paneles.domain.entidades.PanelData
import com.personal.metricas.paneles.domain.entidades.PanelTipoGrafica
import com.personal.metricas.paneles.ui.componente.MA_Panel
import com.personal.metricas.paneles.ui.entidades.PanelUI

enum class ACTUA_SOBRE { GENERAL, ORGANIZACION, PDA }

object PanelesGenericos {

	fun PanelHoras(actuaSobre: ACTUA_SOBRE = ACTUA_SOBRE.GENERAL, valor : String = ""): PanelUI {
		val keyReemplazarPorActuacion: String = "[XXX_XXX]"

		var sql = """
						SELECT
							STRFTIME('%H', CREATION_DATE) AS Hora,
							COUNT(MOB_REQUEST_ID) AS 'Trx'
						FROM
							TRANSACCIONES
							$keyReemplazarPorActuacion
						GROUP BY
							Hora
						ORDER BY
							1 ASC;
					""".trimIndent()


		sql = when (actuaSobre) {
			ACTUA_SOBRE.GENERAL      -> sql.replace(keyReemplazarPorActuacion, "")
			ACTUA_SOBRE.ORGANIZACION -> sql.replace(keyReemplazarPorActuacion, " WHERE ORGANIZATION_CODE = '$valor'")
			ACTUA_SOBRE.PDA          -> sql.replace(keyReemplazarPorActuacion, " WHERE  LECTORA_FISICA_ID = '$valor' ")
		}


		App.log.d(sql)
		val kpiHorasTransacciones = KpiUI(
			titulo = "Transacciones por horas",
			descripcion = "Estimación de procesamiento de transacciones por horas (Trabajo de TRX real).",
			origen = "",
			sql = sql,
			dinamico = true,
			parametros = Parametros()
		)


		val configuracion = PanelConfiguracion().copy(
			ajustarContenidoAncho = true,
			tipo = PanelTipoGrafica.BarrasFinasVerticales(),
			mostrarTabla = false,
			mostrarEtiquetas = true)

		//val kpi = if3(crearKPI, guardarKpi(kpiUI), kpiUI)
		val panel = PanelUI.Companion.crearPanelUI(kpiHorasTransacciones, configuracion)

		return panel
		//MA_Panel(panelData = PanelData.fromPanelUI(panel, NotasManager(), Parametros()))


	}
}