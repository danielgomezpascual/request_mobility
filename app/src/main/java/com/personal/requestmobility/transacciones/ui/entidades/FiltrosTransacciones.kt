package com.personal.requestmobility.transacciones.ui.entidades

data class FiltrosTransacciones(val filtros: List<Filtro> = emptyList<Filtro>()){
    fun dameFiltroBusqueda(): Filtro.Busqueda? {
        filtros.forEach { filtro -> if (filtro is Filtro.Busqueda) return filtro}
        return null
    }
}

