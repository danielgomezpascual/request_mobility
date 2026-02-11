package com.personal.metricas

import android.app.Application
import android.content.Context
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavController
import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.data.ds.remote.network.moduloNetwork
import com.personal.metricas.core.di.moduloCore
import com.personal.metricas.core.log.di.moduloLog
import com.personal.metricas.core.log.domain.MyLog
import com.personal.metricas.core.notificaciones.NotificacionesManager
import com.personal.metricas.core.room.moduloDatabase
import com.personal.metricas.core.utils.Preferencias
import com.personal.metricas.core.utils.SharedPreferencesManager
import com.personal.metricas.core.utils.if3
import com.personal.metricas.dashboards.moduloDashboards
import com.personal.metricas.endpoints.moduloEndPoints
import com.personal.metricas.firebase.crashlytics.Crash
import com.personal.metricas.firebase.domain.FirebaseManager
import com.personal.metricas.firebase.modulesFirebase
import com.personal.metricas.inicializador.modulosInicializador
import com.personal.metricas.kpi.moduloKpis
import com.personal.metricas.log.moduloLogSync
import com.personal.metricas.menu.modulosMenu
import com.personal.metricas.notas.moduloNotas
import com.personal.metricas.organizaciones.moduloOrganizaciones
import com.personal.metricas.paneles.moduloPaneles
import com.personal.metricas.settings.moduleSettings
import com.personal.metricas.sincronizacion.moduloSincronizacion
import com.personal.metricas.transacciones.moduloTransacciones
import com.personal.metricas.worker.sincronizacion.moduloWorker
import com.personal.metricas.worker.sincronizacion.planificadorSyncWorker
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class App : Application() {

    companion object {
        var ENTORNO: String = "DEV"

        lateinit var context: Context
        lateinit var log: MyLog
        lateinit var dialog: DialogManager
        lateinit var sharedPrerfences: SharedPreferencesManager

        lateinit var crash: Crash
        lateinit var navController: NavController

        lateinit var windowSizeClass: WindowSizeClass
        var numColumnas: Int = 1
    }

    override fun onCreate() {
        super.onCreate()

        // FirebaseApp.initializeApp(this)
        FirebaseManager().incializar(this)

        initKoin()
        log = getKoin().get()
        crash = getKoin().get()

        context = applicationContext
        dialog = getKoin().get()
        sharedPrerfences = SharedPreferencesManager(applicationContext)
        ENTORNO =
                if3(
                        App.sharedPrerfences.get<Boolean>(Preferencias.ENTORNO_PRO, false),
                        "PRO",
                        "DEV"
                )
        NotificacionesManager().createNotificationChannel()
        App.log.c(
                "Sincronizacion automática de datos ACTIVADA ${App.sharedPrerfences.get<Boolean>(Preferencias.SINCRONIZAR_AUTO, false)}"
        )
        //	if (App.sharedPrerfences.get<Boolean>(Preferencias.SINCRONIZAR_AUTO, false)) {
        planificadorSyncWorker(this)

        // }
        // Define el formato deseado (HH para formato 24h, mm para minutos)

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val horaActual = LocalTime.now()
        val _horaActual = horaActual.format(formatter)

        /*val hora30 = LocalTime.now().minusMinutes(30)
        val _hora30 =  hora30.format(formatter)
        App.log.d(_horaActual)
        App.log.d(_hora30)*/
        val horasEjemplo = "12:30;13:00;13:30"
        App.log.c("En periodo: ${horasEnPeriodo(horasEjemplo)}")
    }

    fun horasEnPeriodo(horas: String): Boolean {
        // 1. Define el rango de tiempo
        // val now = LocalTime.now()
        val _now = "2025-10-21 13:52:11"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val now = LocalDateTime.parse(_now, formatter)

        val thirtyMinutesAgo = now.minusMinutes(30)

        // 2. Procesa el String de entrada y comprueba
        return horas.split(';') // Divide el string en una lista de horas: ["15:30", "14:30", ...]
                .filter { it.isNotBlank() } // Elimina elementos vacíos si hay un ";" al final
                .any { hourStr ->
                    // La función 'any' devuelve true en cuanto una de las comprobaciones sea
                    // exitosa
                    try {
                        // Convierte el string "HH:mm" a un objeto LocalTime
                        val timeToCheck = LocalTime.parse(hourStr)
                        App.log.d("Tiem to check $timeToCheck")
                        // La lógica clave: ¿La hora está DESPUÉS de hace 30 min Y ANTES de ahora?
                        timeToCheck.isAfter(thirtyMinutesAgo.toLocalTime()) &&
                                timeToCheck.isBefore(now.toLocalTime())
                    } catch (e: DateTimeParseException) {
                        // Si una hora está mal formateada, la ignoramos y continuamos
                        false
                    }
                }
    }

    fun initKoin() {
        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@App)

            // ¡Añade esta línea!
            // Le dice a Koin que se encargue de la creación de Workers.
            workManagerFactory()

            modules(
                    modulesFirebase,
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
                    moduloNotas,
                    moduleSettings,
                    moduloLogSync,
                    moduloWorker,
                    moduloCore
            )
        }
    }
}
