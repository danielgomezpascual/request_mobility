package com.personal.metricas.core.data.repositorio

import com.personal.metricas.core.data.ds.IDS
import com.personal.metricas.core.data.ds.TIPO_DS

abstract class BaseRepositorio<T : IDS>(protected val fuentesDatos: List<IDS>) {

    open fun dameDS(tipo: TIPO_DS): T {
        fuentesDatos.forEach { ds ->
            if (ds.tipo == tipo) {
                return ds as T
            }
        }
        return fuentesDatos.first() as T
    }


}