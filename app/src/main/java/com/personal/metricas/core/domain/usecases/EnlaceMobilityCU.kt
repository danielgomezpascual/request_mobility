package com.personal.metricas.core.domain.usecases

import android.util.Base64
import com.personal.metricas.transacciones.domain.repositorios.IRepoTransacciones
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EnlaceMobilityCU(private val repoTransacciones: IRepoTransacciones) {

    suspend fun ejecutar(mobRequestId: String): String {
        // 1. Obtener transacción
        val transacciones = repoTransacciones.obtenerTransacciones()
        val transaccion =
                transacciones.firstOrNull {
                    // Comparar como String o Int según sea el caso.
                    // En MA_CeldaFiltro el valor es TEXT pero mobRequestId en Transacciones es Int.
                    it.mobRequestId.toString() == mobRequestId
                }
                        ?: throw Exception("Transacción no encontrada: $mobRequestId")

        // 2. Construir SQL
        val sql = construirSql(transaccion)

        // 3. Base64
        val base64 = Base64.encodeToString(sql.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)

        return base64
    }

    private fun construirSql(
            t: com.personal.metricas.transacciones.domain.entidades.Transacciones
    ): String {
        // Formateo de fechas
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val fechaActual = sdf.format(Date())

        // Mapeo de campos.
        // Nota: Ajustar según los campos reales de la entidad Transacciones
        // Se asume que la entidad Transacciones tiene los datos necesarios.

        // Limpiar strings para evitar errores de SQL injection básicos si fuera necesario,
        // pero aquí solo sustituimos comillas simples
        fun String.clean(): String = this.replace("'", "''")

        return """
            INSERT INTO T_TRANSACTION (
                numero,
                tipo_mov,
                ora_prog_vers,
                proceso,
                usuario_lectora,
                language_code,
                org_Id,
                organization_id,
                date,
                lectora_id,
                content_data,
                client_version,
                request_code,
                request_message,
                description,
                subType,
                execution_time,
                enviado,
                reciclado,
                actions,
                printed_label,
                creation_date
            ) VALUES (
                '${t.numero.clean()}',            -- numero
                '${t.tipoMov.clean()}',          -- tipo_mov
                '${t.programVersion.clean()}',   -- ora_prog_vers
                '${t.proceso.clean()}',          -- proceso
                '${t.usuarioLectora.clean()}',   -- usuario_lectora
                'ES',                            -- language_code (Hardcoded ES)
                '${t.organizationId.clean()}',   -- org_Id
                '${t.organizationId.clean()}',   -- organization_id
                '${t.creationDate.clean()}',     -- date
                '${t.lectoraId.clean()}',        -- lectora_id
                '${t.cXmlField.clean()}',        -- content_data (Asumiendo cXmlField)
                '${t.programVersion.clean()}',   -- client_version
                '${t.reqStatus}',                -- request_code
                '${t.reqMessage.clean()}',       -- request_message
                '${t.detalles.clean()}',         -- description
                '',                              -- subType (No claro en mapping, vacío por defecto)
                0,                               -- execution_time
                0,                               -- enviado
                0,                               -- reciclado
                0,                               -- actions
                NULL,                            -- printed_label
                '$fechaActual'                   -- creation_date
            );
        """.trimIndent()
    }
}
