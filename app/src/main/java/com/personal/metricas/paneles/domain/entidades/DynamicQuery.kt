package com.personal.metricas.paneles.domain.entidades

/**
 * Una clase para construir sentencias SQL dinámicas de forma segura y mantenible.
 * Previene la inyección de SQL al separar la consulta de sus argumentos.
 *
 * Esta versión mejorada maneja correctamente la presencia de cláusulas
 * WHERE, GROUP BY y ORDER BY.
 *
 * @param baseQuery La sentencia SQL inicial. Puede o no tener una cláusula WHERE.
 */
class DynamicQuery(private val baseQuery: String) {

	private val whereClauses = mutableListOf<String>()
	private val arguments = mutableListOf<Any>()

	data class BuiltQuery(val sql: String, val args: List<Any>)

	/**
	 * Añade una condición a la cláusula WHERE.
	 *
	 * @param condition La condición con un placeholder, ej: "ORGANIZATION_CODE = ?"
	 * @param value El valor que se asociará a ese placeholder.
	 * @return El propio builder para permitir encadenamiento de llamadas.
	 */
	fun addWhere(condition: String, value: Any): DynamicQuery {
		whereClauses.add(condition)
		arguments.add(value)
		return this
	}

	/**
	 * Construye la consulta final.
	 * Inserta las nuevas cláusulas antes del GROUP BY o del ORDER BY.
	 *
	 * @return Un objeto [BuiltQuery] con el SQL final y sus argumentos.
	 */
	fun build(): BuiltQuery {
		val trimmedBaseQuery = baseQuery.trimAll()

		if (whereClauses.isEmpty()) {
			return BuiltQuery(trimmedBaseQuery, arguments)
		}

		// 1. Determinar si la consulta base ya tiene un WHERE
		val hasExistingWhere = trimmedBaseQuery.contains("WHERE", ignoreCase = true)

		// 2. Construir el prefijo y la declaración de condiciones
		val whereConditions = whereClauses.joinToString(separator = " AND ")

		// Si ya existe un WHERE, las nuevas condiciones se unen con AND.
		// Si no existe, se añade la palabra WHERE seguida de las condiciones.
		val statementToInsert = if (hasExistingWhere) {
			// El primer WHERE solo usa AND porque la baseQuery ya tiene WHERE.
			" AND $whereConditions"
		} else {
			// Si no hay WHERE, añadimos el WHERE completo.
			" WHERE $whereConditions"
		}

		// 3. Buscar el punto de inserción (GROUP BY o ORDER BY)
		val groupByIndex = trimmedBaseQuery.indexOf("GROUP BY", ignoreCase = true)
		val orderByIndex = trimmedBaseQuery.indexOf("ORDER BY", ignoreCase = true)

		// Obtenemos el índice más pequeño y válido (> -1)
		val insertionIndex = listOf(groupByIndex, orderByIndex)
			.filter { it != -1 }
			.minOrNull()

		// 4. Construir el SQL final
		val finalSql = if (insertionIndex != null) {
			// Si encontramos GROUP BY u ORDER BY, insertamos la nueva cláusula justo antes.
			val beforeClause = trimmedBaseQuery.substring(0, insertionIndex)
			val afterClause = trimmedBaseQuery.substring(insertionIndex)
			"$beforeClause$statementToInsert $afterClause"
		} else {
			// Si no hay GROUP BY ni ORDER BY, lo añadimos al final.
			"$trimmedBaseQuery$statementToInsert"
		}

		return BuiltQuery(finalSql.trimAll(), arguments)
	}

	// Pequeña función de extensión para limpiar los espacios extra en la query final.
	private fun String.trimAll() = this.trim().replace("\\s+".toRegex(), " ")
}