package com.personal.requestmobility.dashboards.navegacion

import kotlinx.serialization.Serializable



@Serializable
object CuadriculaDashboards


@Serializable
data class VisualizadorDashboard(val id: Int)


@Serializable
object ListadoDashboards

@Serializable
data class DetalleDashboard(val id: Int)