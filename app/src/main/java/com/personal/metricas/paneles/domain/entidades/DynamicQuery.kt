package com.personal.metricas.paneles.domain.entidades
/**
 * Una clase para construir sentencias SQL dinámicas de forma segura y mantenible.
 * Previene la inyección de SQL al separar la consulta de sus argumentos.
 *
 * Esta versión mejorada maneja correctamente la presencia de cláusulas
 * GROUP BY y ORDER BY, insertando el WHERE en la posición correcta.
 *
 * @param baseQuery La sentencia SQL inicial sin cláusula WHERE.
 */
class DynamicQuery(private val baseQuery: String) {

	private val whereClauses = mutableListOf<String>()
	private val arguments = mutableListOf<Any>()

	/**
	 * Representa una consulta SQL construida, separando la plantilla de los argumentos.
	 *
	 * @property sql La sentencia SQL con placeholders (?).
	 * @property args La lista de argumentos que reemplazarán a los placeholders.
	 */
	data class BuiltQuery(val sql: String, val args: List<Any>)

	/**
	 * Añade una condición a la cláusula WHERE.
	 *
	 * @param condition La condición con un placeholder, ej: "ORGANIZATION_CODE = ?"
	 * @param value El valor que se asociará a ese placeholder.
	 * @return El propio builder para permitir encadenamiento de llamadas (fluent interface).
	 */
	fun addWhere(condition: String, value: Any): DynamicQuery {
		whereClauses.add(condition)
		arguments.add(value)
		return this
	}

	/**
	 * Construye la consulta final.
	 * Inserta la cláusula WHERE antes del GROUP BY o del ORDER BY.
	 *
	 * @return Un objeto [BuiltQuery] con el SQL final y sus argumentos.
	 */
	fun build(): BuiltQuery {
		if (whereClauses.isEmpty()) {
			return BuiltQuery(baseQuery.trimAll(), emptyList())
		}

		val whereStatement = "WHERE ${whereClauses.joinToString(separator = " AND ")}"

		// --- LÓGICA MEJORADA ---
		// Buscamos el punto de inserción correcto. El WHERE va antes de GROUP BY y ORDER BY.
		val groupByIndex = baseQuery.indexOf("GROUP BY", ignoreCase = true)
		val orderByIndex = baseQuery.indexOf("ORDER BY", ignoreCase = true)

		// Creamos una lista con los índices encontrados (-1 si no se encuentra)
		// y filtramos los que no se encontraron.
		val insertionIndex = listOf(groupByIndex, orderByIndex)
			.filter { it != -1 } // Mantenemos solo los índices válidos (> -1)
			.minOrNull()         // Obtenemos el índice más pequeño (el que aparece primero)

		val finalSql = if (insertionIndex != null) {
			// Si encontramos GROUP BY u ORDER BY, insertamos el WHERE justo antes.
			val beforeClause = baseQuery.substring(0, insertionIndex)
			val afterClause = baseQuery.substring(insertionIndex)
			"$beforeClause $whereStatement $afterClause"
		} else {
			// Si no hay ninguna, simplemente lo añadimos al final de la consulta.
			"$baseQuery $whereStatement"
		}

		return BuiltQuery(finalSql.trimAll(), arguments)
	}

	// Pequeña función de extensión para limpiar los espacios extra en la query final.
	private fun String.trimAll() = this.trim().replace("\\s+".toRegex(), " ")
}
/*
// --- Ejemplo de uso ---
fun main() {
	println("--- 💡 EJEMPLO 1: Con GROUP BY y ORDER BY ---")

	val queryWithGroupBy = """
        SELECT
            ORGANIZATION_CODE,
            COUNT(*) as TOTAL
        FROM
            TRANSACCIONES
        GROUP BY
            ORGANIZATION_CODE
        ORDER BY
            TOTAL DESC
    """

	val filteredGroupByQuery = DynamicQuery(queryWithGroupBy)
		.addWhere("ESTADO = ?", "FINALIZADO")
		.build()

	println("SQL Generado:\n${filteredGroupByQuery.sql}")
	println("Argumentos: ${filteredGroupByQuery.args}\n")
	// Resultado esperado: El WHERE se inserta ANTES del GROUP BY.

	println("--- 💡 EJEMPLO 2: Solo con ORDER BY (caso original) ---")

	val queryWithOrderBy = """
        SELECT
            *
        FROM
            TRANSACCIONES
        ORDER BY
            CREATION_DATE DESC
    """

	val filteredOrderByQuery = DynamicQuery(queryWithOrderBy)
		.addWhere("ORGANIZATION_CODE = ?", "ABM")
		.build()

	println("SQL Generado:\n${filteredOrderByQuery.sql}")
	println("Argumentos: ${filteredOrderByQuery.args}")
}*/