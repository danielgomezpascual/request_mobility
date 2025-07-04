package com.personal.requestmobility.paneles.domain.entidades

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.tabla.Celda
import com.personal.requestmobility.core.composables.tabla.Columnas
import com.personal.requestmobility.core.composables.tabla.Fila
import com.personal.requestmobility.core.composables.tabla.ValoresTabla
import com.personal.requestmobility.paneles.ui.entidades.PanelUI
import com.personal.requestmobility.transacciones.domain.entidades.ResultadoSQL
import org.apache.commons.jexl3.JexlBuilder
import org.apache.commons.jexl3.MapContext

fun PanelData.fromPanelUI(panelUI: PanelUI): PanelData {
    val panelConfiguracion = panelUI.configuracion
    val tabla: ValoresTabla = ResultadoSQL.fromSqlToTabla(panelUI.kpi.sql)
    return PanelData(
        panelConfiguracion = panelConfiguracion,
        valoresTabla = tabla
    )
}

data class PanelData(
    val panelConfiguracion: PanelConfiguracion = PanelConfiguracion(),
    var valoresTabla: ValoresTabla = ValoresTabla()) {

    fun ordenarElementos() = valoresTabla.dameElementosOrdenados(campoOrdenacionTabla = panelConfiguracion.columnaY)

    fun limiteElementos(): List<Fila> =
        valoresTabla.dameElementosTruncados(
            panelConfiguracion
        )

    fun establecerColorFilas(): List<Fila> {


        //establecemos los colores que va a tener cada elmento (teninedo en cuenta que deben al menos tener todos los coles declarados.
        val colores = EsquemaColores().getColores(panelConfiguracion.colores)
        val totalColores: Int = colores.size
        valoresTabla.filas = valoresTabla.filas.mapIndexed { index, f ->
            val indiceColor = index % totalColores
            f.copy(color = colores[indiceColor])
        }


        //miramos si tenemos alguna condicin para en ese caso aplicarla
        if (panelConfiguracion.condiciones.isNotEmpty()) {
            val jexl = JexlBuilder().create()
            valoresTabla.filas = valoresTabla.filas.mapIndexed { indice, fila ->
                var color = fila.color
                panelConfiguracion.condiciones.forEach { condicion ->
                    val expresion = jexl.createExpression("valor " + condicion.predicado)
                    
                    //val valorY = panelConfiguracion.columnaY
                    val valorY = condicion.columna.posicion
                
                    var valor: String = fila.celdas.get(valorY).valor as String
                    747306
                    App.log.d("Posicion $valorY -> $valor")

                    if (valor.isNotEmpty()) {
                        val contexto = MapContext().apply {
                            set("valor", valor.toFloat())
                        }
                        val resultado: Any = expresion.evaluate(contexto)
                        if (resultado is Boolean && resultado) {
                            color =
                                EsquemaColores().dameEsquemaCondiciones().colores.get(condicion.color)
                        }
                    }
                }
                fila.copy(color = color)
            }
        }


        //miramos si tenemos alguna condicin para alguna celda
        if (panelConfiguracion.condicionesCeldas.isNotEmpty()) {
            val funcionesCondicionesCelda: FuncionesCondicionesCeldaManager = FuncionesCondicionesCeldaManager()
            val jexl = JexlBuilder().create()

            valoresTabla.filas = valoresTabla.filas.mapIndexed { indice, fila ->

                var nuevasCeldas: List<Celda> = fila.celdas
                //         var hayModificaciones: Boolean = false



                panelConfiguracion.condicionesCeldas.forEach { condicionCelda ->

                    try{



                        val columnaEvaluar: Columnas = condicionCelda.columna
                        val posicionEvaluar = columnaEvaluar.posicion
                        val exp = "valor " + condicionCelda.predicado
                        App.log.d("Expresion a evaluar $exp")
                        val expresion = jexl.createExpression(exp)
                        val valor: String = fila.celdas.get(posicionEvaluar).valor as String
                        val contexto = MapContext().apply {
                            set("valor", valor)
                        }
                        val resultadoCondicion: Any = expresion.evaluate(contexto)
                        if (resultadoCondicion is Boolean && resultadoCondicion) {
                            val colorCondicion = EsquemaColores().dameEsquemaCondiciones().colores.get(condicionCelda.color)
                            nuevasCeldas = nuevasCeldas.mapIndexed { indice, celda ->
                                if (indice == posicionEvaluar) {
                                    val celdaConCondicion: FuncionesCondicionCelda = funcionesCondicionesCelda.aplicarCondicion(valor, condicionCelda,  valoresTabla.columnas.get(indice))
                                    if (condicionCelda.condicionCelda > 0) {
                                        celda.copy(colorCelda = colorCondicion, contenido = {
                                            Box(modifier = Modifier.background(color = colorCondicion)) {
                                                celdaConCondicion.composable()
                                            }
                                        })
                                    } else {
                                        celda.copy(colorCelda = colorCondicion)
                                    }
                                } else {
                                    celda
                                }
                            }
                        } /*else {
                            nuevasCeldas = nuevasCeldas
                        }*/
                    }catch (e: Exception){
                        e.printStackTrace()
                       // nuevasCeldas = nuevasCeldas
                    }


                }

                App.log.lista("Celdas", nuevasCeldas)
                fila.copy(celdas = nuevasCeldas)


                /*   fila.celdas
                   fila.copy(color = color)*/
            }
        }



        return valoresTabla.filas
    }
}