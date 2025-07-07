package com.personal.requestmobility.dashboards.navegacion

import com.personal.requestmobility.core.composables.tabla.Fila
import com.personal.requestmobility.transacciones.ui.entidades.Filtro
import kotlinx.serialization.Serializable



@Serializable
object CuadriculaDashboards


@Serializable
data class VisualizadorDashboard(val id: Int, val parametrosJson : String)


@Serializable
object ListadoDashboards

@Serializable
data class DetalleDashboard(val id: Int)