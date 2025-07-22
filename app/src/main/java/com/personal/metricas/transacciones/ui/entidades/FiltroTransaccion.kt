package com.personal.metricas.transacciones.ui.entidades

sealed class Filtro {

    data class Busqueda(
        val identificador: Int,
        val campo: String,
        val condicion: (String, TransaccionesUI) -> Boolean,
        val seleccionado: Boolean = false,
        val valorBuscar: String = ""
    ) : Filtro()

    data class Seleccion(
        val identificador: Int,
        val campo: String,
        val descripcion: String = ",1",
        val condicion: (TransaccionesUI) -> Boolean,
        var seleccionado: Boolean = false,
        var invertido: Boolean = false) : Filtro()

}

