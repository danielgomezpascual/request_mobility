package com.personal.requestmobility.transacciones.data.ds.remote.parametros

import com.personal.requestmobility.core.data.ds.remote.network.retrofit.request.ParamBase

data class ParamTransacciones(
   /* val p_usuario_password: String = "",
    val P_ITEM_TYPE: String = "",
    val p_usuario_id: String = "",*/
    val P_ORGANIZATION_ID: String = "2623",
    val P_MOB_REQUEST_ID: String = "",
    val P_TIPO_MOV: String = "",
    val P_NUMERO: String = "",
    val P_READER_ID: String = "DE0502",


) : ParamBase()