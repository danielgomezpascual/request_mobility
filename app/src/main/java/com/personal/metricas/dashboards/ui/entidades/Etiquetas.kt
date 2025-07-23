package com.personal.metricas.dashboards.ui.entidades

data class Etiquetas(val etiqueta: String, val seleccionada : Boolean = false){
	companion object {
		fun EtiquetaVacia(): Etiquetas = Etiquetas(etiqueta =  "")

	}
}
