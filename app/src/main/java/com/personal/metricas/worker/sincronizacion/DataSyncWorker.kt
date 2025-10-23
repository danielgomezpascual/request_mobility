package com.personal.metricas.worker.sincronizacion

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.personal.metricas.App
import com.personal.metricas.core.utils.K
import com.personal.metricas.core.utils.Preferencias
import com.personal.metricas.core.utils.TiempoHora
import com.personal.metricas.organizaciones.domain.entidades.Organizaciones

import com.personal.metricas.organizaciones.domain.interactors.ObtenerOrganizacionesPlanificacionCU
import com.personal.metricas.sincronizacion.domain.interactors.RealizarSincronizacionCU
import com.personal.metricas.sincronizacion.domain.interactors.TIPO_SINCRONIZACION
import java.time.LocalTime
import java.time.format.DateTimeParseException


//import org.koin.java.KoinJavaComponent.getKoin


class DataSyncWorker(
	appContext: Context,
	workerParams: WorkerParameters,
	private val realizarSincronizacionCU: RealizarSincronizacionCU,
	private val obtenerPlanificacionOrganizacion: ObtenerOrganizacionesPlanificacionCU,
) : CoroutineWorker(appContext, workerParams) {

	// El trabajo a realizar se define aquí dentro.
	// Esta función se ejecuta en un hilo de fondo por defecto.
	override suspend fun doWork(): Result {
		return try {
			App.log.d("Inicio del doWork del worker...")

			val sincroAuto = App.sharedPrerfences.get(Preferencias.SINCRONIZAR_AUTO, false)

			App.log.d("Sincro Auto $sincroAuto")
			if (sincroAuto) {




				val organizaciones: List<Organizaciones> = obtenerPlanificacionOrganizacion.damePlanificaciones()

				organizaciones.forEach { organizacion ->
					//si esta activo y la hora esta dentro, REALIZAMOS LA SINCRONIZACION
				//	App.log.d("Organizacion ${organizacion.organizationCode} : ${organizacion.horas}")

					if (organizacion.activo && horasEnPeriodo(organizacion.horas)) {
						//App.log.v("SINCRONIZANDO ${organizacion.organizationCode}")
						realizarSincronizacionCU.sincronizarOrganizacion(organizacion, TIPO_SINCRONIZACION.PLANIFICADA)
						//App.log.d("Organizacion Sincronziada... $organizacion")
					}

				}

/*
				val organizacionesStr: String = App.sharedPrerfences.get<String>(K.ORGANIZACIONES, "")
				val organizacionesSeleccionadasPrevias: List<String> = organizacionesStr.split(";")
*/

				/*val realizarSincronizacionCU: RealizarSincronizacionCU = getKoin().get()
				val obtenerOrganizacion: ObtenerOrganizacionesCU = getKoin().get()*/



				App.sharedPrerfences.put(K.ULTIMA_SINCRONIZACION, TiempoHora.ahora())

			}

			/*
						val organizacionesStr: String = App.sharedPrerfences.get<String>(K.ORGANIZACIONES, "")
						val organizacionesSeleccionadasPrevias: List<String> = organizacionesStr.split(";")


						/*val realizarSincronizacionCU: RealizarSincronizacionCU = getKoin().get()
						val obtenerOrganizacion: ObtenerOrganizacionesCU = getKoin().get()*/

						val organizaciones: List<Organizaciones> = obtenerOrganizacion.getAll()

						App.sharedPrerfences.put(K.ULTIMA_SINCRONIZACION, TiempoHora.ahora())
			*/
			/*
			con este metodo se sincronizacban todas las seleccionadas.
			organizacionesSeleccionadasPrevias.forEach { organizacion ->
				if (organizacion.isNotEmpty()){
					val org : Organizaciones? = organizaciones.filter { it.organizationId.equals(organizacion) }.firstOrNull()
					if (org!= null){
						realizarSincronizacionCU.sincronizarOrganizacion(org)
						App.log.d("Organizacion Sincronziada... $organizacion")
					}
				}

			}*/


			// ----------------------------------------------------
			// AQUÍ VA TU LÓGICA
			// Por ejemplo: Sincronizar datos con tu backend,
			// limpiar la caché, etc.
			// ----------------------------------------------------
			App.log.d("DataSyncWorke Trabajo periódico ejecutado con éxito.")

			// Indica que el trabajo ha terminado correctamente.
			Result.success()
		}
		catch (e: Exception) {
			App.log.e("DataSyncWorker Error durante la ejecución del trabajo")

			// Indica que el trabajo ha fallado y no debe reintentarse.
			// Puedes usar Result.retry() si quieres que WorkManager lo intente de nuevo más tarde.
			Result.failure()
		}
	}


	fun horasEnPeriodo(horas: String): Boolean {
		// 1. Define el rango de tiempo
		val now = LocalTime.now()
		val thirtyMinutesAgo = now.minusMinutes(30)

		// 2. Procesa el String de entrada y comprueba
		return horas
			.split(';') // Divide el string en una lista de horas: ["15:30", "14:30", ...]
			.filter { it.isNotBlank() } // Elimina elementos vacíos si hay un ";" al final
			.any { hourStr ->
				// La función 'any' devuelve true en cuanto una de las comprobaciones sea exitosa
				try {
					// Convierte el string "HH:mm" a un objeto LocalTime
					val timeToCheck = LocalTime.parse(hourStr)

					// La lógica clave: ¿La hora está DESPUÉS de hace 30 min Y ANTES de ahora?
					timeToCheck.isAfter(thirtyMinutesAgo) && timeToCheck.isBefore(now)
				}
				catch (e: DateTimeParseException) {
					// Si una hora está mal formateada, la ignoramos y continuamos
					false
				}
			}
	}

	// Usamos un companion object para las constantes, siguiendo las guías de estilo.
	companion object {
		const val UNIQUE_WORK_NAME = "data_sync_worker"
	}
}