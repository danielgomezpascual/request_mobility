package com.personal.metricas.sincronizacion.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.metricas.App
import com.personal.metricas.core.utils.K
import com.personal.metricas.sincronizacion.domain.interactors.ObtenerOrganizacionesCU
import com.personal.metricas.sincronizacion.domain.interactors.RealizarSincronizacionCU
import com.personal.metricas.sincronizacion.domain.interactors.TIPO_SINCRONIZACION
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

// 0: Green (< 1h), 1: Yellow (1h-5h), 2: Red (5h-12h), 3: Black (> 12h)
data class SyncStatus(val timeText: String = "00:00", val colorLevel: Int = 0)

class GlobalSyncViewModel(
        private val obtenerOrganizacionesCU: ObtenerOrganizacionesCU,
        private val realizarSincronizacionCU: RealizarSincronizacionCU
) : ViewModel() {

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _syncStatus = MutableStateFlow(SyncStatus())
    val syncStatus: StateFlow<SyncStatus> = _syncStatus.asStateFlow()

    private val _navigateToHome = Channel<Unit>()
    val navigateToHome = _navigateToHome.receiveAsFlow()

    init {
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                updateTime()
                delay(1000) // Update every second
            }
        }
    }

    private fun updateTime() {
        var lastSyncTimeStr = "0"
        try {
            // Intenta obtener como String
            lastSyncTimeStr = App.sharedPrerfences.get(K.ULTIMA_SINCRONIZACION, "0")
        } catch (e: Exception) {
            // Si falla (posiblemente era un Long), intenta recuperar como Long y convertir
            try {
                val lastSyncLong = App.sharedPrerfences.get(K.ULTIMA_SINCRONIZACION, 0L)
                lastSyncTimeStr = lastSyncLong.toString()

                // Corregir el formato en SharedPreferences para el futuro
                if (lastSyncLong != 0L) {
                    App.sharedPrerfences.put(K.ULTIMA_SINCRONIZACION, lastSyncTimeStr)
                }
            } catch (e2: Exception) {
                lastSyncTimeStr = "0"
            }
        }

        if (lastSyncTimeStr == "0") {
            _syncStatus.value = SyncStatus("00:00:00", 0)
            return
        }

        val currentTime = System.currentTimeMillis()

        // Conversión solicitada: String -> Double -> Diferencia -> Long
        val lastSyncDouble = lastSyncTimeStr.toDoubleOrNull() ?: 0.0
        val currentDouble = currentTime.toDouble()
        val diffMillis = (currentDouble - lastSyncDouble).toLong()

        // Calculate hours, minutes, seconds
        val hours = TimeUnit.MILLISECONDS.toHours(diffMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diffMillis) % 60

        val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)

        // Determine Level based on hours
        val level =
                when {
                    hours < 1 -> 0 // 0 to 0:59:59 -> Green
                    hours < 5 -> 1 // 1:00:00 to 4:59:59 -> Yellow
                    hours < 12 -> 2 // 5:00:00 to 11:59:59 -> Red
                    else -> 3 // >= 12h -> Black ("negro")
                }

        _syncStatus.value = SyncStatus(timeString, level)
    }

    fun sync() {
        if (_isSyncing.value) return

        viewModelScope.launch(Dispatchers.IO) {
            _isSyncing.value = true
            try {
                // Recuperar los IDs seleccionados desde Shared Preferences
                val organizacionesStr: String = App.sharedPrerfences.get(K.ORGANIZACIONES, "")
                if (organizacionesStr.isNotEmpty()) {
                    val selectedIds = organizacionesStr.split(";")
                    val allOrgs = obtenerOrganizacionesCU.getAll()

                    val orgsToSync = allOrgs.filter { selectedIds.contains(it.organizationId) }

                    orgsToSync.forEach { org ->
                        realizarSincronizacionCU.sincronizarOrganizacion(
                                org,
                                TIPO_SINCRONIZACION.MANUAL
                        )
                    }
                    // Update timestamp on successful sync start/finish (Assuming finish here)
                    App.sharedPrerfences.put(
                            K.ULTIMA_SINCRONIZACION,
                            System.currentTimeMillis().toString()
                    )
                    updateTime() // Force immediate update
                    _navigateToHome.send(Unit)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isSyncing.value = false
            }
        }
    }
}
