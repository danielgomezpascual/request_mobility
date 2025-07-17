package com.personal.requestmobility.sincronizacion.data.ds.remote

import com.personal.requestmobility.App
import com.personal.requestmobility.core.data.ds.TIPO_DS
import com.personal.requestmobility.core.data.ds.remote.network.retrofit.request.objectToHeaderMap
import com.personal.requestmobility.core.utils.getValueFromTagWithJsoup
import com.personal.requestmobility.sincronizacion.data.ds.remote.servicio.EndPointRemotos
import com.personal.requestmobility.transacciones.data.ds.IDataSourceTransacciones
import com.personal.requestmobility.transacciones.data.ds.remote.entidades.TrxResponseRetrofit
import com.personal.requestmobility.transacciones.data.ds.remote.entidades.toTransacciones
import com.personal.requestmobility.transacciones.data.ds.remote.parametros.ParamTransacciones
import com.personal.requestmobility.transacciones.data.ds.remote.servicio.TransaccionesApiRemoto
import com.personal.requestmobility.transacciones.domain.entidades.Transacciones

class EndPointsRemotoDS(private val apiRemoto: EndPointRemotos) {

    suspend fun getString(url: String):String{
        val r: ParamTransacciones = ParamTransacciones( )
        val headers = r.objectToHeaderMap()
        App.log.lista("Header", headers.toList())
        App.log.d("URL: $url")
        val response = apiRemoto.get(headers, url)
       val data = response.string()

        return data

    }

}