package com.personal.requestmobility.core.composables.dialogos

sealed class Dialogos{
    data object Vacio : Dialogos()
    data class Informacion( val texto: String = "", val onResult : (DialogosResultado) -> Unit): Dialogos()
    data class SiNo(val texto: String = "", val onResult : (DialogosResultado) -> Unit): Dialogos()
}

sealed class DialogosResultado{
    data object Si: DialogosResultado()
    data object No: DialogosResultado()
    data object Confirmado: DialogosResultado()
    data object Descartado: DialogosResultado()
}