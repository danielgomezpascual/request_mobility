package com.personal.requestmobility.core.utils

import com.personal.requestmobility.App

data class Parametros(val ps:List<Parametro> = emptyList<Parametro>()){


	companion object {


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
data class Parametro(val key: String ="", val valor: String = "",  val defecto: String ="", val fijo: Boolean = false)