package com.personal.metricas.kpi.ui.entidades

class OcurrenciasSQL {

	// Usamos un MutableMap para poder modificarlo. Es privado para proteger el estado.
	private val _palabras = mutableMapOf<String, Int>()

	fun dameOcurrencias(){

	}

	fun divide(text: String): List<String> {
		// Si el texto está en blanco, no hay palabras que devolver.
		if (text.isBlank()) {
			return emptyList()
		}

		// El regex \s+ coincide con uno o más caracteres de espacio en blanco.
		val whitespaceRegex = Regex("\\s+")

		// trim() elimina espacios al principio/final para evitar strings vacíos en la lista.
		return text.trim().split(whitespaceRegex)
	}
	fun addWords(words: List<String>) {
		words.forEach { word ->
			// getOrPut es una forma eficiente de añadir una clave si no existe
			// o de obtener su valor si ya está presente.
			// En este caso, si la palabra no está, la añade con valor 0 y luego le suma 1.
			// Si ya está, simplemente obtiene su valor actual y le suma 1.

			val v = word.trim()
			val currentCount = _palabras.getOrPut(v) { 0 }
			_palabras[v] = currentCount + 1

			// Una forma aún más idiomática y concisa de hacer lo mismo:
			// _wordCounts[word] = _wordCounts.getOrDefault(word, 0) + 1
		}
	}

	fun getOcurrencias(): List<String> {
		val exclusiones = listOf<String>("=", "-", ",", " ", "", "=")
		val filteredCounts = exclusiones(exclusiones)

		return filteredCounts.entries
			.sortedByDescending { it.value } // <-- CAMBIO AQUÍ
			.map { (word, count) -> "$word"
			} // Ordenamos para que el resultado sea predecible
	}

	fun filtrar(
		filtro: String = "",
	): List<String> =

		getOcurrencias().filter { palabra -> palabra.startsWith(filtro, ignoreCase = true) }


	fun exclusiones(excluir: List<String> = emptyList()): Map<String, Int> {
		if (excluir.isEmpty()) {
			return _palabras.toMap() // Devuelve una copia si no hay nada que filtrar
		}
		val exclusionSet = excluir.toSet() // Más eficiente para búsquedas
		return _palabras.filterKeys { it !in exclusionSet }
	}

	/**
	 * Reinicia el contador, dejando el mapa de palabras vacío.
	 */
	fun clear() {
		_palabras.clear()
	}
}