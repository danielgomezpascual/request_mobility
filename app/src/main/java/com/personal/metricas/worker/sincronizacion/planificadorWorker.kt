package com.personal.metricas.worker.sincronizacion

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import com.personal.metricas.App
import com.personal.metricas.core.utils.K

// Esta función la puedes llamar desde tu Activity, ViewModel o Application.onCreate()
fun planificadorSyncWorker(context: Context) {

	App.log.d("Preparando worker...")


	// Definimos restricciones (opcional, pero muy recomendado)
	// Por ejemplo, que solo se ejecute cuando haya conexión a la red.
	val constraints = Constraints.Builder()
		.setRequiredNetworkType(NetworkType.CONNECTED)
		.build()


	// Obtenemos la instancia de WorkManager
	val workManager = WorkManager.getInstance(context)


	//---- PARA REAKUZAR TREST ---
	val periodicSyncRequest = OneTimeWorkRequestBuilder<DataSyncWorker>().setConstraints(constraints).build()
	workManager.enqueue(periodicSyncRequest)



	//------- PARA REALZIAR LA PROGRAMACIN, COMO MINUMO DEBE DE SER 15 MIN.
	// Encolamos el trabajo periódico.
	// Usamos enqueueUniquePeriodicWork para asegurarnos de que solo haya una
	// instancia de este trabajo planificada en todo momento.
	/*val periodicSyncRequest = PeriodicWorkRequestBuilder<DataSyncWorker>(
		1, // repeatInterval
		TimeUnit.HOURS // time Unit
	).setConstraints(constraints).build()




	workManager.enqueueUniquePeriodicWork(
		DataSyncWorker.UNIQUE_WORK_NAME, // Un nombre único para este trabajo
		ExistingPeriodicWorkPolicy.REPLACE, // Si ya existe, no hace nada. También puedes usar REPLACE.
		periodicSyncRequest
	)*/

	App.sharedPrerfences.put(K.ID_WORKER, periodicSyncRequest.id.toString())


	App.log.d("Scheduler Trabajo periódico de sincronización planificado.")
}