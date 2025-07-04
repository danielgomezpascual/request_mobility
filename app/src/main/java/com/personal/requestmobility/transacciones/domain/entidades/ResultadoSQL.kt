package com.personal.requestmobility.transacciones.domain.entidades

import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.tabla.Celda
import com.personal.requestmobility.core.composables.tabla.Columnas
import com.personal.requestmobility.core.composables.tabla.Fila
import com.personal.requestmobility.core.composables.tabla.ValoresTabla
import com.personal.requestmobility.core.room.AppDatabase
import com.personal.requestmobility.core.room.ResultadoEjecucionSQL
import org.koin.mp.KoinPlatform.getKoin

data class ResultadoSQL(
	var titulos: List<String> = emptyList(),
	var filas: List<List<String>> = emptyList(),
					   ) {
	companion object {
		fun from(execSQL: ResultadoEjecucionSQL) = ResultadoSQL(titulos = execSQL.titulos,
				filas = execSQL.filas)
		
		fun fromSqlToTabla(sql: String): ValoresTabla {
			val trxDao = getKoin().get<AppDatabase>().transaccionesDao()
			val lista = trxDao.sqlToListString(sql)
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
		
		
		
		columnasTabla.forEach {  c ->
			App.log.lista("Columnas ${c.copy()}", c.valores)
			
		}
		
		return ValoresTabla(filas = filasValoresTabla, columnas = columnasTabla)
		
	}
}



