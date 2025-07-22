package com.personal.metricas.dashboards.navegacion

import kotlinx.serialization.Serializable



@Serializable
object CuadriculaDashboards


@Serializable
data class VisualizadorDashboard(val id: Int, val parametrosJson : String)


@Serializable
object ListadoDashboards

@Serializable
data class DetalleDashboard(val id: Int)