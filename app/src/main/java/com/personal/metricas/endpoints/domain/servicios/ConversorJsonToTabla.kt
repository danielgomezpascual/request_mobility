package com.personal.metricas.endpoints.domain.servicios

import androidx.sqlite.db.SupportSQLiteDatabase
import com.personal.metricas.App
import com.personal.metricas.core.room.AppDatabase
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.longOrNull
import org.koin.mp.KoinPlatform
import kotlin.collections.iterator

class ConversorJsonToTabla {

	val appDatabase = KoinPlatform.getKoin().get<AppDatabase>()
	val db: SupportSQLiteDatabase = appDatabase.openHelper.writableDatabase // Usamos readableDatabase para operaciones de lectura




	fun jsonToTabla(str: String, nodoIdentificadorFila: String, tablaAlmacenar: String, vaciarTabla: Boolean): Boolean {
		try {
			mapToTable(tablaAlmacenar, jsonToMap(str, nodoIdentificadorFila), vaciarTabla)
			return true;
		}
		catch (e: Exception) {
			e.printStackTrace()
			return false
		}

	}


	private fun mapToTable(nombreTabla: String, tabla: List<Map<String, Any?>>, vaciarTabla: Boolean) {
		if (tabla.isEmpty()) {
			App.Companion.log.i("Tabla con datos vacia, no se rellena $nombreTabla")
			return
		}
		var columnas: MutableList<String> = mutableListOf()
		tabla.first().forEach { key, value -> columnas.add(key) }

		App.Companion.log.lista("Columnas", columnas)
		//val nombreTabla = "TEST"
		var sqlCreate: String = "CREATE TABLE IF NOT EXISTS $nombreTabla ( " +
								"ID INTEGER NOT NULL "
		columnas.forEach { columna -> sqlCreate += ", $columna TEXT" }
		sqlCreate += ", PRIMARY KEY(ID) "
		sqlCreate += ")"


		var sqlInserts: MutableList<String> = mutableListOf()
		val sqlColumnas = columnas.joinToString(separator = ", ")

		tabla.forEachIndexed { index, row ->
			var sqlValores = ""
			row.forEach { (key, value) ->
				sqlValores += "'${value.toString().replace("'", "''")}',"
			}
			sqlValores = sqlValores.substring(0, sqlValores.length - 1)
			sqlInserts.add(" INSERT OR REPLACE INTO $nombreTabla ($sqlColumnas) VALUES ($sqlValores);")
		}

		runBlocking {
			if (vaciarTabla) db.execSQL("DROP TABLE IF EXISTS $nombreTabla")
			db.execSQL(sqlCreate)
			sqlInserts.forEach { sql -> db.execSQL(sql) }

		}


	}

	private fun jsonToMap(jsonString: String, rowElementKey: String? = null): List<Map<String, Any?>> {
		val json = Json { ignoreUnknownKeys = true }

		try {
			val parsedJson = json.parseToJsonElement(jsonString)
			val rows: List<JsonElement>

			if (rowElementKey != null) {
				// Buscamos el elemento recursivamente
				val foundElement = findJsonElementByKeyRecursively(parsedJson, rowElementKey)

				rows = if (foundElement is JsonArray) {
					foundElement.toList()
				} else if (foundElement is JsonObject) {
					// Si encontramos un objeto, lo tratamos como una única fila
					return listOf(parseJsonObjectToMap(foundElement))
				} else {
					println("Advertencia: La clave '$rowElementKey' no se encontró o no es un array/objeto válido para formar una tabla.")
					emptyList()
				}
			} else {
				// Lógica original si no se proporciona rowElementKey
				if (parsedJson is JsonArray) {
					rows = parsedJson.toList()
				} else if (parsedJson is JsonObject) {
					rows = listOf(parsedJson) // Si es un objeto, lo tratamos como una única fila
				} else {
					return emptyList() // JSON vacío o no válido (e.g., solo un primitivo en la raíz).
				}
			}

			if (rows.isEmpty()) {
				return emptyList()
			}

			val tableData = mutableListOf<Map<String, Any?>>()
			for (rowElement in rows) {
				if (rowElement is JsonObject) {
					tableData.add(parseJsonObjectToMap(rowElement))
				}
			}
			return tableData
		}
		catch (e: Exception) {
			println("Error al parsear o procesar el JSON: ${e.message}")
			return emptyList()
		}
	}

