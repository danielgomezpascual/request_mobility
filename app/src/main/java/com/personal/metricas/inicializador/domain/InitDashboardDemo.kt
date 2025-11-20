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
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel.Anillo
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel.BarrasAnchasVertivales
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel.BarrasFinasVertivales
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel.Circular
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel.IndicadorHorizontal
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel.IndicadorVertical
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel.Lineas
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel.PanelesHorizontales
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel.PanelesVerticales
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel.SinSeleccionar
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel.SingalHorizontal
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel.SingalHorizontalTabla
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel.SingalVertical
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel.SingalVerticalTabla
import com.personal.metricas.paneles.domain.entidades.PlantillasPanel.SoloTabla
import com.personal.metricas.paneles.domain.entidades.TIPO_CONECTOR
import com.personal.metricas.paneles.domain.entidades.TiposPanel
import com.personal.metricas.paneles.ui.entidades.ColoresSeleccion
import com.personal.metricas.paneles.ui.entidades.PanelUI
import com.personal.metricas.paneles.ui.entidades.fromPanel

class InitDashboardDemo(
	private val operaciones: InicializadorOperaciones,
	private val comunes: KpisComunes,
) {
	suspend fun crearDashboard(){
		val dh = operaciones.guardarDashboard(nombre = "DEMO",
											  listOf<PanelUI>(

													   comunes.createGenerico(titulo = "DEMO", SQL.SQL_DEMO, plantilla =PlantillasPanel.SoloTabla.id, celdas = 1, colores = EsquemaColores.MUTICOLOR),
													   comunes.createGenerico(titulo = "DEMO", SQL.SQL_DEMO, plantilla =PlantillasPanel.BarrasFinasVertivales.id, celdas = 1, colores = EsquemaColores.FOSFORITOS),
													   comunes.createGenerico(titulo = "DEMO", SQL.SQL_DEMO, plantilla =PlantillasPanel.BarrasAnchasVertivales.id, celdas = 1, colores = EsquemaColores.MUTICOLOR),
													   comunes.createGenerico(titulo = "DEMO", SQL.SQL_DEMO, plantilla =PlantillasPanel.BarrasAnchasVertivales.id, celdas = 1, colores = EsquemaColores.PERS_ROJA),
													   comunes.createGenerico(titulo = "DEMO", SQL.SQL_DEMO, plantilla =PlantillasPanel.BarrasAnchasVertivales.id, celdas = 1, colores = EsquemaColores.ERRORES),
													   comunes.createGenerico(titulo = "DEMO", SQL.SQL_DEMO, plantilla =PlantillasPanel.Circular.id, celdas = 1, colores = EsquemaColores.MUTICOLOR),
													   comunes.createGenerico(titulo = "DEMO", SQL.SQL_DEMO, plantilla =PlantillasPanel.Circular.id, celdas = 1, colores = EsquemaColores.GRISES),
													   comunes.createGenerico(titulo = "DEMO", SQL.SQL_DEMO, plantilla =PlantillasPanel.Anillo.id, celdas = 1, colores = EsquemaColores.MUTICOLOR),
													   comunes.createGenerico(titulo = "DEMO", SQL.SQL_DEMO, plantilla =PlantillasPanel.Lineas.id, celdas = 1, colores = EsquemaColores.MUTICOLOR),
													   comunes.createGenerico(titulo = "DEMO", SQL.SQL_DEMO, plantilla =PlantillasPanel.Lineas.id, celdas = 1, colores = EsquemaColores.PERS_VERDE),
													   comunes.createGenerico(titulo = "DEMO", SQL.SQL_DEMO, plantilla =PlantillasPanel.PanelesHorizontales.id, celdas = 1, colores = EsquemaColores.MUTICOLOR),
													   comunes.createGenerico(titulo = "DEMO", SQL.SQL_DEMO, plantilla =PlantillasPanel.PanelesVerticales.id, celdas = 1, colores = EsquemaColores.MUTICOLOR),
													   comunes.createGenerico(titulo = "DEMO", SQL.SQL_DEMO, plantilla =PlantillasPanel.SingalVertical.id, celdas = 1, colores = EsquemaColores.MUTICOLOR),
													   comunes.createGenerico(titulo = "DEMO", SQL.SQL_DEMO, plantilla =PlantillasPanel.SingalHorizontal.id, celdas = 1, colores = EsquemaColores.MUTICOLOR),
													   comunes.createGenerico(titulo = "DEMO", SQL.SQL_DEMO, plantilla =PlantillasPanel.SingalVerticalTabla.id, celdas = 1, colores = EsquemaColores.MUTICOLOR),
													   comunes.createGenerico(titulo = "DEMO", SQL.SQL_DEMO, plantilla =PlantillasPanel.SingalVerticalTabla.id, celdas = 1, colores = EsquemaColores.PERS_VERDE),
													   //comunes.createGenerico(titulo = "DEMO 15", SQL.SQL_DEMO, plantilla =PlantillasPanel.SingalHorizontalTabla.id, celdas = 1, colores = EsquemaColores.MUTICOLOR),
											  ),
											  etiqueta = Etiquetas.EtiquetaValor("DEMO"),
											  home = false,
											  color = -16744448
		)
	}

}


