package com.personal.metricas.sincronizacion.domain.interactors

import androidx.core.util.TimeUtils
import com.personal.metricas.core.utils.TiempoHora
import com.personal.metricas.log.domain.entidades.Log
import com.personal.metricas.log.domain.interactors.GuardarLogCU
import com.personal.metricas.organizaciones.domain.entidades.Organizaciones
import com.personal.metricas.transacciones.data.repositorios.TransaccionesRepoImp
import com.personal.metricas.transacciones.domain.entidades.Transacciones
import com.personal.metricas.transacciones.domain.interactors.GuardarTransacciones

class RealizarSincronizacionCU(
	private val repoTrx: TransaccionesRepoImp,
	private val guardar: GuardarTransacciones,
	private val log: GuardarLogCU,
) {



	suspend fun sincronizarOrganizacion(organizacion: Organizaciones, tipo: TIPO_SINCRONIZACION) {
		val trx: List<Transacciones> = repoTrx.obtenerTransaccionesPorOrganizacion(organizacion.organizationId)
		//contador = contador+1
		val l: List<Transacciones> = trx.map {
			it.cXmlField = ""
			it.organizationCode = organizacion.organizationCode
			it.organizationName = organizacion.organizationName
			it.organizationId = organizacion.organizationId
			it.masterOrganizationId = organizacion.masterOrganizationId
			it
		}

		guardar.guardar(l)
		log.guardar(Log(id = 0, organization_code = organizacion.organizationCode, hora = TiempoHora.ahora(), tipo = tipo, trx = trx.size))
		/*val s = "${organizacion.organizationCode} $contador/$totalOraganizacionesSincronizar"
		_uiState.value =(_uiState.value as UIState.Success).copy(infoSincro = s)
		App.log.v(s)


		if (contador == totalOraganizacionesSincronizar) {
			_uiState.value = UIState.Success(organizaciones = oraganizciones, trabajando = false)
			dialog.informacion(_t(R.string.information_actualizada)) { }
		}*/
	}
}