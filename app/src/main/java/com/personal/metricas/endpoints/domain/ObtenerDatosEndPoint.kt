package com.personal.metricas.endpoints.domain

import com.personal.metricas.endpoints.data.ds.remote.EndPointsRemotoDS

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