package com.personal.metricas.inicializador.domain

import com.personal.metricas.core.composables.tabla.Columnas
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.dashboards.ui.entidades.Etiquetas
import com.personal.metricas.inicializador.domain.comunes.KpisComunes
import com.personal.metricas.inicializador.domain.sqls.SQL
import com.personal.metricas.kpi.ui.entidades.KpiUI
import com.personal.metricas.paneles.domain.entidades.Alarmas
import com.personal.metricas.paneles.domain.entidades.Condiciones
import com.personal.metricas.paneles.domain.entidades.EsquemaColores
import com.personal.metricas.paneles.domain.entidades.FuncionesCondicionesCeldaManager
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel
import com.personal.metricas.paneles.ui.entidades.PanelUI

class InitDahsboardVersionesGeneral(
	private val operaciones: InicializadorOperaciones,
	private val comunes: KpisComunes,
) {
	suspend fun crearDashboard() {


		val dh = operaciones.guardarDashboard(nombre = "Versiones",
											  listOf<PanelUI>(

												  comunes.createGenerico(titulo= "Exito por version", SQL.TASA_EXITO_POR_VERSION, plantilla =  PlantillasPanel.SoloTabla.id, celdas = 2, colores = EsquemaColores.PERS),
												  comunes.createGenerico(titulo= "Errores por version", SQL.ERRORES_POR_VERSION_TRX, plantilla =  PlantillasPanel.SoloTabla.id, celdas = 2, colores = EsquemaColores.PERS_ROJA),
												  comunes.createGenerico(titulo= "Volumen uso", SQL.VOLUMEN_USO_DISTRIBUICION, plantilla =  PlantillasPanel.Circular.id, celdas = 2, colores = EsquemaColores.PERS, columnaCalculo = 2),
												  comunes.createGenerico(titulo= "Distribuicion por estados", SQL.DISTRIBUCION_POR_ESTADOS, plantilla =  PlantillasPanel.SoloTabla.id, celdas = 2, colores = EsquemaColores.PERS_VERDE),
												  comunes.createGenerico(titulo= "Reprocesados por version", SQL.TRX_REPROCESADAS_POR_VERSION, plantilla =  PlantillasPanel.PanelesVerticales.id, celdas = 2, colores = EsquemaColores.PERS),
												  comunes.createGenerico(titulo= "Distribuicion en lectoras", SQL.DISTRIBUCION_VERSION_LECTRORAS_FISICA, plantilla =  PlantillasPanel.SoloTabla.id, celdas = 2, colores = EsquemaColores.PERS_AMARILLA),
												  comunes.createGenerico(titulo= "Angüedad última Trx", SQL.DIAS_ANTIGUEDAD_ULTIMA_TRANSACCION, plantilla =  PlantillasPanel.PanelesVerticales.id, celdas = 2, colores = EsquemaColores.PERS, columnaCalculo = 2),

												  ),
											  etiqueta = Etiquetas.EtiquetaValor("Versioens"),
											  home = false,
											  color = -16744448
		)
	}


}


