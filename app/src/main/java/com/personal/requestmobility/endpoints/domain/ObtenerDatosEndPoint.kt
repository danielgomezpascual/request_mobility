package com.personal.requestmobility.endpoints.domain

import com.personal.requestmobility.endpoints.data.ds.remote.EndPointsRemotoDS
import com.personal.requestmobility.endpoints.domain.servicios.ConversorJsonToTabla
import kotlinx.coroutines.runBlocking

class ObtenerDatosEndPoint(
		private val remoto: EndPointsRemotoDS
) {
	fun test() {
		/*runBlocking {
			val url: String = "https://fb6a6494573a4a9187c8537186167ba0.mobile.ocp.oraclecloud.com/mobile/custom/ApiMaxamWS_DEV/LogicReaders"
			val str = remoto.getRemote(url)
			val conversorJsonToTabla: ConversorJsonToTabla = ConversorJsonToTabla()
			val resultado = conversorJsonToTabla.jsonToMap(str, "LogicReader")
			conversorJsonToTabla.mapToTable("Lectoras", resultado)


			val urlUsuarios: String = "https://fb6a6494573a4a9187c8537186167ba0.mobile.ocp.oraclecloud.com/mobile/custom/ApiMaxamWS_DEV/Users"
			val resultadoUsuarios = conversorJsonToTabla.jsonToMap(remoto.getRemote(urlUsuarios), "UserInfo")
			conversorJsonToTabla.mapToTable("Usuarios", resultadoUsuarios)

		}*/

	}

}