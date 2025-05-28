package com.personal.requestmobility.paneles.domain.entidades

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class PanelConfiguracion(
    val orientacion: PanelOrientacion = PanelOrientacion.VERTICAL,
    val tipo: PanelTipoGrafica = PanelTipoGrafica.BARRAS_ANCHAS_VERTICALES,

    val titulo: String = "GR 001",
    val descripcion : String = "",
    val limiteElementos: Int = 15,
    val mostrarEtiquetas: Boolean = true,


    val target: Float = 0f,
    val ordenado: Boolean = false,
    val espacioGrafica: Float = 0.4f,
    val espacioTabla: Float = 0.6f,
    val ocuparTodoEspacio: Boolean = false,

    val width: Dp = 500.dp,
    val height: Dp = 600.dp,


    /*val paddingTablaVertical : PaddingValues =  PaddingValues(60.dp, 15.dp),
    val paddingTablaHorizontal : PaddingValues =  PaddingValues(2.dp, 15.dp),*/
    val mostrarGrafica: Boolean = true,
    val mostrarTabla: Boolean = true,
    val mostrarTituloTabla: Boolean = true,
    val agruparValores : Boolean = true,

    val campoAgrupacionTabla : Int = 0,
    val campoSumaValorTabla : Int = 1,

    val colores : List<Color>  = listOf<Color>(
        Color(0xFFFF0000),
        Color(0xFF00FF00),
        Color(0xFF0000FF),
        Color(0xFFFFFF00),
        Color(0xFFFF00FF),
        Color(0xFF00FFFF),
        Color(0xFFFF69B4),
        Color(0xFFFFA500),
        Color(0xFF800080),
        Color(0xFF008000),
        Color(0xFF000080),
    ),
    var ajustarContenidoAncho: Boolean = true,
    var indicadorColor: Boolean = true,
    var filasColor: Boolean = true
)