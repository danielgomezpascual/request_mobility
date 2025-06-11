package com.personal.requestmobility

import android.app.Application
import com.personal.requestmobility.core.log.di.moduloLog
import com.personal.requestmobility.core.log.domain.MyLog
import com.personal.requestmobility.core.room.AppDatabase
import com.personal.requestmobility.core.room.moduloDatabase
import com.personal.requestmobility.dashboards.moduloDashboards
import com.personal.requestmobility.kpi.moduloKpis
import com.personal.requestmobility.paneles.moduloPaneles
import com.personal.requestmobility.transacciones.data.ds.local.entities.TransaccionesRoom
import com.personal.requestmobility.transacciones.moduloTransacciones
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {


    companion object {
        lateinit var log: MyLog

    }


    override fun onCreate() {
        super.onCreate()
        initKoin()
        log = getKoin().get()
        cargaTransaccionesTest()
      }

    fun initKoin() {
        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@App)
            modules(
                moduloLog,
                moduloDatabase,
                moduloKpis,
                moduloTransacciones,
                moduloDashboards,
                moduloPaneles
            )

        }
    }

    private fun cargaTransaccionesTest() {


        val transacciones = listOf(
            TransaccionesRoom(1, "PL: 1.0.37. ORA: 20180510. APK: 1.34.0.161", "<XMLTYPE>", "31/03/2025 9:53:26", 0, "0022560250331095002", "ALB_MAN", "EAS002", "lgarcia", "El albaran Y56676 se ha creado / actualizado correctamente. No se ha podido generar el documento. No documents setup found for DELIVERY_NOTE, 2560, 336502, 26531, E.", "<CLOB>", 738148, null, null, "MOBILITY", "E", null, "0DCCDA04F289D049AE828EF637EF8F4E", "<XMLTYPE>", null, "<XMLTYPE>", null),
            TransaccionesRoom(738147, "1.34.0.161", "<XMLTYPE>", "31/03/2025 9:53:22", 0, "ES1009909250331010001", "REPACKING", "EGE001", "fjtormo", "Repacking ES1009909250331010001 created succesfully!! #childs: 13", "<CLOB>", -9999, null, null, "MOBILITY", "US", null, "6B03044783FA1B9C448B9FB0F7339A6B", "<XMLTYPE>", null, "<XMLTYPE>", null),
            TransaccionesRoom(738145, "PL: 1.0.37. ORA: 20180510. APK: 1.34.0.161", "<XMLTYPE>", "31/03/2025 9:51:08", 0, "0022560250331092834", "ALB_MAN", "EAS002", "lgarcia", "El albaran Y56670 se ha creado / actualizado correctamente. No se ha podido generar el documento. No documents setup found for DELIVERY_NOTE, 2560, 336502, 2758, E.", "<CLOB>", 738145, null, null, "MOBILITY", "E", null, "7DB026D8440B6EEE5C7FADC4B0C5CE29", "<XMLTYPE>", null, "<XMLTYPE>", null),
            TransaccionesRoom(738144, "PL: 1.0.37. ORA: 20180510. APK: 1.34.0.161", "<XMLTYPE>", "31/03/2025 9:50:44", 0, "0022560250331092658", "ALB_MAN", "EAS002", "lgarcia", "El albaran Y56677 se ha creado / actualizado correctamente. No se ha podido generar el documento. No documents setup found for DELIVERY_NOTE, 2560, 336502, 2758, E.", "<CLOB>", 738144, null, null, "MOBILITY", "E", null, "951DD54212CC66058620B2244A944FB2", "<XMLTYPE>", null, "<XMLTYPE>", null),
            TransaccionesRoom(737277, "PL: 1.0.37. ORA: 20180510. APK: 1.34.0.161", "<XMLTYPE>", "28/03/2025 11:54:26", 0, "0012548250328115647", "ALB_MAN", "EGE001", "fjtormo", "El albaran Y56658 se ha creado / actualizado correctamente. No se ha podido generar el documento. No documents setup found for DELIVERY_NOTE, 2548, 441508, 3266, E.", "<CLOB>", 737277, null, null, "MOBILITY", "E", null, "CF0143783825924B9DFAB3FA5409A4A9", "<XMLTYPE>", null, "<XMLTYPE>", null),
            TransaccionesRoom(737274, "PL: 1.0.37. ORA: 20180510. APK: 1.34.0.161", "<XMLTYPE>", "28/03/2025 11:50:55", 0, "0012548250328115233", "ALB_MAN", "EGE001", "fjtormo", "El albaran Y56657 se ha creado / actualizado correctamente. No se ha podido generar el documento. No documents setup found for DELIVERY_NOTE, 2548, 441508, 3266, E.", "<CLOB>", 737274, null, null, "MOBILITY", "E", null, "F19344ABDDFBCCBDEEE3ED977E0180CC", "<XMLTYPE>", null, "<XMLTYPE>", null),
            TransaccionesRoom(737271, "1.34.0.161", "<XMLTYPE>", "28/03/2025 11:47:59", 0, "ES1009909250328010002", "REPACKING", "EGE001", "fjtormo", "Repacking ES1009909250328010002 created succesfully!! #childs: 57", "<CLOB>", -9999, null, null, "MOBILITY", "US", null, "E997A14F3EBD4F905A75003C05D45B8B", "<XMLTYPE>", null, "<XMLTYPE>", null),
            TransaccionesRoom(737216, "PL: 1.0.37. ORA: 20180510. APK: 1.34.0.161", "<XMLTYPE>", "28/03/2025 10:42:00", 0, "0022560250328103403", "ALB_MAN", "EAS002", "lgarcia", "El albaran Y56653 se ha creado / actualizado correctamente. No se ha podido generar el documento. No documents setup found for DELIVERY_NOTE, 2560, 336502, 13357, E.", "<CLOB>", 737216, null, null, "MOBILITY", "E", null, "E8179CD63A0E6F1E62DCD9C1F7CB953B", "<XMLTYPE>", null, "<XMLTYPE>", null),
            TransaccionesRoom(737215, "PL: 1.0.37. ORA: 20180510. APK: 1.34.0.161", "<XMLTYPE>", "28/03/2025 10:41:36", 0, "0022560250328102356", "ALB_MAN", "EAS002", "lgarcia", "El albaran Y56651 se ha creado / actualizado correctamente. No se ha podido generar el documento. No documents setup found for DELIVERY_NOTE, 2560, 336502, 13357, E.", "<CLOB>", 737215, null, null, "MOBILITY", "E", null, "B7DAA05BC99425C878385E8BA5CB70C7", "<XMLTYPE>", null, "<XMLTYPE>", null),
            TransaccionesRoom(737168, "1.34.0.161", "<XMLTYPE>", "28/03/2025 9:54:50", 0, "ES1009909250328010001", "REPACKING", "EGE001", "fjtormo", "Repacking ES1009909250328010001 created succesfully!! #childs: 16", "<CLOB>", -9999, null, null, "MOBILITY", "US", null, "D11484BDAD013CBFE15AD5F1E353E34E", "<XMLTYPE>", null, "<XMLTYPE>", null),
            TransaccionesRoom(737155, "PL: 1.0.37. ORA: 20180510. APK: 1.34.0.161", "<XMLTYPE>", "28/03/2025 9:29:45", 0, "0022560250328092951", "ALB_MAN", "EAS002", "lgarcia", "El albaran Y56648 se ha creado / actualizado correctamente. No se ha podido generar el documento. No documents setup found for DELIVERY_NOTE, 2560, 336502, 38866, E.", "<CLOB>", 737155, 737153, null, "MOBILITY", "E", null, "FCBB2552E30786D32B412802445EE85A", "<XMLTYPE>", null, "<XMLTYPE>", null),
            TransaccionesRoom(737153, "PL: 1.0.37. ORA: 20180510. APK: 1.34.0.161", "<XMLTYPE>", "28/03/2025 9:29:26", 4, "0022560250328092951", "ALB_MAN", "EAS002", "lgarcia", "ORA-00054: recurso ocupado y obtenido con NOWAIT especificado o timeout vencido. (ERR0001). Se han encontrado errores procesando la tarea (Id: 737153).", "<CLOB>", 737153, 737155, null, "MOBILITY", "E", null, "FCBB2552E30786D32B412802445EE85A", "<XMLTYPE>", null, "<XMLTYPE>", null),
            TransaccionesRoom(737132, "PL: 1.0.37. ORA: 20180510. APK: 1.34.0.161", "<XMLTYPE>", "28/03/2025 8:39:09", 0, "0022560250327123306", "ALB_MAN", "EAS002", "lgarcia", "El albaran Y56621 se ha creado / actualizado correctamente. No se ha podido generar el documento. No documents setup found for DELIVERY_NOTE, 2560, 336502, 38166, E.", "<CLOB>", 737132, null, null, "MOBILITY", "E", null, "97C815475672E77F24B206521C1A952F", "<XMLTYPE>", null, "<XMLTYPE>", null),
            TransaccionesRoom(737130, "PL: 1.0.37. ORA: 20180510. APK: 1.34.0.161", "<XMLTYPE>", "28/03/2025 8:38:59", 0, "0022560250327123051", "ALB_MAN", "EAS002", "lgarcia", "El albaran Y56622 se ha creado / actualizado correctamente. No se ha podido generar el documento. No documents setup found for DELIVERY_NOTE, 2560, 336502, 184088, E.", "<CLOB>", 737130, null, null, "MOBILITY", "E", null, "C39BDABEC48FB06B5102713C65D4BBEB", "<XMLTYPE>", null, "<XMLTYPE>", null),





        )

        GlobalScope.launch(Dispatchers.IO) {
            val database: AppDatabase = getKoin().get()
            val dao = database.transaccionesDao()
            dao.vaciarTabla()
            dao.insert(transacciones)

        }
    }




}