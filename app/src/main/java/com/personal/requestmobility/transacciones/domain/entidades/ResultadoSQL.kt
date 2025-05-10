package com.personal.requestmobility.transacciones.domain.entidades

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.personal.requestmobility.core.composables.labels.LabelNormal
import com.personal.requestmobility.core.composables.tabla.Celda
import com.personal.requestmobility.core.composables.tabla.Fila
import com.personal.requestmobility.core.composables.tabla.ValoresTabla
import com.personal.requestmobility.core.room.ResultadoEjecucionSQL

data class ResultadoSQL(var titulos: List<String> = emptyList(),
                        var filas: List<List<String>> = emptyList()
) {
    companion object {
        fun from(execSQL: ResultadoEjecucionSQL) = ResultadoSQL(
            titulos = execSQL.titulos,
            filas = execSQL.filas
        )


    }

    fun toValoresTabla(): ValoresTabla {

        var filasValoresTabla: List<Fila> = emptyList<Fila>()
        filas.forEach { fila ->
            var filaVT: List<Celda> = emptyList<Celda>()
            fila.forEachIndexed { indice, contenido ->
                filaVT = filaVT.plus(
                    /*if (indice == 0 ){
                        Celda(valor = contenido, titulo = titulos[indice], contenido = {modifier ->
                            LabelNormal(modifier = modifier, valor = "[TST] $contenido", color =  Color.Red, alineacion = TextAlign.Right)
                        })
                    }else{
                        Celda(valor = contenido, titulo = titulos[indice])
                    }*/
                            Celda(valor = contenido, titulo = titulos[indice])
                )
            }
            filasValoresTabla = filasValoresTabla.plus(Fila(celdas = filaVT))
        }

        return ValoresTabla( filas = filasValoresTabla)

    }
}



