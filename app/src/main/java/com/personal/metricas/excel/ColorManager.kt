package com.personal.metricas.excel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlin.math.roundToInt
object ColorManager {
	fun toHexCode(color: Color): String {
		return String.format("#%06X", color.toArgb() and 0xFFFFFF)
	}

	fun hexToRgbBytes(hexColor: String): ByteArray? {
		// 1. Limpiamos el string, quitando el '#' si existe.
		val cleanHex = hexColor.removePrefix("#")

		// 2. Verificamos que tenga la longitud correcta (6 caracteres para RRGGBB).
		if (cleanHex.length != 6) {
			println("Error: El código hexadecimal '$hexColor' no tiene un formato RRGGBB válido.")
			return null
		}

		try {
			// 3. Extraemos los 3 componentes (RR, GG, BB) y los convertimos a Enteros.
			val red = cleanHex.substring(0, 2).toInt(16)
			val green = cleanHex.substring(2, 4).toInt(16)
			val blue = cleanHex.substring(4, 6).toInt(16)

			// 4. Devolvemos los componentes como un array de bytes.
			return byteArrayOf(red.toByte(), green.toByte(), blue.toByte())

		}
		catch (e: NumberFormatException) {
			println("Error al procesar el código hexadecimal '$hexColor'.")
			return null
		}
	}

	fun toPastelColor(hexColor: String, pastel: Float): String {
		// 1. Convertimos el hexadecimal a un objeto Color de Compose
		val initialColor = Color(android.graphics.Color.parseColor(hexColor))

		// 2. Obtenemos los componentes RGB del color original (valores de 0 a 255)
		val red = initialColor.red * 255
		val green = initialColor.green * 255
		val blue = initialColor.blue * 255

		// 3. Mezclamos cada componente con el blanco (cuyo valor es 255)
		// La fórmula es: nuevoValor = valorOriginal + (255 - valorOriginal) * intensidad
		val newRed = (red + (255 - red) * pastel).roundToInt()
		val newGreen = (green + (255 - green) * pastel).roundToInt()
		val newBlue = (blue + (255 - blue) * pastel).roundToInt()

		// 4. Creamos el nuevo color a partir de los componentes calculados
		val pastelColor = Color(red = newRed, green = newGreen, blue = newBlue)

		// 5. Convertimos el resultado de vuelta a un string hexadecimal RGB de 6 dígitos
		return String.format("#%06X", pastelColor.toArgb() and 0xFFFFFF)
	}
}