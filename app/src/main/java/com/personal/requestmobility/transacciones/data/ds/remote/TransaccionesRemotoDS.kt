package com.personal.requestmobility.transacciones.data.ds.remote

import com.personal.requestmobility.App
import com.personal.requestmobility.core.data.ds.TIPO_DS
import com.personal.requestmobility.core.data.ds.remote.network.retrofit.request.objectToHeaderMap
import com.personal.requestmobility.transacciones.data.ds.IDataSourceTransacciones
import com.personal.requestmobility.transacciones.data.ds.remote.entidades.TrxResponseRetrofit
import com.personal.requestmobility.transacciones.data.ds.remote.entidades.toTransacciones
import com.personal.requestmobility.transacciones.data.ds.remote.parametros.ParamTransacciones
import com.personal.requestmobility.transacciones.data.ds.remote.servicio.TransaccionesApiRemoto
import com.personal.requestmobility.transacciones.domain.entidades.Transacciones

class TransaccionesRemotoDS(private val apiTransacciones: TransaccionesApiRemoto) : IDataSourceTransacciones {

    override val tipo: TIPO_DS
        get() = TIPO_DS.RETROFIT


    override suspend fun getAll(): List<Transacciones> {
        val r: ParamTransacciones = ParamTransacciones()
        val headers = r.objectToHeaderMap()
        val response = apiTransacciones.getAll(headers)


        val trxRemotas: List<TrxResponseRetrofit> = response.Response.items
        val trx: List<Transacciones> = trxRemotas.map { it.toTransacciones() }

        return trx


    }

    override suspend fun eliminar(transaccion: Transacciones) {
        TODO("Not yet implemented")
    }

    override suspend fun eliminarTodas() {
        TODO("Not yet implemented")
    }

    override suspend fun guardar(transaccion: Transacciones): Long {
        TODO("Not yet implemented")
    }

    override suspend fun guardar(trx: List<Transacciones>): Long {
        TODO("Not yet implemented")
    }

    override suspend fun obtener(id: Int): Transacciones {
        TODO("Not yet implemented")
    }


}