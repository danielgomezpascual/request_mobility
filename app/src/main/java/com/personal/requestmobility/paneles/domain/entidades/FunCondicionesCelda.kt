package com.personal.requestmobility.paneles.domain.entidades

import androidx.compose.runtime.Composable

data class FuncionesCondicionCelda(val id: Int = 0,
                                   val nombre: String = "",
                                   val sobreTodoConjunto: Boolean,
                                   val representaciones :   List<Int> = emptyList<Int>(),
                                   val composable: @Composable () -> Unit)



