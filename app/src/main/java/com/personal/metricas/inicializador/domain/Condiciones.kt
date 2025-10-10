package com.personal.metricas.inicializador.domain

import com.personal.metricas.core.composables.tabla.Columnas
import com.personal.metricas.paneles.domain.entidades.Alarmas
import com.personal.metricas.paneles.domain.entidades.Condiciones
import com.personal.metricas.paneles.domain.entidades.EsquemaColores
import com.personal.metricas.paneles.domain.entidades.FuncionesCondicionesCeldaManager

object CONDICIONES_PANELES {

	val BANDERAS: com.personal.metricas.paneles.domain.entidades.Condiciones = Condiciones(id = 1,
																						   columna = Columnas(nombre = "LECTORA"),
																						   color = EsquemaColores.MUTICOLOR,
																						   condicionCelda = FuncionesCondicionesCeldaManager.BANDERAS,
																						   predicado = "",
																						   descripion = "",
																						   alarma = Alarmas())
	val listaCondicionesBanderas: List<Condiciones> = listOf<Condiciones>(BANDERAS)


	val COND_ESTADO_ERROR: Condiciones = Condiciones(1, Columnas("ESTADO"),
													 condicionCelda = FuncionesCondicionesCeldaManager.SIN_DEFINIR,
													 color = EsquemaColores.PERS,
													 predicado = "== 'ERROR'",
													 descripion = "",
													 alarma = Alarmas())

	val COND_ESTADO_REPROCESAMIENTO: Condiciones = Condiciones(1, Columnas("ESTADO"),
															   condicionCelda = FuncionesCondicionesCeldaManager.SIN_DEFINIR,
															   color = EsquemaColores.PERS_ROJA,
															   predicado = "== 'REPROCESADO'",
															   descripion = "",
															   alarma = Alarmas())

	val listaCondicionesErr = listOf<Condiciones>(COND_ESTADO_ERROR, COND_ESTADO_REPROCESAMIENTO)


	val COND_RATIO_ERRORES: Condiciones = Condiciones(1, Columnas("% ERR"),
													  condicionCelda = FuncionesCondicionesCeldaManager.SIN_DEFINIR,

													  color = EsquemaColores.PERS,
													  predicado = "> 5",
													  descripion = "",
													  alarma = Alarmas())
}