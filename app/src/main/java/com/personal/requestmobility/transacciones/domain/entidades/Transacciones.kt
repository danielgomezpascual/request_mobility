package com.personal.requestmobility.transacciones.domain.entidades

import androidx.room.PrimaryKey

class Transacciones(
  var mobRequestId : Int,
  var programVersion : String = "",
  var cXmlField : String = "",
  var creationDate : String = "",
  var reqStatus : Int = -1 ,
  var numero : String = "",
  var tipoMov : String = "",
  var lectoraId : String = "",
  var usuarioLectora : String = "",
  var reqMessage : String = "",
  var cLobField : String = "",
  var parentRequestId : Int = -1,
  var redoneByReqId : Int = -1,
  var doneForReqId : Int = -1,
  var proceso : String = "",
  var languageCode : String = "",
  var readerXmlId : String = "",
  var xmlHash : String = "",
  var tcbErrorPile : String = "",
  var reqResult : String = "",
  var reqResultDetail : String = "",
  var reqResultDate : String = "",
  var etiquetas: String = "",
  var detalles: String = "",
  var lectoraFisicaId: String = ""


)