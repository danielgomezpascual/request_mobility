package com.personal.requestmobility.core.utils
import android.content.Context
import android.content.SharedPreferences

/**
 * Clase para gestionar SharedPreferences de una forma sencilla y centralizada.
 * Sigue las mejores prácticas de Google Android para el uso de SharedPreferences.
 *
 * @property context El contexto de la aplicación, necesario para acceder a SharedPreferences.
 * @constructor Crea una instancia de SharedPreferencesManager.
 */
class SharedPreferencesManager(private val context: Context) {

	// Nombre por defecto del archivo de SharedPreferences.
	// Es una buena práctica tener un nombre constante para tu archivo de preferencias.
	private val PREFS_NAME = "METRICAS_SP"

	// Instancia de SharedPreferences. Se inicializa de forma lazy para optimizar recursos.
	val sharedPreferences: SharedPreferences by lazy {
		context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
	}

	/**
	 * Guarda un valor en SharedPreferences.
	 * Soporta tipos básicos como String, Int, Boolean, Float y Long.
	 *
	 * @param key La clave bajo la cual se almacenará el valor.
	 * @param value El valor a almacenar.
	 * @param T El tipo del valor (inferido automáticamente por Kotlin).
	 */
	fun <T> put(key: String, value: T) {
		val editor = sharedPreferences.edit()
		when (value) {
			is String -> editor.putString(key, value)
			is Int -> editor.putInt(key, value)
			is Boolean -> editor.putBoolean(key, value)
			is Float -> editor.putFloat(key, value)
			is Long -> editor.putLong(key, value)
			else -> throw IllegalArgumentException("Tipo de dato no soportado para SharedPreferences: ${value?.javaClass?.simpleName}")
		}
		editor.apply() // Aplica los cambios de forma asíncrona para no bloquear el hilo principal.
	}

	/**
	 * Recupera un valor de SharedPreferences.
	 * Se debe especificar el tipo esperado para la recuperación.
	 *
	 * @param key La clave del valor a recuperar.
	 * @param defaultValue El valor por defecto a devolver si la clave no se encuentra.
	 * @param T El tipo del valor esperado.
	 * @return El valor recuperado, o defaultValue si la clave no existe.
	 */
	inline fun <reified T> get(key: String, defaultValue: T): T {
		return when (T::class) {
			String::class -> sharedPreferences.getString(key, defaultValue as String) as T
			Int::class -> sharedPreferences.getInt(key, defaultValue as Int) as T
			Boolean::class -> sharedPreferences.getBoolean(key, defaultValue as Boolean) as T
			Float::class -> sharedPreferences.getFloat(key, defaultValue as Float) as T
			Long::class -> sharedPreferences.getLong(key, defaultValue as Long) as T
			else -> throw IllegalArgumentException("Tipo de dato no soportado para SharedPreferences: ${T::class.simpleName}")
		}
	}

	/**
	 * Elimina una clave y su valor asociado de SharedPreferences.
	 *
	 * @param key La clave a eliminar.
	 */
	fun remove(key: String) {
		sharedPreferences.edit().remove(key).apply()
	}

	/**
	 * Limpia todos los valores almacenados en SharedPreferences.
	 */
	fun clear() {
		sharedPreferences.edit().clear().apply()
	}
}