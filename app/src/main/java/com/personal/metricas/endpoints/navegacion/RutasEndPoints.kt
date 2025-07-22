package com.personal.metricas.endpoints.navegacion

import kotlinx.serialization.Serializable

@Serializable
object ScreenListadoEndPoints

@Serializable
data class ScreenDetalleEndPoints(val id: Int)