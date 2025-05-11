package com.personal.requestmobility.core.composables.componentes.GraTab

import com.personal.requestmobility.core.composables.graficas.ValoresGrafica
import com.personal.requestmobility.core.composables.tabla.ValoresTabla

data class GraTabData(
    val graTabConfiguracion: GraTabConfiguracion = GraTabConfiguracion(),
    var valoresGrafica: ValoresGrafica = ValoresGrafica(),
    var valoresTabla: ValoresTabla = ValoresTabla()) {

    fun ordenarElementos(grafica: Boolean = true, tabla: Boolean = true) {

        if (grafica && graTabConfiguracion.mostrarGrafica && valoresGrafica.tieneContenido()) {
            valoresGrafica = valoresGrafica.copy(elementos = valoresGrafica.dameElementosOrdenados())
        }
        if (tabla && graTabConfiguracion.mostrarTabla && valoresTabla.tieneContenido()) {
            valoresTabla = valoresTabla.copy(filas = valoresTabla.dameElementosOrdenados(campoOrdenacionTabla = graTabConfiguracion.campoSumaValorTabla))
        }
    }


    fun limiteElementos(grafica: Boolean = true, tabla: Boolean = true) {
        /*if (grafica && graTabConfiguracion.mostrarGrafica && valoresGrafica.tieneContenido()) {
            valoresGrafica = valoresGrafica.copy(elementos = valoresGrafica.dameElementosTruncados(limite = graTabConfiguracion.limiteElementos, agrupar = graTabConfiguracion.agruparValores))
        }*/
//todo: esta funcion debe devovler los elemntos con el elemtno agrupado
        if (tabla && graTabConfiguracion.mostrarTabla && valoresTabla.tieneContenido()) {
            valoresTabla = valoresTabla.copy(filas = valoresTabla.dameElementosTruncados(limite = graTabConfiguracion.limiteElementos, agrupar = graTabConfiguracion.agruparValores, indiceCampoSumar = graTabConfiguracion.campoSumaValorTabla))
        }
    }

    fun setupValores() {
        //establecemos los colores que va a tener cada elmento (teninedo en cuenta que deben al menos tener todos los coles declarados.
        if (graTabConfiguracion.colores.isNotEmpty() && graTabConfiguracion.colores.size >=  valoresGrafica.elementos.size) {
            valoresGrafica.elementos.forEachIndexed { index, it -> it.color = graTabConfiguracion.colores[index] }
            valoresTabla.filas =  valoresTabla.filas.mapIndexed {index,  f -> f.copy(color =graTabConfiguracion.colores[index]  ) }
        //    valoresTabla.filas.forEachIndexed { index, it -> it.color = graTabConfiguracion.colores[index] }
        }
    }


}