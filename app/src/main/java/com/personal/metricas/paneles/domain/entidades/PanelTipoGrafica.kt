package com.personal.metricas.paneles.domain.entidades

import com.personal.metricas.R


sealed class PanelTipoGrafica{
    data class IndicadorVertical(val id: Int = 0, val nombre : String = "INDICADOR VERTICAL" , val icono : Int = R.drawable.indicadorv): PanelTipoGrafica()
    data class IndicadorHorizontal(val id: Int = 1, val nombre : String = "INDICADOR HORIZONTAL" , val icono : Int = R.drawable.indicadorh): PanelTipoGrafica()
    data class BarrasAnchasVerticales(val id: Int = 2, val nombre : String = "BARRAS ANCHAS VERTICALES" , val icono : Int = R.drawable.linasanchas): PanelTipoGrafica()
    data class BarrasFinasVerticales(val id: Int = 3, val nombre : String = "BARRAS FINAS VERTICALES" , val icono : Int = R.drawable.lineasfinas): PanelTipoGrafica()
    data class Circular(val id: Int = 4, val nombre : String = "CIRCULARL" , val icono : Int = R.drawable.circular): PanelTipoGrafica()
    data class Anillo(val id: Int = 5, val nombre : String = "ANILLO" , val icono : Int = R.drawable.anillo): PanelTipoGrafica()
    data class Lineas(val id: Int = 6, val nombre : String = "LINEAS" , val icono : Int = R.drawable.lineas): PanelTipoGrafica()

companion object{
    fun dameTipos() : List<PanelTipoGrafica> = listOf<PanelTipoGrafica>(
            IndicadorVertical(),
            IndicadorHorizontal(),
            BarrasAnchasVerticales(),
            BarrasFinasVerticales(),
            Circular(),
            Anillo(),
            Lineas()                                                   )
	}
}
