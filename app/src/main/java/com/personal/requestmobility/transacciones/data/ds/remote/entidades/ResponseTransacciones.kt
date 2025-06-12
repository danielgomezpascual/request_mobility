package com.personal.requestmobility.transacciones.data.ds.remote.entidades

import com.google.gson.annotations.SerializedName
import com.personal.requestmobility.core.utils.siVacio
import com.personal.requestmobility.transacciones.domain.entidades.Transacciones

data class ResponseTransacciones(@SerializedName(value = "Response") val Response: Response)


data class Response(@SerializedName(value = "Solicitudes") val items:
                    List<TrxResponseRetrofit> = emptyList<TrxResponseRetrofit>())


data class TrxResponseRetrofit(
    @SerializedName("ORGANIZATION_ID") val ORGANIZATION_ID: String = "",
    @SerializedName("SCE_REFERENCIA") val SCE_REFERENCIA: String = "",
    @SerializedName("ORACLE_DOC") val ORACLE_DOC: String = "",
    @SerializedName("MOB_REQUEST_ID") val MOB_REQUEST_ID: String = "",
    @SerializedName("NUMERO") val NUMERO: String = "",
    @SerializedName("TIPO_MOV") val TIPO_MOV: String = "",
    @SerializedName("LECTORA_ID") val LECTORA_ID: String = "",
    @SerializedName("USUARIO_LECTORA") val USUARIO_LECTORA: String = "",
    @SerializedName("REQ_STATUS") val REQ_STATUS: String = "",
    @SerializedName("REQ_MESSAGE") val REQ_MESSAGE: String = "",
    @SerializedName("C_XML_FIELD") val C_XML_FIELD: String = "",
    @SerializedName("CREATION_DATE") val CREATION_DATE: String = "",
    @SerializedName("PARENT_REQUEST_ID") val PARENT_REQUEST_ID: String = "",
    @SerializedName("DONE_FOR_REQ_ID") val DONE_FOR_REQ_ID: String = "",
    @SerializedName("PROCESO") val PROCESO: String = "",
    @SerializedName("LANGUAGE_CODE") val LANGUAGE_CODE: String = "",
    @SerializedName("PROGRAM_VERSION") val PROGRAM_VERSION: String = "",
    @SerializedName("READER_XML_ID") val READER_XML_ID: String = "",
    @SerializedName("XML_HASH") val XML_HASH: String = "",
    @SerializedName("TCB_ERROR_PILE") val TCB_ERROR_PILE: String = "",
    @SerializedName("REQ_RESULT") val REQ_RESULT: String = "",
    @SerializedName("REQ_RESULT_DETAIL") val REQ_RESULT_DETAIL: String = "",
    @SerializedName("REQ_RESULT_DATE") val REQ_RESULT_DATE: String = ""
    )


fun TrxResponseRetrofit.toTransacciones() = Transacciones(
    mobRequestId = this.MOB_REQUEST_ID.toInt(),
    programVersion = this.PROGRAM_VERSION,
    cXmlField = this.C_XML_FIELD,
    creationDate = this.CREATION_DATE,
    reqStatus =   this.REQ_STATUS.siVacio("-1").toInt(),
    numero = this.NUMERO,
    tipoMov = this.TIPO_MOV,
    lectoraId = this.LECTORA_ID,
    usuarioLectora = this.USUARIO_LECTORA,
    reqMessage = this.REQ_MESSAGE,
    parentRequestId =this.PARENT_REQUEST_ID.siVacio("-1").toInt(),

    proceso = this.PROCESO,
    languageCode = this.LANGUAGE_CODE,
    readerXmlId = this.READER_XML_ID,
    xmlHash = this.XML_HASH,
    tcbErrorPile = this.TCB_ERROR_PILE,
    reqResult = this.REQ_RESULT,
    reqResultDetail = this.REQ_RESULT_DETAIL.siVacio(""),
    reqResultDate = this.REQ_RESULT_DATE.siVacio(""),
)
