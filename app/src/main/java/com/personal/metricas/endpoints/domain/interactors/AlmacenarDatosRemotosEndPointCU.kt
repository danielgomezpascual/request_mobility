package com.personal.metricas.endpoints.domain.interactors

import androidx.sqlite.db.SupportSQLiteDatabase
import com.personal.metricas.App
import com.personal.metricas.core.room.AppDatabase
import com.personal.metricas.core.utils.Parametro
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.endpoints.data.ds.remote.EndPointsRemotoDS
import com.personal.metricas.endpoints.domain.entidades.EndPoint
import com.personal.metricas.endpoints.domain.entidades.ResultadoEndPoint
import com.personal.metricas.endpoints.domain.servicios.ConversorJsonToTabla
import com.personal.metricas.organizaciones.domain.interactors.ObtenerOrganizacionesCU
import com.personal.metricas.transacciones.data.repositorios.TransaccionesRepoImp
import com.personal.metricas.transacciones.domain.entidades.Transacciones
import com.personal.metricas.transacciones.domain.interactors.GuardarTransacciones
import org.koin.mp.KoinPlatform

class AlmacenarDatosRemotosEndPointCU(
	private val obtenerEndPointCU: ObtenerEndPointCU,
	private val accesoRemoto: EndPointsRemotoDS,
	private val conversonrJson: ConversorJsonToTabla,

	private val obtenerOrganizacionesCU: ObtenerOrganizacionesCU,
	private val repoTrx: TransaccionesRepoImp,
	private val guardarTrx: GuardarTransacciones,

	) {


	private val appDatabase = KoinPlatform.getKoin().get<AppDatabase>()
	private val db: SupportSQLiteDatabase = appDatabase.openHelper.writableDatabase // Usamos readableDatabase para operaciones de lectura


	suspend fun obtenerRemoto(identificador: Int): ResultadoEndPoint {

		try {
			val endPoint = obtenerEndPointCU.obtener(identificador)
			val url: String = endPoint.url
			val str = accesoRemoto.getRemote(url, endPoint.parametros.ps)
			conversonrJson.jsonToTabla(str, endPoint.nodoIdentificadorFila, endPoint.tabla, endPoint.eliminarDatos)
			return ResultadoEndPoint(procesado = true, errores = false, descripcion = "Datos almacenados ${endPoint.tabla}")
		}
		catch (e: Exception) {
			return ResultadoEndPoint(procesado = true, errores = true, descripcion = e.message ?: "Error al procesar")
		}
	}

	suspend fun obtenerRemoto(endPoint: EndPoint): ResultadoEndPoint {

		try {


			if (endPoint.tabla.lowercase().equals("transacciones")) {
				//--------------------------------------
				var organizacionId: String = ""
				endPoint.parametros.ps.forEach { parametro ->
					if (parametro.key.equals("P_ORGANIZATION_ID")) {
						organizacionId = parametro.valor
					}

				}
				val organizacion = obtenerOrganizacionesCU.getAll().first { it.organizationId.equals(organizacionId) }

				App.log.d(" Organizacion: ${organizacion.toString()}")
				val trx: List<Transacciones> = repoTrx.getTrxOracle(organizacion.organizationId)



				val l: List<Transacciones> = trx.map {
					it.cXmlField = ""
					it.organizationCode = organizacion.organizationCode
					it.organizationName = organizacion.organizationName
					it.organizationId = organizacion.organizationId
					it.masterOrganizationId = organizacion.masterOrganizationId
					it
				}

				guardarTrx.guardar(l)
			} else {

				//val endPoint = obtenerEndPointCU.obtener(identificador)
				val url: String = endPoint.url
				val str = accesoRemoto.getRemote(url, endPoint.parametros.ps)
				conversonrJson.jsonToTabla(str, endPoint.nodoIdentificadorFila,
										   endPoint.tabla,
										   endPoint.eliminarDatos)

			}

			//--------------------------------------


			return ResultadoEndPoint(procesado = true, errores = false, descripcion = "Datos almacenados ${endPoint.tabla}")
		}
		catch (e: Exception) {
			return ResultadoEndPoint(procesado = true, errores = true, descripcion = e.message ?: "Error al procesar")
		}
	}

	suspend fun obtenerRemotoParametors(identificador: Int, parametros: Parametros): ResultadoEndPoint {

		try {
			val endPoint = obtenerEndPointCU.obtener(identificador)
			val url: String = endPoint.url
			var listaParametrosReemplazados: MutableList<Parametro> = mutableListOf<Parametro>()
			endPoint.parametros.ps.forEach { parametro ->

				val s: String = Parametros.reemplazar(parametro.valor, endPoint.parametros, parametros)
				val np: Parametro = parametro.copy(valor = s)
				listaParametrosReemplazados.add(np)
			}


			val str = accesoRemoto.getRemote(url, listaParametrosReemplazados)
			conversonrJson.jsonToTabla(str, endPoint.nodoIdentificadorFila, endPoint.tabla, endPoint.eliminarDatos)
			return ResultadoEndPoint(procesado = true, errores = false, descripcion = "Datos almacenados ${endPoint.tabla}")
		}
		catch (e: Exception) {
			return ResultadoEndPoint(procesado = true, errores = true, descripcion = e.message ?: "Error al procesar")
		}
	}
}