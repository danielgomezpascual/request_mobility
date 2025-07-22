package com.personal.metricas.transacciones.data.ds.remote

import com.personal.metricas.core.data.ds.TIPO_DS
import com.personal.metricas.core.data.ds.remote.network.retrofit.request.objectToHeaderMap
import com.personal.metricas.core.utils.getValueFromTagWithJsoup
import com.personal.metricas.transacciones.data.ds.IDataSourceTransacciones
import com.personal.metricas.transacciones.data.ds.remote.entidades.TrxResponseRetrofit
import com.personal.metricas.transacciones.data.ds.remote.entidades.toTransacciones
import com.personal.metricas.transacciones.data.ds.remote.parametros.ParamTransacciones
import com.personal.metricas.transacciones.data.ds.remote.servicio.TransaccionesApiRemoto
import com.personal.metricas.transacciones.domain.entidades.Transacciones

class TransaccionesRemotoDS(private val apiTransacciones: TransaccionesApiRemoto) : IDataSourceTransacciones {

    override val tipo: TIPO_DS
        get() = TIPO_DS.RETROFIT


    override suspend fun getAll(organizacion: String): List<Transacciones> {
        val r: ParamTransacciones = ParamTransacciones(P_ORGANIZATION_ID = organizacion)
        val headers = r.objectToHeaderMap()
        val response = apiTransacciones.getAll(headers)
        val trxRemotas: List<TrxResponseRetrofit> = response.Response.items
        val trx: List<Transacciones> = trxRemotas.map {
            val t = it.toTransacciones()
         /*   App.log.d("Etiquetas  ${t.cXmlField.getValueFromTagWithJsoup("contador_etiquetas") }")
            App.log.d("Detalles  ${t.cXmlField.getValueFromTagWithJsoup("contador_detalles") }")
            App.log.d("Lecrora fisica  ${t.cXmlField.getValueFromTagWithJsoup("lectora_fisica_id") }")*/

            t.etiquetas = t.cXmlField.getValueFromTagWithJsoup("contador_etiquetas")?:"0"
            t.detalles = t.cXmlField.getValueFromTagWithJsoup("contador_detalles")?:"0"
            t.lectoraFisicaId = t.cXmlField.getValueFromTagWithJsoup("lectora_fisica_id")?:"0"

            t
        }
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