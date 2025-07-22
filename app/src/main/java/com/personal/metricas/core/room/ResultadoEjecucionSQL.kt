package com.personal.metricas.core.room

data class ResultadoEjecucionSQL(var titulos: List<String> = emptyList(),
                                 var filas: List<List<String>> = emptyList()
                                 )
