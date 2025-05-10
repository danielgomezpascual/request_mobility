package com.personal.requestmobility.transacciones.ui.entidades

import androidx.room.util.EMPTY_STRING_ARRAY
import com.personal.requestmobility.transacciones.domain.entidades.Transacciones

data class TransaccionesUI(
    val mobRequestId: Int = 0,
    val programVersion: String = "",
    //lar cXmlField: String = "",
    val creationDate: String = "",
    val estado: EstadoProceso = EstadoProceso.DESCONOCIDO,
    val reqStatus: Int = -1,
    val numero: String = "",
    val tipoMov: String = "",
    val lectoraLogica: String = "",
    val lectoraFisica: String = "",
    val usuarioLectora: String = "",
    val reqMessage: String = "",
    //lar cLobField: String = "",
    val parentRequestId: Int = -1,
    val redoneByReqId: Int = -1,
    val doneForReqId: Int = -1,
    val proceso: String = "",
    val languageCode: String = "",
    //lar readerXmlId: String = "",
    val xmlHash: String = "",
    //lar tcbErrorPile: String = "",
    val reqResult: String = "",
    val reqResultDetail: String = "",
    val reqResultDate: String = "",
    val seleccionada : Boolean = false,
    val visible : Boolean = true //se ve o no en la lista
){

    override fun toString(): String {
        return "TransaccionesUI(mobRequestId=$mobRequestId, programVersion='$programVersion', creationDate='$creationDate', estado=$estado, reqStatus=$reqStatus, numero='$numero', tipoMov='$tipoMov', lectoraLogica='$lectoraLogica', lectoraFisica='$lectoraFisica', usuarioLectora='$usuarioLectora', reqMessage='$reqMessage', parentRequestId=$parentRequestId, redoneByReqId=$redoneByReqId, doneForReqId=$doneForReqId, proceso='$proceso', languageCode='$languageCode', xmlHash='$xmlHash', reqResult='$reqResult', reqResultDetail='$reqResultDetail', reqResultDate='$reqResultDate', seleccionada=$seleccionada)"
    }
}



fun TransaccionesUI.fromTransacciones(trx: Transacciones): TransaccionesUI {
    return TransaccionesUI(
        mobRequestId = trx.mobRequestId,
        programVersion = trx.programVersion,
       // cXmlField = trx.cXmlField,
        creationDate = trx.creationDate,
        estado = EstadoProceso.fromReqStatus(trx.reqStatus),
        reqStatus = trx.reqStatus,
        numero = trx.numero,
        tipoMov = trx.tipoMov,
        lectoraFisica = trx.lectoraId,
        lectoraLogica = trx.lectoraId,
        usuarioLectora = trx.usuarioLectora,
        reqMessage = trx.reqMessage,
      //  cLobField = trx.cLobField,
        parentRequestId = trx.parentRequestId,
        redoneByReqId = trx.redoneByReqId,
        doneForReqId = trx.doneForReqId,
        proceso = trx.proceso,
        languageCode = trx.languageCode,
      //  readerXmlId = trx.readerXmlId,
        xmlHash = trx.xmlHash,
     //   tcbErrorPile = trx.tcbErrorPile,
        reqResult = trx.reqResult,
        reqResultDetail = trx.reqResultDetail,
        reqResultDate = trx.reqResultDate,
    )

}

