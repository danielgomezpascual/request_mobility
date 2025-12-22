package com.personal.metricas.paneles.data.ds.local.entidades

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.personal.metricas.paneles.domain.entidades.Condiciones
import com.personal.metricas.paneles.domain.entidades.PanelConfiguracion
import com.personal.metricas.paneles.domain.entidades.PanelOrientacion
import com.personal.metricas.paneles.domain.entidades.PanelTipoGrafica
import kotlin.Boolean

data class PanelConfiguracionRoom(
        val orientacion: PanelOrientacion = PanelOrientacion.VERTICAL,
        val plantilla: Int = 0,
        val tipo: Int = 0,
        val titulo: String = "",
        val descripcion: String = "",
        val limiteElementos: Int = 10,
        val mostrarEtiquetas: Boolean = true,
        val agruparResto: Boolean = true,
        val target: Float = 0f,
        val ordenado: Boolean = false,
        val espacioGrafica: String = "40",
        val espacioTabla: String = "60",
        val ocuparTodoEspacio: Boolean = false,
        val width: String = "500",
        val height: String = "600",

        /*val paddingTablaVertical : PaddingValues =  PaddingValues(60.dp, 15.dp),
        val paddingTablaHorizontal : PaddingValues =  PaddingValues(2.dp, 15.dp),*/
        val mostrarGrafica: Boolean = true,
        val mostrarTabla: Boolean = true,
        val mostrarTituloTabla: Boolean = true,
        val agruparValores: Boolean = true,
        val columnaX: Int = 0,
        val columnaY: Int = 1,
        val colores: Int = 0,
        var ajustarContenidoAncho: Boolean = true,
        var indicadorColor: Boolean = true,
        var filasColor: Boolean = true,
        val condiciones: List<Condiciones> = listOf<Condiciones>(),
        val condicionesCeldas: List<Condiciones> = listOf<Condiciones>(),
        val permiteNotas: Boolean = true,
        val valorMaximo: String = "10",
        val colorFondoIndicador: Int = Color.White.toArgb(),
        val colorPanel: Int = Color.White.toArgb(),
        val filtroOrganizacion: Boolean = false,
        val filtroLectora: Boolean = false,
        val celdasPantallasGrandes: Int = 1,
        val textoCeldaCompleto: Boolean = false,
        val textoCompletoHorizontal: Boolean = false,
) {

    companion object {
        fun fromConfiguracion(panelConfiguracion: PanelConfiguracion): PanelConfiguracionRoom {
            val id =
                    when (panelConfiguracion.tipo) {
                        is PanelTipoGrafica.Anillo -> panelConfiguracion.tipo.id
                        is PanelTipoGrafica.BarrasAnchasVerticales -> panelConfiguracion.tipo.id
                        is PanelTipoGrafica.BarrasFinasVerticales -> panelConfiguracion.tipo.id
                        is PanelTipoGrafica.Circular -> panelConfiguracion.tipo.id
                        is PanelTipoGrafica.IndicadorHorizontal -> panelConfiguracion.tipo.id
                        is PanelTipoGrafica.IndicadorVertical -> panelConfiguracion.tipo.id
                        is PanelTipoGrafica.Lineas -> panelConfiguracion.tipo.id
                        is PanelTipoGrafica.SignalVertical -> panelConfiguracion.tipo.id
                        is PanelTipoGrafica.SignalHorizontal -> panelConfiguracion.tipo.id
                    }

            return PanelConfiguracionRoom(
                    orientacion = panelConfiguracion.orientacion,
                    tipo = id,
                    plantilla = panelConfiguracion.plantilla,
                    titulo = panelConfiguracion.titulo,
                    descripcion = panelConfiguracion.descripcion,
                    limiteElementos = panelConfiguracion.limiteElementos,
                    mostrarEtiquetas = panelConfiguracion.mostrarEtiquetas,
                    agruparResto = panelConfiguracion.agruparResto,
                    target = panelConfiguracion.target,
                    ordenado = panelConfiguracion.ordenado,
                    espacioGrafica = panelConfiguracion.espacioGrafica,
                    espacioTabla = panelConfiguracion.espacioTabla,
                    ocuparTodoEspacio = panelConfiguracion.ocuparTodoEspacio,
                    width = panelConfiguracion.width,
                    height = panelConfiguracion.height,
                    mostrarGrafica = panelConfiguracion.mostrarGrafica,
                    mostrarTabla = panelConfiguracion.mostrarTabla,
                    mostrarTituloTabla = panelConfiguracion.mostrarTituloTabla,
                    agruparValores = panelConfiguracion.agruparValores,
                    columnaX = panelConfiguracion.columnaX,
                    columnaY = panelConfiguracion.columnaY,
                    colores = panelConfiguracion.colores,
                    ajustarContenidoAncho = panelConfiguracion.ajustarContenidoAncho,
                    indicadorColor = panelConfiguracion.indicadorColor,
                    filasColor = panelConfiguracion.filasColor,
                    condiciones = panelConfiguracion.condiciones,
                    condicionesCeldas = panelConfiguracion.condicionesCeldas,
                    permiteNotas = panelConfiguracion.permiteNotas,
                    valorMaximo = panelConfiguracion.valorMaximo,
                    colorFondoIndicador = panelConfiguracion.colorFondoIndicador,
                    colorPanel = panelConfiguracion.colorPanel,
                    filtroOrganizacion = panelConfiguracion.filtroOrganizacion,
                    filtroLectora = panelConfiguracion.filtroLectora,
                    celdasPantallasGrandes = panelConfiguracion.celdasPantallasGrandes,
                    textoCeldaCompleto = panelConfiguracion.textoCeldaCompleto,
                    textoCompletoHorizontal = panelConfiguracion.textoCeldaHorizontal,
            )
        }
    }

    fun toConfiguracion(): PanelConfiguracion {

        val tipo: PanelTipoGrafica =
                when (this.tipo) {
                    0 -> PanelTipoGrafica.IndicadorVertical()
                    1 -> PanelTipoGrafica.IndicadorHorizontal()
                    2 -> PanelTipoGrafica.BarrasAnchasVerticales()
                    3 -> PanelTipoGrafica.BarrasFinasVerticales()
                    4 -> PanelTipoGrafica.Circular()
                    5 -> PanelTipoGrafica.Anillo()
                    6 -> PanelTipoGrafica.Lineas()
                    7 -> PanelTipoGrafica.SignalVertical()
                    8 -> PanelTipoGrafica.SignalHorizontal()
                    else -> PanelTipoGrafica.IndicadorVertical()
                }

        // val plantillaPanel = PlantillasPanel.from(this.plantilla)
        return PanelConfiguracion(
                orientacion = this.orientacion,
                tipo = tipo,
                plantilla = this.plantilla,
                titulo = this.titulo,
                descripcion = this.descripcion,
                limiteElementos = this.limiteElementos,
                mostrarEtiquetas = this.mostrarEtiquetas,
                agruparResto = this.agruparResto,
                target = this.target,
                ordenado = this.ordenado,
                espacioGrafica = this.espacioGrafica,
                espacioTabla = this.espacioTabla,
                ocuparTodoEspacio = this.ocuparTodoEspacio,
                width = this.width,
                height = this.height,
                mostrarGrafica = this.mostrarGrafica,
                mostrarTabla = this.mostrarTabla,
                mostrarTituloTabla = this.mostrarTituloTabla,
                agruparValores = this.agruparValores,
                columnaX = this.columnaX,
                columnaY = this.columnaY,
                colores = this.colores,
                ajustarContenidoAncho = this.ajustarContenidoAncho,
                indicadorColor = this.indicadorColor,
                filasColor = this.filasColor,
                condiciones = this.condiciones,
                condicionesCeldas = this.condicionesCeldas,
                permiteNotas = this.permiteNotas,
                valorMaximo = this.valorMaximo,
                colorFondoIndicador = this.colorFondoIndicador,
                colorPanel = this.colorPanel,
                filtroOrganizacion = this.filtroOrganizacion,
                filtroLectora = this.filtroLectora,
                celdasPantallasGrandes = this.celdasPantallasGrandes,
                textoCeldaCompleto = this.textoCeldaCompleto,
                textoCeldaHorizontal = this.textoCompletoHorizontal,
        )
    }
}
