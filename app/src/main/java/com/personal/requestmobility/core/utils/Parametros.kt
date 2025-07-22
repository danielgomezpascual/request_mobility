package com.personal.requestmobility.core.utils

import android.util.Base64
import com.personal.requestmobility.App
import com.personal.requestmobility.core.data.ds.remote.network.retrofit.request.HeaderRetrofitBase

data class Parametros(val ps:List<Parametro> = emptyList<Parametro>()){


	companion object {




		fun dameParametrosPorDefectoMobility() = listOf<Parametro>(
			Parametro(key = "Authorization", valor = "Basic RkI2QTY0OTQ1NzNBNEE5MTg3Qzg1MzcxODYxNjdCQTBfTW9iaWxlQW5vbnltb3VzX0FQUElEOjFmM2M3YTFkLWRlZGMtNDFhZC1hYWY5LWFhMjhjMzJjMmEwNQ=="),
			Parametro(key = "Oracle-Mobile-Backend-Id", valor = "f017276c-e16e-40f9-be57-08602a6053d8"),
			Parametro(key = "P_MAX_ROWS", valor = " 50000"),
			Parametro(key = "P_LANGUAGE_CODE", valor = "es"))

		fun reemplazar(str: String, parametrosKpi: Parametros = Parametros(), parametrosDashboard: Parametros = Parametros()): String {

			var strReemplazos = str
		/*	App.log.d("Str: $str")
			App.log.d("parametrosKpi: ${parametrosKpi.ps.toString()}")
			App.log.d("parametrosDashboard: ${parametrosDashboard.ps.toString()}")
			App.log.linea()*/
			parametrosKpi.ps.forEach { parametro ->

				val key = parametro.key
				App.log.d(key)
				App.log.d(parametrosDashboard.toString())
				val parametroOrigenDatos: Parametro? = parametrosDashboard.ps.firstOrNull { it.key.equals(key) }

				var valor = ""
				//var valor = if3(parametro.valor.isEmpty(), parametro.defecto, parametro.valor)
				if ( parametroOrigenDatos != null) {
					valor = parametroOrigenDatos.valor
				}

				if (parametro.fijo){valor = parametro.defecto}

				if (valor.isEmpty()) {valor = parametro.defecto}

				strReemplazos = strReemplazos.replace("#$key", "$valor", ignoreCase = true)
			}


			return  strReemplazos
		}
	}

}
data class Parametro(val key: String ="", val valor: String = "",  val defecto: String ="", val fijo: Boolean = false){
	fun convertirABase64(): String {
		val listaParametrosSinBase64: List<String> = listOf<String>(
			"Authorization", "ORACLE_MOBILE_BACKEND_ID",
			"ORACLE-MOBILE-BACKEND-ID",
			"Oracle-Mobile-Backend-Id", "Oracle_Mobile_Backend_Id")
		if (listaParametrosSinBase64.contains(key)) return valor
		val bytes = valor.toByteArray()
		//return valor
		return Base64.encodeToString(bytes, Base64.NO_WRAP).toString()
	}
}