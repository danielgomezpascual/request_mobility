package com.personal.requestmobility.core.log.ui

import android.util.Log
import com.personal.requestmobility.core.log.domain.IOutputLog
import com.personal.requestmobility.core.log.domain.TipoTraza
import com.personal.requestmobility.core.log.domain.Traza

class LogcatOutput : IOutputLog {
    override fun escribir(trazas: Traza) {

        val mensaje = trazas.mensaje
        when (trazas.tipo) {
            TipoTraza.DEBUG -> Log.d(trazas.key, "${mensaje}")
            TipoTraza.INFO -> Log.i(trazas.key, "${mensaje}")
            TipoTraza.WARNING -> Log.w(trazas.key, "${mensaje}")
            TipoTraza.ERROR -> Log.e(trazas.key, "${mensaje}")
            TipoTraza.VERBOSE -> Log.v(trazas.key, "${mensaje}")
        }

    }
}