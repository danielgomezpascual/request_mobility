package com.personal.requestmobility.transacciones.data.ds.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.personal.requestmobility.core.room.IRoom
import com.personal.requestmobility.transacciones.domain.entidades.Transacciones
import kotlin.String

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
    var ETIQUETAS: String = "",
    var DETALLES: String = "",
    var LECTORA_FISICA_ID: String = "",

    var ORGANIZATION_CODE: String = "",
    var ORGANIZATION_NAME: String = "",
    var ORGANIZATION_ID: String = "",
    var MASTERORGANIZATION_ID: String = ""



) : IRoom


fun TransaccionesRoom.toTransacciones(): Transacciones {
    val DESCONOCIDO: Int = -1
    return Transacciones(
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
        etiquetas = this.ETIQUETAS ?: "",
        detalles = this.DETALLES ?: "",
        lectoraFisicaId = this.LECTORA_FISICA_ID ?: "",
         organizationCode = this.ORGANIZATION_CODE ?: "",
     	organizationName = this.ORGANIZATION_NAME ?: "",
     	organizationId = this.ORGANIZATION_ID ?: "",
     	masterOrganizationId = this.MASTERORGANIZATION_ID ?: ""


    )
}


fun TransaccionesRoom.fromTransaccion(trx: Transacciones): TransaccionesRoom = TransaccionesRoom(
    MOB_REQUEST_ID = trx.mobRequestId,
    PROGRAM_VERSION = trx.programVersion,
    C_XML_FIELD = trx.cXmlField,
    CREATION_DATE = trx.creationDate,
    REQ_STATUS = trx.reqStatus,
    NUMERO = trx.numero,
    TIPO_MOV = trx.tipoMov,
    LECTORA_ID = trx.lectoraId,
    USUARIO_LECTORA = trx.usuarioLectora,
    REQ_MESSAGE = trx.reqMessage,
    C_LOB_FIELD = trx.cLobField,
    PARENT_REQUEST_ID = trx.parentRequestId,
    REDONE_BY_REQ_ID = trx.redoneByReqId,
    DONE_FOR_REQ_ID = trx.doneForReqId,
    PROCESO = trx.proceso,
    LANGUAGE_CODE = trx.languageCode,
    READER_XML_ID = trx.readerXmlId,
    XML_HASH = trx.xmlHash,
    TCB_ERROR_PILE = trx.tcbErrorPile,
    REQ_RESULT = trx.reqResult,
    REQ_RESULT_DETAIL = trx.reqResultDetail,
    REQ_RESULT_DATE = trx.reqResultDate,
    ETIQUETAS = trx.etiquetas,
    DETALLES = trx.detalles,
    LECTORA_FISICA_ID = trx.lectoraFisicaId,
	 ORGANIZATION_CODE = trx.organizationCode,
	 ORGANIZATION_NAME = trx.organizationName,
	 ORGANIZATION_ID = trx.organizationId,
	 MASTERORGANIZATION_ID = trx.masterOrganizationId,
)


