package com.personal.metricas.worker.sincronizacion

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.personal.metricas.App
import com.personal.metricas.core.utils.K
import com.personal.metricas.core.utils.TiempoHora
import com.personal.metricas.organizaciones.domain.entidades.Organizaciones
import com.personal.metricas.organizaciones.domain.interactors.ObtenerOrganizacionesCU
import com.personal.metricas.sincronizacion.domain.interactors.RealizarSincronizacionCU


//import org.koin.java.KoinJavaComponent.getKoin



class DataSyncWorker(
	appContext: Context,
	workerParams: WorkerParameters,
	private val realizarSincronizacionCU: RealizarSincronizacionCU,
	private val obtenerOrganizacion: ObtenerOrganizacionesCU
) : CoroutineWorker(appContext, workerParams){

	// El trabajo a realizar se define aquí dentro.
	// Esta función se ejecuta en un hilo de fondo por defecto.
	override suspend fun doWork(): Result {
		return try {
			App.log.d("Inicio del doWork del worker...")


			val organizacionesStr: String = App.sharedPrerfences.get<String>(K.ORGANIZACIONES, "")
			val organizacionesSeleccionadasPrevias: List<String> = organizacionesStr.split(";")


			/*val realizarSincronizacionCU: RealizarSincronizacionCU = getKoin().get()
			val obtenerOrganizacion: ObtenerOrganizacionesCU = getKoin().get()*/

			val organizaciones: List<Organizaciones> = obtenerOrganizacion.getAll()

			App.sharedPrerfences.put(K.ULTIMA_SINCRONIZACION, TiempoHora.ahora())

			organizacionesSeleccionadasPrevias.forEach { organizacion ->
				if (organizacion.isNotEmpty()){
					val org : Organizaciones? = organizaciones.filter { it.organizationId.equals(organizacion) }.firstOrNull()
					if (org!= null){
						realizarSincronizacionCU.sincronizarOrganizacion(org)
						App.log.d("Organizacion Sincronziada... $organizacion")
					}
				}

			}

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

	// Usamos un companion object para las constantes, siguiendo las guías de estilo.
	companion object {
		const val UNIQUE_WORK_NAME = "data_sync_worker"
	}
}