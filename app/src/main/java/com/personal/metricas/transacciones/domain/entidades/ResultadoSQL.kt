package com.personal.metricas.transacciones.domain.entidades

import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.unit.dp
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
			//App.log.c("Ejecuta SQL")
			//App.log.v("1: $sql")

sql.trimIndent()
			//val _sql = sql.trimIndent().replace("\r\n", " ").replace("\t", " ").replace(" TRANSACCIONES ", " TRX_TIME ", true)


			val _sql = reemplazarNombreTablaEnSql(sql, "TRANSACCIONES", "TRX_TIME")
			App.log.d("2: $_sql")

			val sqlConReemplazos = Parametros.reemplazar(str = _sql, parametrosKpi = parametrosKpi, parametrosDashboard = parametrosOrigenDatos)
			val trxDao = getKoin().get<AppDatabase>().transaccionesDao()
			val lista = trxDao.sqlToListString(sqlConReemplazos)
			return from(lista).toValoresTabla()
		}
		fun reemplazarNombreTablaEnSql(
			sqlScript: String,
			nombreAntiguo: String,
			nombreNuevo: String
		): String {

			// 1. Escapamos el nombre antiguo por si contiene caracteres
			//    especiales de Regex (como un punto).
			val nombreAntiguoEscapado = Regex.escape(nombreAntiguo)

			// 2. Creamos la Regex.
			// \b significa "límite de palabra" (word boundary).
			// Esto asegura que reemplazamos "usuarios" pero NO "usuarios_backup".
			val regex = Regex("\\b${nombreAntiguoEscapado}\\b")

			// 3. Reemplazamos todas las ocurrencias en el script original.
			return regex.replace(sqlScript, nombreNuevo)
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


				columna.valores = columna.addValor(valor)
				//columna.valores = columna.valores.plus(valor)
				
				filaVT = filaVT.plus(Celda(valor = valor,
										   titulo = titulos[indice],
										   /*size = columna.maximaLongitudDp*/))
			}
			filasValoresTabla = filasValoresTabla.plus(Fila(celdas = filaVT))


		}
		var filasValoresTablaSizes: List<Fila> = emptyList<Fila>()
		filasValoresTabla.forEach { fila ->
			fila.celdas.forEachIndexed{ indice, celda ->
				var columna: Columnas = columnasTabla.elementAt(indice)
				celda.size =  columna.maximaLongitudDp
			}
		}
//		App.log.lista("Coluimnas", columnasTabla)

		return ValoresTabla(filas = filasValoresTabla, columnas = columnasTabla)
		
	}
}



