package com.personal.requestmobility.core.composables.componentes

import com.personal.requestmobility.R

sealed class TituloScreen(val titulo: String, val descripcion: String, val icono: Int ){
	object Home: TituloScreen(titulo = "Home", descripcion = "Paneles inciales", icono = R.drawable.panel_top2)
	object Sincronizar: TituloScreen(titulo = "Sincronziacion", descripcion = "Obten los datos y mide sus resultados", icono = R.drawable.sincro_top)
	object Cuadriculas: TituloScreen(titulo = "Cuadriculas", descripcion = "Consulta los dashboards", icono = R.drawable.cuadricula)
	object DashboardDetalle: TituloScreen(titulo = "Dashboard", descripcion = "Define y establece paneles a consultar", icono = R.drawable.dashboard)
	object DashboardLista: TituloScreen(titulo = "Dashboard", descripcion = "Define y establece paneles a consultar", icono = R.drawable.dashboard)
	object Herramientas: TituloScreen(titulo = "Herramientas", descripcion = "Define componentes", icono = R.drawable.herramientas)
	object Paneles: TituloScreen(titulo = "Paneles", descripcion = "Define paneles", icono = R.drawable.paneles)
	object Kpi: TituloScreen(titulo = "Kpis", descripcion = "Define kpis para su explortacion", icono = R.drawable.kpi)
}