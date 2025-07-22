package com.personal.requestmobility.endpoints.data.ds

import com.personal.requestmobility.core.data.ds.IDS
import com.personal.requestmobility.endpoints.domain.entidades.EndPoint

interface IDataSourceEndPoint: IDS {
	suspend fun getAll(): List<EndPoint>
	suspend fun eliminar(endPoint: EndPoint)
	suspend fun eliminarTodos()
	suspend fun guardar(endPoint: EndPoint): Long
	suspend fun getPorID(identificador: Int): EndPoint
}