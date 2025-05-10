package com.personal.requestmobility.core.log.domain


interface IOutputLog {
    fun escribir(trazas: Traza)
}

class MyLog(private val configuracion: ConfiguracionMyLog, private val output: IOutputLog) {

    private var indicePuntoDebug: Int = 0

    init {
        importante("Clase de marcado habilitada. Comineza a mosrar trazas")
    }

    fun test() {
        val texto: String = "Hola mundo"
        d("d: $texto")
        w("W: $texto")
        e("e: $texto")
        i("i: $texto")
        v("i: $texto")

        importante("importante: $texto", true)
        recuadro("recuadro : $texto")
        linea()
        multiples("Variable 1", "Variable 2", "Variable 3")
        asciiArt("ASCII ART")
        marca()
    }

    fun d(mensaje: Any?) = printTraza(TipoTraza.DEBUG, (mensaje ?: "").toString())
    fun w(mensaje: Any?) = printTraza(TipoTraza.WARNING, (mensaje ?: "").toString())
    fun e(mensaje: Any?) = printTraza(TipoTraza.ERROR, (mensaje ?: "").toString())
    fun i(mensaje: Any?) = printTraza(TipoTraza.INFO, (mensaje ?: "").toString())
    fun v(mensaje: Any?) = printTraza(TipoTraza.VERBOSE, (mensaje ?: "").toString())

    fun lista( titulo: String = "Lista de elementos",l: List<Any>, completa: Boolean = false, numElementos : Int = 10)  = lista(l, titulo, completa, numElementos)


    fun lista(l: List<Any>, titulo: String = "Lista de elementos", completa: Boolean = false, numElementos : Int = 10) {
        enter()
        importante("$titulo")

        if (l.isEmpty()){
            d("Vacio")
            linea()
            enter()
            return
        }

        val lf = if (completa || l.size < numElementos ) l else l.subList(0, numElementos)
        lf.forEachIndexed { index, any -> printTraza(TipoTraza.DEBUG, "[$index] ${any.toString()}") }
        if (completa) {
            val elementosRestantes = l.size - lf.size
            d("...  + $elementosRestantes elementos más")
        }
        d("${l.size} elementos")
        linea()
        enter()
    }

    fun enter() = printTraza(TipoTraza.DEBUG, "\n")

    fun c(mensaje: String) {

        printTraza(TipoTraza.DEBUG, "------------------------------------------------------------------------------------------------------------")
        printTraza(TipoTraza.DEBUG, "                   $mensaje")
        printTraza(TipoTraza.DEBUG, "------------------------------------------------------------------------------------------------------------")

    }

    fun importante(mensaje: String, mayusculas: Boolean = false) {
        //todo: en algunos casos no se muestra bien la info del fichero y procedimiento qu e se esta mostradndo, habria que actualizar en determiandos casos el indice del stacktrace.
        val _mensaje = if (mayusculas) mensaje.uppercase() else mensaje
        printTraza(TipoTraza.DEBUG, " ")
        c(_mensaje)
        printTraza(TipoTraza.DEBUG, " ")
    }

    fun linea(l : Int = 100) {
        val cadena: String = "─".repeat(l)
        printTraza(TipoTraza.DEBUG, "$cadena")
    }


    fun recuadro(mensaje: String) {
        val border = "═".repeat(mensaje.length + 8)
        val top = "╔$border╗"
        val bottom = "╚$border╝"
        val middle = "║    $mensaje    ║"
        printTraza(TipoTraza.DEBUG, "\n$top\n$middle\n$bottom")
    }

    fun multiples(vararg mensajes: Any?) {
        val _mensajes = if (mensajes == null) arrayOf("") else mensajes

        printTraza(TipoTraza.DEBUG, "${_mensajes.size.toString()} elementos")
        _mensajes.forEachIndexed { indice, mensaje ->
            printTraza(TipoTraza.DEBUG, "  " + indice.toString() + ": " + (mensaje ?: "").toString())
        }
        printTraza(TipoTraza.DEBUG, "..................................................................................................")

    }

    fun asciiArt(mensaje: String) {
        val t = "\n" + AsciiArt.from(mensaje)
        printTraza(TipoTraza.DEBUG, "")
        printTraza(TipoTraza.DEBUG, t)
        printTraza(TipoTraza.DEBUG, "")
    }

    fun marca() {
        printTraza(TipoTraza.DEBUG, configuracion.TAG + " - " + indicePuntoDebug.toString())
        indicePuntoDebug++
    }

    private fun printTraza(tipo: TipoTraza, mensaje: String) {
        val hilo = if (configuracion.mostrarNombreHilo) "[${Thread.currentThread().name}]" else ""
        val infoCodigoFuente = if (configuracion.mostrarNombreHilo) dameInformacionTrazaCodigoFuente() else ""
        val outputMensaje = "$hilo $infoCodigoFuente: $mensaje"
        output.escribir(Traza(configuracion.TAG, tipo, outputMensaje))
    }


    private fun dameInformacionTrazaCodigoFuente(): String {

        val sts: Array<StackTraceElement>? = Thread.currentThread().stackTrace
        val st: StackTraceElement = sts!!.get(configuracion.trazaInformacionStackTrace)


        val nombreClase = st.className.split(".").last()
        val informacion: String = "${nombreClase} > ${st.methodName}:${st.lineNumber.toString()}"
        return informacion
    }
}