	/**
	 * Busca un JsonElement por su clave de forma recursiva dentro de un árbol JSON.
	 *
	 * @param element El JsonElement actual donde empezar la búsqueda.
	 * @param key La clave a buscar.
	 * @return El JsonElement encontrado, o `null` si la clave no se encuentra.
	 */
	private fun findJsonElementByKeyRecursively(element: JsonElement, key: String): JsonElement? {
		if (element is JsonObject) {
			// Si el objeto actual contiene la clave, la devolvemos
			if (element.containsKey(key)) {
				return element[key]
			}
			// Si no, buscamos en los valores de sus propiedades
			for (propertyValue in element.values) {
				val found = findJsonElementByKeyRecursively(propertyValue, key)
				if (found != null) {
					return found
				}
			}
		} else if (element is JsonArray) {
			// Si es un array, iteramos sobre sus elementos y buscamos en cada uno
			for (arrayElement in element) {
				val found = findJsonElementByKeyRecursively(arrayElement, key)
				if (found != null) {
					return found
				}
			}
		}
		// Si no es un objeto ni un array, o la clave no se encontró, devolvemos null
		return null
	}

	// Función auxiliar para parsear un JsonObject a un Map<String, Any?>
	private fun parseJsonObjectToMap(jsonObject: JsonObject): Map<String, Any?> {
		val rowMap = mutableMapOf<String, Any?>()
		for ((key, value) in jsonObject) {
			rowMap[key] = when (value) {
				is JsonPrimitive -> {
					when {
						value.isString                -> value.content
						value.booleanOrNull != null   -> value.booleanOrNull
						value.longOrNull != null      -> value.longOrNull
						value.doubleOrNull != null    -> value.doubleOrNull
						value.contentOrNull == "null" -> null
						else                          -> value.contentOrNull
					}
				}
				// Si no es un JsonPrimitive (es un JsonObject o JsonArray anidado), lo tratamos como null
				else             -> null
			}
		}
		return rowMap
	}
	/*
		fun jsonToTable(jsonString: String, rowElementKey: String? = null): List<Map<String, Any?>> {
			val json = Json { ignoreUnknownKeys = true }

			try {
				var currentElement: JsonElement = json.parseToJsonElement(jsonString)
				val rows: List<JsonElement>

				if (rowElementKey != null) {
					// Dividimos la clave por puntos para navegar la ruta
					val pathSegments = rowElementKey.split('.')
					var foundElement: JsonElement? = currentElement

					for (segment in pathSegments) {
						if (foundElement is kotlinx.serialization.json.JsonObject) {
							foundElement = foundElement[segment]
							if (foundElement == null) {
								println("Advertencia: Segmento de ruta '$segment' no encontrado en el JSON.")
								return emptyList() // Ruta no encontrada
							}
						} else {
							println("Advertencia: La ruta JSON no es un objeto en el segmento '$segment'.")
							return emptyList() // No es un objeto para seguir la ruta
						}
					}

					// Una vez que hemos navegado la ruta, esperamos que el elemento final sea un JsonArray
					rows = (foundElement as? JsonArray)?.toList() ?: emptyList()
					if (rows.isEmpty() && foundElement != null) {
						// Si el elemento final es un objeto y no un array, tratamos como una única fila
						if (foundElement is kotlinx.serialization.json.JsonObject) {
							return listOf(parseJsonObjectToMap(foundElement))
						}
					}

				} else {
					// Lógica original si no se proporciona rowElementKey
					if (currentElement is JsonArray) {
						rows = currentElement.toList()
					} else if (currentElement is kotlinx.serialization.json.JsonObject) {
						rows = listOf(currentElement) // Si es un objeto, lo tratamos como una única fila
					} else {
						return emptyList() // JSON vacío o no válido (e.g., solo un primitivo en la raíz).
					}
				}

				if (rows.isEmpty()) {
					return emptyList()
				}

				val tableData = mutableListOf<Map<String, Any?>>()
				for (rowElement in rows) {
					if (rowElement is kotlinx.serialization.json.JsonObject) {
						tableData.add(parseJsonObjectToMap(rowElement))
					}
				}
				return tableData
			} catch (e: Exception) {
				println("Error al parsear o procesar el JSON: ${e.message}")
				return emptyList()
			}
		}

		// Función auxiliar para parsear un JsonObject a un Map<String, Any?>
		private fun parseJsonObjectToMap(jsonObject: kotlinx.serialization.json.JsonObject): Map<String, Any?> {
			val rowMap = mutableMapOf<String, Any?>()
			for ((key, value) in jsonObject) {
				rowMap[key] = when (value) {
					is kotlinx.serialization.json.JsonPrimitive -> {
						when {
							value.isString -> value.content
							value.booleanOrNull != null -> value.booleanOrNull
							value.longOrNull != null -> value.longOrNull
							value.doubleOrNull != null -> value.doubleOrNull
							value.contentOrNull == "null" -> null
							else -> value.contentOrNull
						}
					}
					// Si no es un JsonPrimitive (es un JsonObject o JsonArray anidado), lo tratamos como null
					else -> null
				}
			}
			return rowMap
		}*/


}