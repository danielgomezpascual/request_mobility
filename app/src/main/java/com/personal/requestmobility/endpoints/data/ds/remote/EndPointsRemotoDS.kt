package com.personal.requestmobility.endpoints.data.ds.remote

import com.personal.requestmobility.App
import com.personal.requestmobility.core.data.ds.remote.network.retrofit.request.objectToHeaderMap
import com.personal.requestmobility.core.utils.Parametro
import com.personal.requestmobility.core.utils.Parametros
import com.personal.requestmobility.endpoints.data.ds.remote.servicio.EndPointRemotos
import com.personal.requestmobility.transacciones.data.ds.remote.parametros.ParamTransacciones

class EndPointsRemotoDS(private val apiRemoto: EndPointRemotos) {

	suspend fun getRemote(url: String, parametros: List<Parametro>): String {
		val r: ParamTransacciones = ParamTransacciones()



		App.log.lista("Paramtros", parametros)
		var headers:  Map<String, String> =emptyMap()

		parametros.forEach { parametro ->
			val v: String = parametro.convertirABase64()
			headers= headers.plus(Pair(parametro.key, v))

		}

		App.log.lista("Header", headers.toList())
		App.log.d("URL: $url")



			val response = apiRemoto.get(headers, url)
			val data = response.string()




		return data

	}

}