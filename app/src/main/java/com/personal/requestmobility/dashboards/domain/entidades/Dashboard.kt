package com.personal.requestmobility.dashboards.domain.entidades

import androidx.annotation.StyleRes

data class Dashboard(val id: Int,
                     val nombre: String,
                     val logo : String,
                     val descripcion: String,
                     val paneles: List<KpiPaneles> = listOf<KpiPaneles>() )



