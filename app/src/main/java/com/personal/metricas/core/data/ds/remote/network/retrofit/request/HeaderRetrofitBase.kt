package com.personal.metricas.core.data.ds.remote.network.retrofit.request


import android.util.Base64
import com.personal.metricas.App

import kotlin.reflect.full.memberProperties

abstract class HeaderRetrofitBase {
    //DEV
   /* val Authorization: String = "Basic RkI2QTY0OTQ1NzNBNEE5MTg3Qzg1MzcxODYxNjdCQTBfTW9iaWxlQW5vbnltb3VzX0FQUElEOjFmM2M3YTFkLWRlZGMtNDFhZC1hYWY5LWFhMjhjMzJjMmEwNQ=="
    val Oracle_Mobile_Backend_Id: String = "f017276c-e16e-40f9-be57-08602a6053d8"*/


    //PRO
  //  val Authorization: String = "Basic NTA1NDlGOEMxRUNGNDNGRDg2OTgyMDg0NkE4QjRBMTVfTW9iaWxlQW5vbnltb3VzX0FQUElEOjdkNjVmNDRlLTdmY2MtNDE3NS1iOGFkLTBjNmI4MjAzMmY5Nw=="
  //  val Oracle_Mobile_Backend_Id: String = "c2c7fb6e-cc74-4e34-8b29-1239bc47f036"


    val Authorization: String = Entornos.get(App.ENTORNO).autorizacion
    val Oracle_Mobile_Backend_Id: String  = Entornos.get(App.ENTORNO).backendOracle
}


fun HeaderRetrofitBase.objectToHeaderMap(): Map<String, String> {
    return this::class.memberProperties.associate { prop ->

        val p = if (prop.name.equals("Oracle_Mobile_Backend_Id")) {
            "Oracle-Mobile-Backend-Id"
        } else {
            prop.name
        }
        p to convertirABase64(p, prop.getter.call(this).toString())
    }
}


fun HeaderRetrofitBase.convertirABase64(nombre: String, valor: String, urlSafe: Boolean = false): String {
    val listaParametrosSinBase64: List<String> = listOf<String>(
        "Authorization", "ORACLE_MOBILE_BACKEND_ID",
        "ORACLE-MOBILE-BACKEND-ID",
        "Oracle-Mobile-Backend-Id", "Oracle_Mobile_Backend_Id")
    if (listaParametrosSinBase64.contains(nombre)) return valor
    val bytes = valor.toByteArray()
    //return valor
   return Base64.encodeToString(bytes, Base64.NO_WRAP).toString()
}


fun HeaderRetrofitBase.toLog() {
     this::class.memberProperties.forEach { prop ->
        App.log.d("${prop.name} -> ${prop.getter.call(this).toString()} ")
    }
}



