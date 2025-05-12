package com.personal.requestmobility.core.composables.componentes.GraTab

import com.personal.requestmobility.core.composables.graficas.ValoresGrafica
import com.personal.requestmobility.core.composables.tabla.Fila
import com.personal.requestmobility.core.composables.tabla.ValoresTabla

data class GraTabData(
    val graTabConfiguracion: GraTabConfiguracion = GraTabConfiguracion(),
    var valoresTabla: ValoresTabla = ValoresTabla()) {


    fun setupValores() {

        if (graTabConfiguracion.ordenado) {
            valoresTabla.filas = ordenarElementos()
        }

        if (graTabConfiguracion.limiteElementos > 0) {
            valoresTabla.filas = limiteElementos()
        }

        establecerColorFilas()
    }

    private fun ordenarElementos() = valoresTabla.dameElementosOrdenados(campoOrdenacionTabla = graTabConfiguracion.campoSumaValorTabla)

    private fun limiteElementos(): List<Fila> =
        valoresTabla.dameElementosTruncados(
            limite = graTabConfiguracion.limiteElementos,
            agrupar = graTabConfiguracion.agruparValores,
            indiceCampoSumar = graTabConfiguracion.campoSumaValorTabla
        )

    private fun establecerColorFilas() {
        //establecemos los colores que va a tener cada elmento (teninedo en cuenta que deben al menos tener todos los coles declarados.
        if (graTabConfiguracion.colores.isNotEmpty()) {
            val totalColores: Int = graTabConfiguracion.colores.size
            valoresTabla.filas = valoresTabla.filas.mapIndexed { index, f ->
                val indiceColor = index % totalColores
                f.copy(color = graTabConfiguracion.colores[indiceColor])
            }
        }
    }
}