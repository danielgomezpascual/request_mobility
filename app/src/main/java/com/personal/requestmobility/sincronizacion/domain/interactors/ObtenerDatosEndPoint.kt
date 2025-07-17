package com.personal.requestmobility.sincronizacion.domain.interactors

import com.personal.requestmobility.App
import com.personal.requestmobility.sincronizacion.data.ds.remote.EndPointsRemotoDS
import com.personal.requestmobility.sincronizacion.domain.SincronizacionUrl
import kotlinx.coroutines.runBlocking
import kotlin.collections.component1
import kotlin.collections.component2

class ObtenerDatosEndPoint(
		private val remoto: EndPointsRemotoDS
) {
	fun test() {
		runBlocking {
			val url: String = "https://fb6a6494573a4a9187c8537186167ba0.mobile.ocp.oraclecloud.com/mobile/custom/ApiMaxamWS_DEV/LogicReaders"
			val str = remoto.getString(url)
			val sincronizacionUrl : SincronizacionUrl = SincronizacionUrl()
			val resultado = sincronizacionUrl.jsonToTable(str, "LogicReader")
			sincronizacionUrl.toTabla("Lectoras", resultado)



			val urlUsuarios: String = "https://fb6a6494573a4a9187c8537186167ba0.mobile.ocp.oraclecloud.com/mobile/custom/ApiMaxamWS_DEV/Users"
			val resultadoUsuarios = sincronizacionUrl.jsonToTable(remoto.getString(urlUsuarios), "UserInfo")
			sincronizacionUrl.toTabla("Usuarios", resultadoUsuarios)

		}

	}

}