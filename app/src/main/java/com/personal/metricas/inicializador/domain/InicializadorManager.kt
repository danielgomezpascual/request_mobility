package com.personal.metricas.inicializador.domain

import androidx.sqlite.db.SupportSQLiteDatabase
import com.personal.metricas.App
import com.personal.metricas.core.composables.dialogos.DialogManager
import com.personal.metricas.core.room.AppDatabase
import com.personal.metricas.core.utils.Preferencias
import org.koin.mp.KoinPlatform.getKoin

class InicializadorManager(
        private val operaciones: InicializadorOperaciones,
        private val initGeneral: InitDahsboardGeneral,
        private val initOrganizacioes: InitDashboardOrganizaciones,
        private val initLectoras: InitDahsboardLectoras,
        private val initLog: InitDahsboardLog,
        private val initErrores: InitDahsboardErrores,
        private val initVersiones: InitDahsboardVersionesGeneral,
        private val initDemo: InitDashboardDemo,
        private val dialog: DialogManager,
) {
        val appDatabase = getKoin().get<AppDatabase>()

        val db: SupportSQLiteDatabase =
                appDatabase
                        .openHelper
                        .writableDatabase // Usamos readableDatabase para operaciones de lectura

        suspend fun eliminarDatosGeneradosPreviamente() {

                App.sharedPrerfences.put<Boolean>(Preferencias.CONFIGURACION_INICIAL, true)

                val trxDao = getKoin().get<AppDatabase>().transaccionesDao()

                db.execSQL("DELETE FROM DASHBOARD WHERE autogenerado = 'Y'")
                db.execSQL("DELETE FROM PANELES WHERE autogenerado = 'Y'")
                db.execSQL("DELETE FROM KPIS WHERE autogenerado = 'Y'")
        }

        suspend fun crearVistas() {

                db.execSQL("DROP TABLE  IF EXISTS ESTADOS_TRANSACCIONES ")
                db.execSQL(
                        """CREATE TABLE IF NOT EXISTS ESTADOS_TRANSACCIONES (
							STATUS_CODE INTEGER PRIMARY KEY NOT NULL,
							ESTADO TEXT NOT NULL
						);"""
                )

                db.execSQL(
                        "INSERT INTO ESTADOS_TRANSACCIONES (STATUS_CODE, ESTADO) VALUES (0, 'OK');"
                )
                db.execSQL(
                        "INSERT INTO ESTADOS_TRANSACCIONES (STATUS_CODE, ESTADO) VALUES (1, 'ERROR DE MOBILITY DESKTOP');"
                )
                db.execSQL(
                        "INSERT INTO ESTADOS_TRANSACCIONES (STATUS_CODE, ESTADO) VALUES (2, 'ERROR');"
                )
                db.execSQL(
                        "INSERT INTO ESTADOS_TRANSACCIONES (STATUS_CODE, ESTADO) VALUES (4, 'REPROCESADO');"
                )

                /*db.execSQL("DROP VIEW  IF EXISTS TRX_7 ")
                		db.execSQL("CREATE VIEW  TRX_7 AS SELECT * FROM TRANSACCIONES T INNER JOIN ESTADOS_TRANSACCIONES ET ON T.REQ_STATUS = ET.STATUS_CODE WHERE DATE(CREATION_DATE) >= DATE('now', '-7 days')")

                		db.execSQL("DROP VIEW  IF EXISTS TRX_HOY ")
                		db.execSQL("CREATE VIEW IF NOT EXISTS TRX_HOY AS SELECT   * FROM  TRANSACCIONES T INNER JOIN ESTADOS_TRANSACCIONES ET ON T.REQ_STATUS = ET.STATUS_CODE WHERE  date(CREATION_DATE)  = date('now', 'localtime');")
                */
                db.execSQL("DROP VIEW  IF EXISTS TRX_TIME ")
                db.execSQL(
                        "CREATE VIEW IF NOT EXISTS TRX_TIME AS SELECT   * FROM  TRANSACCIONES T /*INNER JOIN ESTADOS_TRANSACCIONES ET ON T.REQ_STATUS = ET.STATUS_CODE*/ WHERE  date(CREATION_DATE)  > date('now', 'localtime', '-5 days');"
                )

                // db.execSQL("CREATE VIEW IF NOT EXISTS TRX_HOY AS SELECT   * FROM  TRANSACCIONES T
                // INNER JOIN ESTADOS_TRANSACCIONES ET ON T.REQ_STATUS = ET.STATUS_CODE WHERE
                // date(CREATION_DATE)  >= date('now', '-3 days');")

        }

        suspend fun start() {

                eliminarDatosGeneradosPreviamente()
                crearVistas()

                initDemo.crearDashboard()
                initVersiones.crearDashboard()
                initLog.crearDashboard()
                initErrores.crearDashboard()
                val dashboardLectora = initLectoras.dashboardLectoar()
                val dashboardUI = initOrganizacioes.generaDashboardOrganizaciones(dashboardLectora)
                initGeneral.crearGeneralComun(dashboardUI)
                initGeneral.crearHome()
        }
}
