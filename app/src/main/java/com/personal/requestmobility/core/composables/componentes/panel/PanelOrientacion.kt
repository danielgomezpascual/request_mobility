package com.personal.requestmobility.core.composables.componentes.panel

enum class PanelOrientacion(val tipo: String) {
    VERTICAL("VERTICAL"),
    HORIZONTAL("HORIZONTAL");

    companion object {

        fun from(codigo: String): PanelOrientacion = (entries.find { it.tipo == codigo })?: PanelOrientacion.VERTICAL


    }

}