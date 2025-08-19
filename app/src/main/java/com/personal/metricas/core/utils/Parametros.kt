package com.personal.metricas.core.utils

import android.util.Base64
import com.personal.metricas.App

data class Parametros(val ps: List<Parametro> = emptyList<Parametro>()) {


	companion object {


		fun dameParametrosPorDefectoMobility() = listOf<Parametro>(
			Parametro(key = "Authorization", valor = "Basic RkI2QTY0OTQ1NzNBNEE5MTg3Qzg1MzcxODYxNjdCQTBfTW9iaWxlQW5vbnltb3VzX0FQUElEOjFmM2M3YTFkLWRlZGMtNDFhZC1hYWY5LWFhMjhjMzJjMmEwNQ=="),
			Parametro(key = "Oracle-Mobile-Backend-Id", valor = "f017276c-e16e-40f9-be57-08602a6053d8"),
			Parametro(key = "P_MAX_ROWS", valor = " 50000"),
			Parametro(key = "P_LANGUAGE_CODE", valor = "es"))

		fun reemplazar(str: String, parametrosKpi: Parametros = Parametros(), parametrosDashboard: Parametros = Parametros()): String {

			var strReemplazos = str
			parametrosKpi.ps.forEach { parametro ->
				val key = parametro.key
				var keyEnValor: Boolean = false
				var keyValor = ""
				if (parametro.valor.contains("#")) {
					keyValor = parametro.valor.replace("#", "")
					keyEnValor = true
				}
				val k = if3(keyEnValor, keyValor, key)
				var parametroOrigenDatos: Parametro? = parametrosDashboard.ps.firstOrNull { it.key.equals(k) }
				var valor = ""
				if (parametroOrigenDatos != null) {
					valor = parametroOrigenDatos.valor
					App.log.d("A $valor")
				}

				if (parametro.fijo) {
					valor = parametro.defecto
					App.log.d("B $valor")
				}
				if (valor.isEmpty()) {
					valor = parametro.valor
					App.log.d("C $valor")
				}
				strReemplazos = strReemplazos.replace("#$k", "$valor", ignoreCase = true)
				App.log.d("Reemplazamos #$k, con $valor")
			}
			return strReemplazos
		}
	}

}

data class Parametro(val key: String = "", val valor: String = "", val defecto: String = "", val fijo: Boolean = false) {
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