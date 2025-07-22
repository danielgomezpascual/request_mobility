package com.personal.metricas.paneles.domain.entidades

enum class PanelOrientacion(val tipo: String) {
    VERTICAL("VERTICAL"),
    HORIZONTAL("HORIZONTAL");

    companion object {

        fun from(codigo: String): PanelOrientacion = (entries.find { it.tipo == codigo })?: PanelOrientacion.VERTICAL


    }

}