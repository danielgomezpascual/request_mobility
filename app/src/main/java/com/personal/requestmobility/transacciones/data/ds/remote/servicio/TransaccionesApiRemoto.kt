package com.personal.requestmobility.transacciones.data.ds.remote.servicio


import com.personal.requestmobility.transacciones.data.ds.remote.entidades.ResponseTransacciones
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface TransaccionesApiRemoto {

    @GET("GetSolicitudes")
    suspend fun getAll(@HeaderMap headers: Map<String, String>): ResponseTransacciones

}