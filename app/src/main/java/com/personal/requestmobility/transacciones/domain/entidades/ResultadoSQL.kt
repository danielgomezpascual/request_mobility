package com.personal.requestmobility.transacciones.domain.entidades

import com.personal.requestmobility.core.composables.tabla.Celda
import com.personal.requestmobility.core.composables.tabla.Fila
import com.personal.requestmobility.core.composables.tabla.ValoresTabla
import com.personal.requestmobility.core.room.AppDatabase
import com.personal.requestmobility.core.room.ResultadoEjecucionSQL
import org.koin.mp.KoinPlatform.getKoin

data class ResultadoSQL(var titulos: List<String> = emptyList(),
                        var filas: List<List<String>> = emptyList()
) {
    companion object {
        fun from(execSQL: ResultadoEjecucionSQL) = ResultadoSQL(
            titulos = execSQL.titulos,
            filas = execSQL.filas
        )

        fun fromSqlToTabla(sql: String): ValoresTabla{
            val trxDao = getKoin().get<AppDatabase>().transaccionesDao()
            val lista =  trxDao.sqlToListString(sql)
            return from(lista).toValoresTabla()
        }



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


                    Celda(valor =   if (contenido.isEmpty()) " - " else contenido, titulo = titulos[indice])
                )
            }
            filasValoresTabla = filasValoresTabla.plus(Fila(celdas = filaVT))
        }

        return ValoresTabla(filas = filasValoresTabla)

    }
}



