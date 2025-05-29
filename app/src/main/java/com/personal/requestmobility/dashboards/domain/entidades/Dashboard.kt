package com.personal.requestmobility.dashboards.domain.entidades

import androidx.annotation.StyleRes
import com.personal.requestmobility.paneles.domain.entidades.Panel

data class Dashboard(val id: Int,
                     val nombre: String,
                     val logo : String,
                     val descripcion: String,
                     val paneles: List<Panel> = listOf<Panel>() )



