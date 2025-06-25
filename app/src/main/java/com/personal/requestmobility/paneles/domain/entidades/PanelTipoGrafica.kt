package com.personal.requestmobility.paneles.domain.entidades

enum class PanelTipoGrafica(val tipo: String) {
    INDICADOR_VERTICAL("INDICADOR_VERTICAL"),
    INDICADOR_HORIZONTAL("INDICADOR_HORIZONTAL"),
    BARRAS_ANCHAS_VERTICALES("BARRAS_ANCHAS_VERTICALES"),
    BARRAS_FINAS_VERTICALES("BARRAS_FINAS_VERTICALES"),
    CIRCULAR("CIRCULAR"),
    ANILLO("ANILLO"),
    LINEAS("LINEAS");

    companion object{
        fun from(codigo: String): PanelTipoGrafica = (entries.find { it.tipo == codigo }) ?: PanelTipoGrafica.BARRAS_ANCHAS_VERTICALES
    }

}