package com.personal.metricas

import android.app.Application
import android.content.Context
import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.data.ds.remote.network.moduloNetwork
import com.personal.metricas.core.log.di.moduloLog
import com.personal.metricas.core.log.domain.MyLog
import com.personal.metricas.core.room.moduloDatabase
import com.personal.metricas.core.utils.SharedPreferencesManager
import com.personal.metricas.dashboards.moduloDashboards
import com.personal.metricas.endpoints.moduloEndPoints
import com.personal.metricas.inicializador.modulosInicializador
import com.personal.metricas.kpi.moduloKpis
import com.personal.metricas.menu.modulosMenu
import com.personal.metricas.notas.moduloNotas
import com.personal.metricas.organizaciones.moduloOrganizaciones
import com.personal.metricas.paneles.moduloPaneles
import com.personal.metricas.sincronizacion.moduloSincronizacion
import com.personal.metricas.transacciones.moduloTransacciones
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {


	companion object {
		lateinit var context: Context
		lateinit var log: MyLog
		lateinit var dialog: DialogManager
		lateinit var sharedPrerfences: SharedPreferencesManager


	}


	override fun onCreate() {
		super.onCreate()
		initKoin()
		log = getKoin().get()

		context = applicationContext
		dialog = DialogManager()
		sharedPrerfences = SharedPreferencesManager(applicationContext)


		/*  val sincronizacionUrl: SincronizacionUrl = SincronizacionUrl()
		  sincronizacionUrl.testJson()

		  val obtenerDatosEndPoint: ObtenerDatosEndPoint = getKoin().get()
		  obtenerDatosEndPoint.test()*/
	}


	fun initKoin() {
		startKoin {
			// Log Koin into Android logger
			androidLogger()
			// Reference Android context
			androidContext(this@App)
			modules(
				moduloLog,
				moduloNetwork,
				moduloDatabase,
				moduloOrganizaciones,
				moduloKpis,
				moduloEndPoints,
				moduloTransacciones,
				moduloDashboards,
				moduloPaneles,
				moduloSincronizacion,
				modulosInicializador,
				modulosMenu,
				moduloNotas
			)

		}
	}


}