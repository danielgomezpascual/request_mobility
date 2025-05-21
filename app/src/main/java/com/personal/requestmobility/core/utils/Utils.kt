package com.personal.requestmobility.core.utils

import com.google.gson.Gson
import com.personal.requestmobility.App
import com.personal.requestmobility.core.composables.componentes.panel.PanelConfiguracion
import kotlin.jvm.java

object Utils {

    fun esTrue(valor: String, valorTrue: String = "Y", diferenciarMayuscula: Boolean = true) = valor.equals(other = valorTrue, ignoreCase = diferenciarMayuscula)


    fun toSiNo(valor: Boolean, valorTrue: String = "Y", valorFalse: String = "N") = if (valor) valorTrue else valorFalse


}

fun String.esNumerico(): Boolean {
    return this.matches(Regex("^-?\\d+([.,]\\d+)?$"))
}

fun <T> if3(condicion: Boolean, valorTrue: T, valorFalse: T): T {
    return if (condicion) valorTrue else valorFalse
}


fun <T> _toJson(clase: T) = Gson().toJson(clase)
inline fun <reified T> _toObjectFromJson(json: String): T? {

    try {
        val o = Gson().fromJson<T>(json, T::class.java)
        return o
    } catch (e: Exception) {
        e.printStackTrace()
        return  null
    }

}