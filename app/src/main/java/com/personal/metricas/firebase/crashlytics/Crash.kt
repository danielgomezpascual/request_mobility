package com.personal.metricas.firebase.crashlytics

import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * Clase auxiliar para centralizar las interacciones con Firebase Crashlytics.
 *
 * Actúa como un 'wrapper' alrededor de la instancia de Crashlytics,
 * facilitando su uso, testing y manteniendo el código desacoplado de la librería
 * de Firebase.
 *
 * @param crashlytics Instancia de FirebaseCrashlytics que será inyectada.
 */
class Crash(private val crashlytics: FirebaseCrashlytics) {

	/**
	 * Registra un identificador de usuario para asociar los informes de error.
	 * @param userId El identificador único del usuario.
	 */
	fun setUserId(userId: String?) {
		userId?.let { crashlytics.setUserId(it) }
	}


	/**
	 * Registra un valor de clave-valor personalizado para añadir contexto a los informes.
	 * @param key La clave para el dato personalizado.
	 * @param value El valor asociado a la clave.
	 */
	fun setCustomKey(key: String, value: String) {
		crashlytics.setCustomKey(key, value)
	}

	/**
	 * Registra un log de "breadcrumb" para entender los pasos previos a un error.
	 * @param message El mensaje descriptivo del evento.
	 */
	fun log(message: String) {

		//todo: se debe guardar el registro de donde nos encontramos, posicion del fichero que va marcando , un App.log de la app en debug
		crashlytics.log(message)
	}

	/**
	 * Registra una excepción no fatal (controlada) en Crashlytics.
	 * @param throwable La excepción capturada.
	 */
	fun recordException(throwable: Throwable) {
		crashlytics.recordException(throwable)
	}
}