package com.personal.requestmobility.transacciones.data.ds.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.personal.requestmobility.core.room.IRoom
import com.personal.requestmobility.transacciones.domain.entidades.Transacciones

@Entity(tableName = "Transacciones")
data class TransaccionesRoom(
    @PrimaryKey var MOB_REQUEST_ID: Int,
    var PROGRAM_VERSION: String? = "",
    var C_XML_FIELD: String? = "",
    var CREATION_DATE: String? = "",
    var REQ_STATUS: Int? = -1,
    var NUMERO: String? = "",
    var TIPO_MOV: String? = "",
    var LECTORA_ID: String? = "",
    var USUARIO_LECTORA: String? = "",
    var REQ_MESSAGE: String? = "",
    var C_LOB_FIELD: String? = "",
    var PARENT_REQUEST_ID: Int? = -1,
    var REDONE_BY_REQ_ID: Int? = -1,
    var DONE_FOR_REQ_ID: Int? = -1,
    var PROCESO: String? = "",
    var LANGUAGE_CODE: String? = "",
    var READER_XML_ID: String? = "",
    var XML_HASH: String? = "",
    var TCB_ERROR_PILE: String? = "",
    var REQ_RESULT: String? = "",
    var REQ_RESULT_DETAIL: String? = "",
    var REQ_RESULT_DATE: String? = "",
) : IRoom


fun TransaccionesRoom.toTransacciones() : Transacciones{
    val DESCONOCIDO: Int = -1
    return   Transacciones(
        mobRequestId = this.MOB_REQUEST_ID,
        programVersion = this.PROGRAM_VERSION ?: "",
        cXmlField = this.C_XML_FIELD ?: "",
        creationDate = this.CREATION_DATE ?: "",
        reqStatus = this.REQ_STATUS ?: DESCONOCIDO,
        numero = this.NUMERO ?: "",
        tipoMov = this.TIPO_MOV ?: "",
        lectoraId = this.LECTORA_ID ?: "",
        usuarioLectora = this.USUARIO_LECTORA ?: "",
        reqMessage = this.REQ_MESSAGE ?: "",
        cLobField = this.C_LOB_FIELD ?: "",
        parentRequestId = this.PARENT_REQUEST_ID ?: DESCONOCIDO,
        redoneByReqId = this.REDONE_BY_REQ_ID ?: DESCONOCIDO,
        doneForReqId = this.DONE_FOR_REQ_ID ?: DESCONOCIDO,
        proceso = this.PROCESO ?: "",
        languageCode = this.LANGUAGE_CODE ?: "",
        readerXmlId = this.READER_XML_ID ?: "",
        xmlHash = this.XML_HASH ?: "",
        tcbErrorPile = this.TCB_ERROR_PILE ?: "",
        reqResult = this.REQ_RESULT ?: "",
        reqResultDetail = this.REQ_RESULT_DETAIL ?: "",
        reqResultDate = this.REQ_RESULT_DATE ?: "",
    )

}