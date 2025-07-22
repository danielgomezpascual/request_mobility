package com.personal.metricas.endpoints.domain.interactors

import androidx.sqlite.db.SupportSQLiteDatabase
import com.personal.metricas.core.room.AppDatabase
import com.personal.metricas.endpoints.data.ds.remote.EndPointsRemotoDS
import com.personal.metricas.endpoints.domain.entidades.ResultadoEndPoint
import com.personal.metricas.endpoints.domain.servicios.ConversorJsonToTabla
import org.koin.mp.KoinPlatform

class AlmacenarDatosRemotosEndPointCU(
	private val obtenerEndPointCU: ObtenerEndPointCU,
	private val accesoRemoto: EndPointsRemotoDS,
	private val conversonrJson: ConversorJsonToTabla,
) {


	private val appDatabase = KoinPlatform.getKoin().get<AppDatabase>()
	private val db: SupportSQLiteDatabase = appDatabase.openHelper.writableDatabase // Usamos readableDatabase para operaciones de lectura


	suspend fun obtenerRemoto(identificador: Int): ResultadoEndPoint {

		try{
			val endPoint = obtenerEndPointCU.obtener(identificador)
			val url: String = endPoint.url
			val str = accesoRemoto.getRemote(url, endPoint.parametros.ps)
			conversonrJson.jsonToTabla(str, endPoint.nodoIdentificadorFila, endPoint.tabla)
			return ResultadoEndPoint(procesado = true, errores = false, descripcion = "Datos almacenados ${endPoint.tabla}")
		}catch (e: Exception){
			return ResultadoEndPoint(procesado = true, errores = true, descripcion = e.message?:"Error al procesar")
		}


	}
}