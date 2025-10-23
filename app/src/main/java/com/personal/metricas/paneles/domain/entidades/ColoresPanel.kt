package com.personal.metricas.paneles.domain.entidades

import androidx.compose.ui.graphics.Color

data class EsquemaColores(val id: Int = 0, val nombre: String = "", val colores: List<Color> = emptyList<Color>()) {

	init {

	}


	companion object{
		val MUTICOLOR: Int = 0
		val GRISES: Int = 1
		val FOSFORITOS: Int = 2
		val PERS: Int = 3
		val ERRORES: Int = 4
		val PERS_ROJA: Int = 5
		val PERS_VERDE: Int = 6
		val PERS_AMARILLA: Int = 7
		val FIJO: Int = 8
	}


	enum class Paletas(val valor: Int) {
		MULTICOLOR(0),
		GRISES(1),
		FOSFORITOS(2),
		PERS(3),
		ERRORES(4),
		PERS_ROJA(5),
		PERS_VERDE(6),
		PERS_AMARILLA(7),
		FIJO(8),
	}


	
	fun getFijo(colores: List<Color> =dameColoresFijo()) = get(Paletas.FIJO.valor, colores)

	fun get(tipo: Int, colores: List<Color> = dameColoresFijo()) = when (tipo) {
		Paletas.MULTICOLOR.valor -> EsquemaColores(Paletas.MULTICOLOR.valor, "Normal", dameColoresBasicos())
		Paletas.GRISES.valor     -> EsquemaColores(Paletas.GRISES.valor, "Grises", dameTonosGrises())
		Paletas.FOSFORITOS.valor    -> EsquemaColores(Paletas.FOSFORITOS.valor, "Fosforitos", dameColoresFosforitos())
		Paletas.PERS.valor    -> EsquemaColores(Paletas.PERS.valor, "Pers, Azul", dameColoresPersinaAzul())
		Paletas.PERS_ROJA.valor    -> EsquemaColores(Paletas.PERS_ROJA.valor, "Pers, Roja", dameColoresPersinaRoja())
		Paletas.PERS_VERDE.valor    -> EsquemaColores(Paletas.PERS_VERDE.valor, "Pers Verde", dameColoresPersinaVerde())
		Paletas.PERS_AMARILLA.valor    -> EsquemaColores(Paletas.PERS_AMARILLA.valor, "Pers Amarilla", dameColoresPersinaAmarilla())
		Paletas.ERRORES.valor    -> EsquemaColores(Paletas.ERRORES.valor, "Errores", dameColoresErrores())
		Paletas.FIJO.valor    -> EsquemaColores(Paletas.FIJO.valor, "Fijo", colores)
		else                     -> EsquemaColores(Paletas.MULTICOLOR.valor, "Normal", dameColoresBasicos())
	}

	fun getColores(tipo: Int) = when (tipo) {
		Paletas.MULTICOLOR.valor -> dameColoresBasicos()
		Paletas.GRISES.valor     -> dameTonosGrises()
		Paletas.FOSFORITOS.valor    -> dameColoresFosforitos()
		Paletas.PERS.valor    -> dameColoresPersinaAzul()
		Paletas.PERS_ROJA.valor    -> dameColoresPersinaRoja()
		Paletas.PERS_VERDE.valor    -> dameColoresPersinaVerde()
		Paletas.PERS_AMARILLA.valor    -> dameColoresPersinaAmarilla()
		Paletas.ERRORES.valor    -> dameColoresErrores()
		Paletas.FIJO.valor    -> dameColoresFijo()
		else -> dameColoresBasicos()
	}




	fun dameListasDisponibles() =
		listOf<EsquemaColores>(
			get(Paletas.MULTICOLOR.valor),
			get(Paletas.GRISES.valor),
			get(Paletas.FOSFORITOS.valor),
			get(Paletas.PERS.valor),
			get(Paletas.PERS_ROJA.valor),
			get(Paletas.PERS_VERDE.valor),
			get(Paletas.PERS_AMARILLA.valor),
			get(Paletas.ERRORES.valor),
			get(Paletas.FIJO.valor)
		)


	private fun dameColoresFijo()= listOf<Color>(Color.Red)

	private fun dameColoresPersinaAzul() = listOf<Color>(
		Color(0xFFBBDEFB),
		Color(0xFF4FC3F7),
	)

	private fun dameColoresPersinaRoja() = listOf<Color>(
		Color(0xFFFCE4EC),
		Color(0xFFEF9A9A),
	)


	private fun dameColoresPersinaVerde() = listOf<Color>(
		Color(0xFFE8F5E9),
		Color(0xFFA5D6A7),
	)


	private 	fun dameColoresPersinaAmarilla() = listOf<Color>(
		Color(0xFFFFFDE7),
		Color(0xFFFFE082),
	)

