package com.personal.metricas.paneles.domain.entidades

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import com.personal.metricas.App
import com.personal.metricas.core.composables.tabla.Celda
import com.personal.metricas.core.composables.tabla.Columnas
import com.personal.metricas.core.composables.tabla.Fila
import com.personal.metricas.notas.domain.NotasManager
import com.personal.metricas.core.composables.tabla.ValoresTabla
import com.personal.metricas.core.notificaciones.NotificacionesManager
import com.personal.metricas.core.utils.Parametro
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.core.utils.esNumerico
import com.personal.metricas.core.utils.getAppName
import com.personal.metricas.core.utils.if3
import com.personal.metricas.endpoints.domain.entidades.EndPoint
import com.personal.metricas.paneles.ui.entidades.PanelUI
import com.personal.metricas.paneles.ui.entidades.toPanel
import com.personal.metricas.transacciones.domain.entidades.ResultadoSQL
import org.apache.commons.jexl3.JexlBuilder
import org.apache.commons.jexl3.MapContext


data class PanelData(

	val panel: Panel,
	val panelConfiguracion: PanelConfiguracion = PanelConfiguracion(),
	val parametrosOrigenDatos: Parametros = Parametros(),
	var valoresTabla: ValoresTabla = ValoresTabla(),
	var endPoint: EndPoint = EndPoint(),
	var notasManager: NotasManager = NotasManager(),
	var notificacionesManager: NotificacionesManager = NotificacionesManager(),
	var listaAlarmas: MutableList<Alarmas> = mutableListOf<Alarmas>(),

	) {

	companion object {

		fun fromPanelUI(panelUI: PanelUI,
						notasManager: NotasManager,
						parametrosOrigenDatos: Parametros): PanelData {

			val panelConfiguracion = panelUI.configuracion

			//se reeemplazxan los parametors del dashboards en la sql
			var tabla: ValoresTabla = ValoresTabla()
			var endPoint = panelUI.endPoint
			var nEndPoint: EndPoint = EndPoint()


			var pui: PanelUI = panelUI
			when (panelUI.tipoPanel) {
				TiposPanel.PANEL_TEXTO     -> {

				}

				TiposPanel.PANEL_CONECTOR -> {

					tabla = ResultadoSQL.fromSqlToTabla(sql = pui.kpi.sql,
														parametrosKpi = pui.kpi.parametros,
														parametrosOrigenDatos = parametrosOrigenDatos)
				}

				TiposPanel.PANEL_END_POINT -> {

					var listaParametrosReemplazados: MutableList<Parametro> = mutableListOf<Parametro>()
					endPoint.parametros.ps.forEach { parametro ->
						val s: String = Parametros.reemplazar(parametro.valor, endPoint.parametros, parametrosOrigenDatos)
						val np: Parametro = parametro.copy(valor = s)
						listaParametrosReemplazados.add(np)
					}

					val p: Parametros = Parametros(listaParametrosReemplazados)
					nEndPoint = endPoint.copy(parametros = p)
					pui = pui.copy(endPoint = endPoint.copy(parametros = p))
				}

				TiposPanel.PANEL_KPI       -> {

					tabla = ResultadoSQL.fromSqlToTabla(sql = pui.kpi.sql,
														parametrosKpi = pui.kpi.parametros,
														parametrosOrigenDatos = parametrosOrigenDatos)
				}

			}


			val titulo: String = Parametros.reemplazar(pui.titulo, endPoint.parametros, parametrosOrigenDatos)
			val descripcion: String = Parametros.reemplazar(pui.descripcion, endPoint.parametros, parametrosOrigenDatos)


			return PanelData(
				panel = pui.copy(titulo = titulo, descripcion = descripcion).toPanel(),
				parametrosOrigenDatos = parametrosOrigenDatos,
				panelConfiguracion = panelConfiguracion,
				valoresTabla = tabla,
				endPoint = nEndPoint,
				notasManager = notasManager

			)
		}
	}

	fun dameIdentificador(): String {
		val idPanel = "P${panel.id.toString()}"
		val id = when (panel.tipoPanel) {
			TiposPanel.PANEL_END_POINT -> "E${panel.endPoint.id}"
			TiposPanel.PANEL_KPI       -> "K${panel.kpi.id}"
			TiposPanel.PANEL_TEXTO    -> "T${panel.kpi.id}"
			TiposPanel.PANEL_CONECTOR -> "C${panel.conector.identificador}"
		}

		return "$idPanel.$id"
	}

	fun ordenarElementos() = valoresTabla.dameElementosOrdenados(campoOrdenacionTabla = panelConfiguracion.columnaY)

	fun limiteElementos(): List<Fila> = valoresTabla.dameElementosTruncados(panelConfiguracion)

	fun aplicarCondicionesFilas(): List<Fila> {


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

					

					if (valor.isNotEmpty() ) {
						val contexto = MapContext().apply {
							if (valor.esNumerico()) {
								set("valor", valor.toFloat())
							} else {
								set("valor", valor)
							}

						}
						val resultado: Any = expresion.evaluate(contexto)
						if ((condicion.condicionCelda > 0 && condicion.predicado.isEmpty()) || (resultado is Boolean && resultado)) {
							color = EsquemaColores().dameEsquemaCondiciones().colores.get(condicion.color)
							gestionAlarma(condicion, fila)
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

					try {


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
						if ((condicionCelda.condicionCelda > 0 && condicionCelda.predicado.isEmpty()) || (resultadoCondicion is Boolean && resultadoCondicion)) {
							val colorCondicion = EsquemaColores().dameEsquemaCondiciones().colores.get(condicionCelda.color)
							gestionAlarma(condicionCelda, fila)
							/*	if (condicionCelda.alarma.activa){

									val alm = condicionCelda.alarma.copy(color = condicionCelda.color, titulo =  "${getAppName(App.context)} | ${condicionCelda.alarma.titulo}", texto =notificacionesManager.dameTexto(condicionCelda.alarma.texto, fila) )
									listaAlarmas.add(alm)
									notificacionesManager.showNotificacion(context = App.context,
																		   alarma = alm
																		 )
								}*/

							nuevasCeldas = nuevasCeldas.mapIndexed { indice, celda ->
								if (indice == posicionEvaluar) {
									val celdaConCondicion: FuncionesCondicionCelda = funcionesCondicionesCelda.aplicarCondicion(valor, condicionCelda, valoresTabla.columnas.get(indice))
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
					}
					catch (e: Exception) {
						e.printStackTrace()
						// nuevasCeldas = nuevasCeldas
					}


				}
				fila.copy(celdas = nuevasCeldas)


				/*   fila.celdas
				   fila.copy(color = color)*/
			}
		}



		return valoresTabla.filas
	}

	private fun gestionAlarma(condicion: Condiciones, fila: Fila) {
		if (condicion.alarma.activa) {
			val id = if3(condicion.alarma.unica, "00${panel.id}00${panel.kpi.id}00${condicion.id}", System.currentTimeMillis().toString())
			val alm = condicion.alarma.copy(id = id, color = condicion.color, titulo = "${getAppName(App.context)} | ${condicion.alarma.titulo}", texto = notificacionesManager.dameTexto(condicion.alarma.texto, fila))
			if (listaAlarmas.filter { it.id.equals(id) }.size == 0) {
				listaAlarmas.add(alm)
				notificacionesManager.showNotificacion(context = App.context,
													   alarma = alm)
			}
		}
	}
}


