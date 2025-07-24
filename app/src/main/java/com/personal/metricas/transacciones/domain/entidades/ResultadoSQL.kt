package com.personal.metricas.transacciones.domain.entidades

import com.personal.metricas.App
import com.personal.metricas.core.composables.tabla.Celda
import com.personal.metricas.core.composables.tabla.Columnas
import com.personal.metricas.core.composables.tabla.Fila
import com.personal.metricas.core.composables.tabla.ValoresTabla
import com.personal.metricas.core.room.AppDatabase
import com.personal.metricas.core.room.ResultadoEjecucionSQL
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.kpi.domain.entidades.Kpi
import org.koin.mp.KoinPlatform.getKoin

data class ResultadoSQL(
	var titulos: List<String> = emptyList(),
	var filas: List<List<String>> = emptyList(),
					   ) {
	companion object {
		fun from(execSQL: ResultadoEjecucionSQL) = ResultadoSQL(titulos = execSQL.titulos, filas = execSQL.filas)


		fun fromSqlToTabla(kpi: Kpi): ValoresTabla {
			val sql = kpi.sql
			val parametors = kpi.parametros
			return fromSqlToTabla(sql, parametors)
		}

		fun fromSqlToTabla(sql: String, parametrosKpi: Parametros = Parametros(), parametrosOrigenDatos: Parametros = Parametros()): ValoresTabla {


			//var sqlConReemplazos = sql
			/*parametrosKpi.ps.forEach { parametro ->

				val key = parametro.key
				val parametroOrigenDatos: Parametro? = parametrosOrigenDatos.ps.firstOrNull{it.key.equals(key)}

				var valor = if3 (parametro.valor.isEmpty(), parametro.defecto, parametro.valor)
				if (!parametro.fijo &&  parametroOrigenDatos != null){
					valor  = parametroOrigenDatos.valor
				}

				sqlConReemplazos = sqlConReemplazos.replace("\$$key", "$valor", ignoreCase = true)
			}*/

			val sqlConReemplazos = Parametros.reemplazar(str = sql, parametrosKpi = parametrosKpi, parametrosDashboard = parametrosOrigenDatos)
			val trxDao = getKoin().get<AppDatabase>().transaccionesDao()
			val lista = trxDao.sqlToListString(sqlConReemplazos)
			return from(lista).toValoresTabla()
		}
		
		
	}
	
	fun toValoresTabla(): ValoresTabla {
		
		var filasValoresTabla: List<Fila> = emptyList<Fila>()
		
		var columnasTabla: List<Columnas> = emptyList<Columnas>()
		
		filas.forEach { fila ->
			var filaVT: List<Celda> = emptyList<Celda>()
			fila.forEachIndexed { indice, contenido ->
				val valor = if (contenido.isEmpty()) " - " else contenido
				
				
				var columna: Columnas? = columnasTabla.elementAtOrNull(indice)
				
				
				if (columna == null) {
					columna =
						Columnas(nombre = titulos[indice], posicion = indice, valores = emptyList())
					columnasTabla = columnasTabla.plus(columna)
				}
				
				
				columna.valores = columna.valores.plus(valor)
				
				filaVT = filaVT.plus(Celda(valor = valor, titulo = titulos[indice]))
			}
			filasValoresTabla = filasValoresTabla.plus(Fila(celdas = filaVT))
		}


		return ValoresTabla(filas = filasValoresTabla, columnas = columnasTabla)
		
	}
}



