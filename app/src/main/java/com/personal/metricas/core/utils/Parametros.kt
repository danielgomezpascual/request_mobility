package com.personal.metricas.core.utils

import android.util.Base64
import com.personal.metricas.App
import com.personal.metricas.core.data.ds.remote.network.retrofit.request.Entornos
import com.personal.metricas.paneles.domain.entidades.DynamicQuery

data class Parametros(val ps: List<Parametro> = emptyList<Parametro>()) {


	companion object {


		fun dameParametrosPorDefectoMobility() = listOf<Parametro>(
			Parametro(key = "Authorization", valor = Entornos.get(App.ENTORNO).autorizacion),
			Parametro(key = "Oracle-Mobile-Backend-Id", valor = Entornos.get(App.ENTORNO).backendOracle),
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
	fun addFiltrosOrganizacionLectora(sqlOriginal: String , filtroOrganizacion: Boolean = false, filtroLectora: Boolean = false) :String{
		var sql: String = sqlOriginal
		if (filtroOrganizacion) {
			var organizationCode = ""
			ps.forEach { parametro ->
				if ((parametro.key.equals("organizationCode", true))
					|| (parametro.key.equals("organization_code", true))) {
					organizationCode = parametro.valor
				}
			}
			if (organizationCode.isNotEmpty()) {


				sql = DynamicQuery(sql)
					.addWhere("ORGANIZATION_CODE = '$organizationCode'", organizationCode)
					.build().sql

			}
		}

		if (filtroLectora) {
			var lectoraFisicaID = ""
			ps.forEach { parametro ->
				if (parametro.key.equals("LECTORA_FISICA_ID", true)) {
					lectoraFisicaID = parametro.valor
				}
			}
			if (lectoraFisicaID.isNotEmpty()) {

				sql = DynamicQuery(sql)
					.addWhere("LECTORA_FISICA_ID = '$lectoraFisicaID'", lectoraFisicaID)
					.build().sql

			}
		}

		return sql
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