package com.personal.metricas.paneles.domain.entidades

import androidx.compose.ui.graphics.Color

data class EsquemaColores(val id: Int = 0, val nombre: String = "", val colores: List<Color> = emptyList<Color>()) {
	init {

	}

	enum class Paletas(val valor: Int) {
		NORMAL(0),
		GRISES(1),
		FOSFORITOS(2),
		PERS(3),
		ERRORES(4)
	}


	

	fun get(tipo: Int) = when (tipo) {
		Paletas.NORMAL.valor    -> EsquemaColores(Paletas.NORMAL.valor, "Normal", dameColoresBasicos())
		Paletas.GRISES.valor    -> EsquemaColores(Paletas.GRISES.valor, "Grises", dameTonosGrises())
		Paletas.FOSFORITOS.valor    -> EsquemaColores(Paletas.FOSFORITOS.valor, "Fosforitos", dameColoresFosforitos())
		Paletas.PERS.valor    -> EsquemaColores(Paletas.PERS.valor, "Pers, Azul", dameColoresPersinaAzul())
		Paletas.ERRORES.valor    -> EsquemaColores(Paletas.ERRORES.valor, "Errores", dameColoresErrores())
		else -> EsquemaColores(Paletas.NORMAL.valor, "Normal", dameColoresBasicos())
	}

	fun getColores(tipo: Int) = when (tipo) {
		Paletas.NORMAL.valor    -> dameColoresBasicos()
		Paletas.GRISES.valor    -> dameTonosGrises()
		Paletas.FOSFORITOS.valor    -> dameColoresFosforitos()
		Paletas.PERS.valor    -> dameColoresPersinaAzul()
		Paletas.ERRORES.valor    -> dameColoresErrores()
		else -> dameColoresBasicos()
	}


	fun dameListasDisponibles() =
		listOf<EsquemaColores>(
				   get(Paletas.NORMAL.valor),
				   get(Paletas.GRISES.valor),
				   get(Paletas.FOSFORITOS.valor),
				   get(Paletas.PERS.valor),
				   get(Paletas.ERRORES.valor)
		)


	fun dameColoresPersinaAzul() = listOf<Color>(
		Color(0xFFBBDEFB),
		Color(0xFF4FC3F7),
	)


	fun dameColoresBasicos() = listOf<Color>(
		Color(0xFFFF0000),
		Color(0xFF00FF00),
		Color(0xFF0000FF),
		Color(0xFFFFFF00),
		Color(0xFFFF00FF),
		Color(0xFF00FFFF),
		Color(0xFFFF69B4),
		Color(0xFFFFA500),
		Color(0xFF800080),
		Color(0xFF008000),

		)

	fun dameTonosGrises() = listOf<Color>(
		Color(0xFF000000),  // Negro
		Color(0xFF212121),  // Gris casi negro
		Color(0xFF424242),  // Gris oscuro
		Color(0xFF616161),
		Color(0xFF757575),  // Gris medio
		Color(0xFF9E9E9E),
		Color(0xFFBDBDBD),  // Gris claro
		Color(0xFFE0E0E0),
		Color(0xFFEEEEEE),  // Gris muy claro
		Color(0xFFFFFFFF)   // Blanco
	)

	fun dameColoresFosforitos() // He cambiado el nombre para mayor claridad
			= listOf<Color>(
		// --- Originales ---
		Color(0xFFCCFF00),  // 1. Amarillo Fosforito / Chartreuse
		Color(0xFF39FF14),  // 2. Verde Neón
		Color(0xFFFF00FF),  // 3. Rosa / Magenta Neón
		Color(0xFF00FFFF),  // 4. Cian / Azul Eléctrico
		Color(0xFFFF5F00),  // 5. Naranja Neón
		Color(0xFFBF00FF),  // 6. Púrpura Eléctrico
		Color(0xFFFF007F),  // 7. Rosa Intenso (Rose)

		// --- Añadidos ---
		Color(0xFFFF0000),  // 8. Rojo Puro / Neón
		Color(0xFF00FF7F),  // 9. Verde Primavera
		Color(0xFF007FFF)   // 10. Azul Vibrante (Azure)
	)

	fun dameColoresErrores() // He cambiado el nombre para mayor claridad
			= listOf<Color>(
		Color(0xFFB00020),  // Rojo de error severo (Material Design)
		Color(0xFFD32F2F),  // Rojo oscuro intenso
		Color(0xFFE53935),  // Rojo puro
		Color(0xFFF44336),  // Rojo estándar
		Color(0xFFFF5252),  // Rojo claro para acentos
		Color(0xFFFFAB40),  // Naranja de advertencia
		Color(0xFFFFC107),  // Ámbar para alertas
		Color(0xFFFFD54F),  // Amarillo de advertencia claro
		Color(0xFFFFEB3B),  // Amarillo brillante para notificaciones
		Color(0xFFFFFDE7)   // Amarillo muy pálido para fondos de alerta
	)


	fun dameEsquemaCondiciones() = EsquemaColores(id = 99, nombre = "Todos", colores = dameTodosColores())

	fun dameTodosColores() = listOf<Color>(
		Color(0x00FFFFFF),
		Color(0xFFFF0000),
		Color(0xFF00FF00),
		Color(0xFF0000FF),
		Color(0xFFFFFF00),
		Color(0xFFFF00FF),
		Color(0xFF00FFFF),
		Color(0xFFFF69B4),
		Color(0xFFFFA500),
		Color(0xFF800080),
		Color(0xFF008000),

		)

}





