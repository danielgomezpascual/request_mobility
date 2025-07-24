package com.personal.metricas.core.composables.dialogos

sealed class Dialogos{
    data object Vacio : Dialogos()
    data class Informacion( val texto: String = "", val onResult : (DialogosResultado) -> Unit): Dialogos()
    data class SiNo(val texto: String = "", val onResult : (DialogosResultado) -> Unit): Dialogos()
    data class Input(val textoInformacion: String = "",val textoInicial: String = "", val onResult : (DialogosResultado, Any) -> Unit): Dialogos()
    data class Nota(val textoInformacion: String = "",val textoInicial: String = "", val onResult : (DialogosResultado, Any) -> Unit): Dialogos()
}

sealed class DialogosResultado{
    data object Si: DialogosResultado()
    data object No: DialogosResultado()
    data object Confirmado: DialogosResultado()
    data object Descartado: DialogosResultado()
}