	private 	fun dameColoresBasicos() = listOf<Color>(
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

	private 	fun dameTonosGrises() = listOf<Color>(
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

		Color(0x00FFFFFF), // Transparente
		Color(0xFF0A0A0A), // Casi Negro
		Color(0xFFFFFFFF), // Blanco
		Color(0xFFFF0000), // Rojo
		Color(0xFF00FF00), // Lima
		Color(0xFF0000FF), // Azul
		Color(0xFFFFFF00), // Amarillo
		Color(0xFFFF00FF), // Fucsia
		Color(0xFF00FFFF), // Cian
		Color(0xFFFF69B4), // Rosa Fuerte
		Color(0xFFFFA500), // Naranja
		Color(0xFF800080), // Púrpura
		Color(0xFF008000), // Verde

		// --- Expansión a 100 colores ---

		// Tonos de Rojo y Rosa
		Color(0xFFDC143C), // Rojo Carmesí
		Color(0xFFFFC0CB), // Rosa
		Color(0xFFFFB6C1), // Rosa Claro
		Color(0xFFDB7093), // Rosa Viejo
		Color(0xFFCD5C5C), // Coral Indio
		Color(0xFFF08080), // Coral Claro
		Color(0xFFFA8072), // Salmón
		Color(0xFFE9967A), // Salmón Oscuro

		// Tonos de Naranja
		Color(0xFFFF7F50), // Coral
		Color(0xFFFF8C00), // Naranja Oscuro
		Color(0xFFED9121), // Zanahoria

		// Tonos de Amarillo
		Color(0xFFFFFACD), // Limón Gasa
		Color(0xFFF0E68C), // Caqui
		Color(0xFFBDB76B), // Caqui Oscuro
		Color(0xFFDAA520), // Vara de Oro

		// Tonos de Verde
		Color(0xFFADFF2F), // Verde Amarillo
		Color(0xFF7FFF00), // Verde Cartujo
		Color(0xFF7CFC00), // Verde Césped
		Color(0xFF32CD32), // Verde Lima
		Color(0xFF98FB98), // Verde Pálido
		Color(0xFF8FBC8F), // Verde Mar Oscuro
		Color(0xFF2E8B57), // Verde Mar
		Color(0xFF3CB371), // Verde Mar Medio
		Color(0xFF20B2AA), // Aguamarina Claro
		Color(0xFF008B8B), // Cian Oscuro
		Color(0xFF00CED1), // Turquesa Oscuro

		// Tonos de Cian y Azul
		Color(0xFFAFEEEE), // Turquesa Pálido
		Color(0xFF7FFFD4), // Aguamarina
		Color(0xFF40E0D0), // Turquesa
		Color(0xFF48D1CC), // Turquesa Medio
		Color(0xFFB0C4DE), // Azul Acero Claro
		Color(0xFFADD8E6), // Azul Claro
		Color(0xFF87CEEB), // Azul Cielo
		Color(0xFF87CEFA), // Azul Cielo Claro
		Color(0xFF00BFFF), // Azul Cielo Profundo
		Color(0xFF1E90FF), // Azul Dodher
		Color(0xFF6495ED), // Azul Aciano
		Color(0xFF4169E1), // Azul Real
		Color(0xFF0000CD), // Azul Medio
		Color(0xFF00008B), // Azul Oscuro
		Color(0xFF000080), // Azul Marino
		Color(0xFF191970), // Azul Medianoche

		// Tonos de Púrpura, Violeta y Magenta
		Color(0xFF8A2BE2), // Azul Violeta
		Color(0xFF9400D3), // Violeta Oscuro
		Color(0xFF9932CC), // Orquídea Oscuro
		Color(0xFFBA55D3), // Orquídea Medio
		Color(0xFFDA70D6), // Orquídea
		Color(0xFFEE82EE), // Violeta
		Color(0xFFDDA0DD), // Ciruela
		Color(0xFFD8BFD8), // Cardo
		Color(0xFF4B0082), // Índigo
		Color(0xFF6A5ACD), // Azul Pizarra
		Color(0xFF7B68EE), // Azul Pizarra Medio
		Color(0xFFC71585), // Violeta Rojo Medio

		// Tonos Marrones
		Color(0xFFFFF8DC), // Maíz
		Color(0xFFDEB887), // Madera Rústica
		Color(0xFFD2B48C), // Canela
		Color(0xFFBC8F8F), // Marrón Rosado
		Color(0xFFF4A460), // Marrón Arenoso
		Color(0xFFD2691E), // Chocolate
		Color(0xFFCD853F), // Perú
		Color(0xFF8B4513), // Silla de Montar
		Color(0xFFA0522D), // Siena
		Color(0xFFA52A2A), // Marrón
		// Color(0xFF800000), // Granate - Este ya estaba implícitamente, pero lo añadimos explícito
		Color(0xFF800000), // Granate

		// Tonos de Blanco y Grises
		Color(0xFFF5F5F5), // Blanco Humo
		Color(0xFFDCDCDC), // Gris Gacela
		Color(0xFFD3D3D3), // Gris Claro
		Color(0xFFC0C0C0), // Plata
		Color(0xFFA9A9A9), // Gris Oscuro
		Color(0xFF808080), // Gris
		Color(0xFF696969), // Gris Ténue
		Color(0xFF778899), // Gris Pizarra Claro
		Color(0xFF708090), // Gris Pizarra
		Color(0xFF2F4F4F), // Gris Pizarra Oscuro

		// Colores Adicionales para completar
		Color(0xFFFFD700), // Oro
		Color(0xFFB8860B), // Vara de Oro Oscuro
		Color(0xFFF5DEB3), // Trigo
		Color(0xFF9ACD32), // Verde Amarillo
		Color(0xFF556B2F), // Verde Oliva Oscuro
		Color(0xFF6B8E23), // Verde Oliva
		Color(0xFF4682B4), // Azul Acero
		Color(0xFFB0E0E6), // Azul Pólvora
		Color(0xFFFFE4E1), // Rosa Niebla
		Color(0xFFFAEBD7), // Blanco Antiguo
		Color(0xFFFDF5E6), // Lino Viejo
		Color(0xFFFFEFD5), // Papaya Látigo
		Color(0xFFFFDAB9), // Melocotón
		Color(0xFFEEE8AA)  // Vara de Oro Pálido

		)

}





