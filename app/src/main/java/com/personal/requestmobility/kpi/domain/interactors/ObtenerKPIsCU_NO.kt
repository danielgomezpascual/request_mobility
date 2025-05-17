package com.personal.requestmobility.kpi.domain.interactors

import com.personal.requestmobility.kpi.domain.entidades.Kpi
import com.personal.requestmobility.transacciones.domain.entidades.ResultadoSQL
import com.personal.requestmobility.transacciones.domain.repositorios.IRepoTransacciones
import kotlin.collections.plus

class ObtenerKPIsCU_NO(private val repoTransacciones: IRepoTransacciones) {

    suspend fun obtenerListaKPIs(): List<Kpi> {
        var listaKpi: List<Kpi> = emptyList()

        listaKpi = listaKpi.plus(dameEjemploKPI_0())
        listaKpi = listaKpi.plus(dameEjemploKPI_1())
        listaKpi = listaKpi.plus(dameEjemploKPI_2())
        listaKpi = listaKpi.plus(dameEjemploKPI_3())
        listaKpi = listaKpi.plus(dameEjemploKPI_4())
        return listaKpi
    }


    private suspend fun dameEjemploKPI_0(): Kpi {
        val kpi = Kpi(
            id = 0,
            titulo = "TRX",
            sql = "SELECT MOB_REQUEST_ID,  CREATION_DATE,  REQ_STATUS, LECTORA_ID FROM TRANSACCIONES ORDER BY MOB_REQUEST_ID",
            // sql = "SELECT * FROM TRANSACCIONES ORDER BY MOB_REQUEST_ID",

        )
        //kpi.resultadoSQL = ResultadoSQL.Companion.from(repoTransacciones.ejecutarSQL(kpi.sql))
        return kpi
    }

    private suspend fun dameEjemploKPI_1(): Kpi {
        val kpi = Kpi(
            id = 1,
            titulo = "Transacciones realizadas (01)",
            sql = "SELECT TIPO_MOV, COUNT(MOB_REQUEST_ID) FROM transacciones GROUP BY TIPO_MOV ORDER BY TIPO_MOV",

            )
     //   kpi.resultadoSQL = ResultadoSQL.Companion.from(repoTransacciones.ejecutarSQL(kpi.sql))
        return kpi
    }

    private suspend fun dameEjemploKPI_2(): Kpi {
        val kpi = Kpi(
            id = 2,
            titulo = "Transacciones erroreneas (02)",
            sql = "SELECT TIPO_MOV, COUNT(MOB_REQUEST_ID) FROM Transacciones WHERE REQ_STATUS = 2  GROUP BY TIPO_MOV ",

            )
      //  kpi.resultadoSQL = ResultadoSQL.Companion.from(repoTransacciones.ejecutarSQL(kpi.sql))
        return kpi
    }

    private suspend fun dameEjemploKPI_3(): Kpi {
        val kpi = Kpi(
            id = 3,
            titulo = "Version (03)",
            sql = "SELECT PROGRAM_VERSION, COUNT(MOB_REQUEST_ID) FROM Transacciones  GROUP BY PROGRAM_VERSION ",

            )
     //   kpi.resultadoSQL = ResultadoSQL.Companion.from(repoTransacciones.ejecutarSQL(kpi.sql))
        return kpi
    }

    private suspend fun dameEjemploKPI_4(): Kpi {
        val kpi = Kpi(
            id = 4,
            titulo = "Organizacion (04)",
            sql = " SELECT SUBSTR(LECTORA_ID,0,4) , COUNT (1) from transacciones WHERE REQ_STATUS = 0  GROUP BY SUBSTR(LECTORA_ID,0,4)  ORDER BY 2 DESC ",

            )
      //  kpi.resultadoSQL = ResultadoSQL.Companion.from(repoTransacciones.ejecutarSQL(kpi.sql))
        return kpi
    }


}