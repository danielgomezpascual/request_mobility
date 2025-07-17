package com.personal.requestmobility

import android.app.Application
import android.content.Context
import com.personal.requestmobility.core.composables.dialogos.DialogManager
import com.personal.requestmobility.core.data.ds.remote.network.moduloNetwork
import com.personal.requestmobility.core.log.di.moduloLog
import com.personal.requestmobility.core.log.domain.MyLog
import com.personal.requestmobility.core.room.AppDatabase
import com.personal.requestmobility.core.room.moduloDatabase
import com.personal.requestmobility.core.utils.SharedPreferencesManager
import com.personal.requestmobility.core.utils._toJson
import com.personal.requestmobility.core.utils._toObjectFromJson
import com.personal.requestmobility.dashboards.moduloDashboards
import com.personal.requestmobility.inicializador.modulosInicializador
import com.personal.requestmobility.kpi.moduloKpis
import com.personal.requestmobility.organizaciones.moduloOrganizaciones
import com.personal.requestmobility.paneles.moduloPaneles
import com.personal.requestmobility.sincronizacion.domain.SincronizacionUrl
import com.personal.requestmobility.sincronizacion.domain.interactors.ObtenerDatosEndPoint
import com.personal.requestmobility.sincronizacion.moduloSincronizacion
import com.personal.requestmobility.sincronizacion.ui.entidades.OrganizacionesSincronizarUI
import com.personal.requestmobility.transacciones.data.ds.local.entities.TransaccionesRoom
import com.personal.requestmobility.transacciones.data.repositorios.TransaccionesRepoImp
import com.personal.requestmobility.transacciones.moduloTransacciones
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {


    companion object {
        lateinit var context : Context
        lateinit var log: MyLog
        lateinit var dialog: DialogManager
         lateinit var sharedPrerfences: SharedPreferencesManager


    }


    override fun onCreate() {
        super.onCreate()
        initKoin()
        log = getKoin().get()

        context =  applicationContext
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
                moduloTransacciones,
                moduloDashboards,
                moduloPaneles,
                moduloSincronizacion,
                modulosInicializador
            )

        }
    }


}