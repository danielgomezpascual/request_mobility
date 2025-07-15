package com.personal.requestmobility.core.utils

data class Parametros(val ps:List<Parametro> = emptyList<Parametro>()){


	companion object {


		fun reemplazar(str: String, parametrosKpi: Parametros = Parametros(), parametrosDashboard: Parametros = Parametros()): String {
			var strReemplazos = str
			parametrosKpi.ps.forEach { parametro ->

				val key = parametro.key
				val parametroOrigenDatos: Parametro? = parametrosDashboard.ps.firstOrNull { it.key.equals(key) }

				var valor = ""
				//var valor = if3(parametro.valor.isEmpty(), parametro.defecto, parametro.valor)
				if ( parametroOrigenDatos != null) {
					valor = parametroOrigenDatos.valor
				}

				if (parametro.fijo){valor = parametro.defecto}

				strReemplazos = strReemplazos.replace("\$$key", "$valor", ignoreCase = true)
			}

			return  strReemplazos
		}
	}

}
data class Parametro(val key: String ="", val valor: String = "",  val defecto: String ="", val fijo: Boolean = false)