package com.personal.metricas.core.composables.dialogos

import com.personal.metricas.App
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DialogManager {

    private val _dialogoActual = MutableStateFlow<Dialogos>(Dialogos.Vacio)
    val dialogoActual: StateFlow<Dialogos> = _dialogoActual.asStateFlow()


    fun sino(texto: String,
             onResultadoDialog: (DialogosResultado) -> Unit) {


        _dialogoActual.value = Dialogos.SiNo(texto) { resultado ->
            onResultadoDialog(resultado)
            onDialogoDescartado()
        }

    }

    fun informacion(texto: String,
                    onResultadoDialog: (DialogosResultado) -> Unit) {

        _dialogoActual.value = Dialogos.Informacion(texto) { resultado ->
            onResultadoDialog(resultado)
            onDialogoDescartado()
        }
    }

    fun input(texto: String,
              textoInicial: String,
              onResultadoDialog: (DialogosResultado, Any) -> Unit){
        _dialogoActual.value = Dialogos.Input(texto, textoInicial= textoInicial) { resultado, str ->
            App.log.d("Resultado del texto $str")
            onResultadoDialog(resultado, str)
            onDialogoDescartado()
        }
    }

    fun nota(texto: String,
              textoInicial: String,
              onResultadoDialog: (DialogosResultado, Any) -> Unit){
        _dialogoActual.value = Dialogos.Nota(
            texto, textoInicial= textoInicial) { resultado, str ->
            App.log.d("Resultado del texto $str")
            onResultadoDialog(resultado, str)
            onDialogoDescartado()
        }
    }



    fun onDialogoDescartado() {
        App.log.d("Cerramos el dialogo.")
        _dialogoActual.value = Dialogos.Vacio
    }
}