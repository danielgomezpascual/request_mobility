package com.personal.requestmobility.paneles.domain.entidades

import com.personal.requestmobility.App
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



        if (panelConfiguracion.condiciones.isNotEmpty()) {
            //miramos si tenemos alguna condicin para en ese caso aplicarla
            // 1. El motor de Jexl. Es buena práctica crearlo una vez y reutilizarlo.
            val jexl = JexlBuilder().create()




            valoresTabla.filas = valoresTabla.filas.mapIndexed { indice, fila ->
                var color = fila.color
                panelConfiguracion.condiciones.forEach { condicion ->
                    // 2. La condición que quieres evaluar, como un String.
                    //val expresionString = "puntuacion > 90 && nivelDeAcceso == 'ADMIN'"
                    // 3. Compilamos la expresión. Esto es más eficiente si la evalúas múltiples veces.
                    val expresion = jexl.createExpression("valor " + condicion.predicado)
                    // 4. Creamos un "contexto" con las variables que la expresión necesita.
                    /*val contexto = MapContext().apply {
                        set("valor", 95)
                        set("nivelDeAcceso", "ADMIN")
                        set("intentos", 3) // Podemos tener variables que no se usan en la expresión
                    }*/
                    val valorY = panelConfiguracion.columnaY
                    val valor : String = fila.celdas.get(valorY).valor as String
                    val contexto = MapContext().apply {
                        set("valor", valor.toFloat())
                    }
                    // 5. ¡Evaluamos!
                    val resultado: Any = expresion.evaluate(contexto)
                    // El resultado es un Boolean, hacemos un cast seguro.
                    if (resultado is Boolean && resultado) {
                        //  fila.copy(color = colores[indiceColor])

                        color = EsquemaColores().dameEsquemaCondiciones().colores.get(condicion.color)

                    }


                }
                fila.copy(color = color)
            }


        }



        return valoresTabla.filas
    }
}