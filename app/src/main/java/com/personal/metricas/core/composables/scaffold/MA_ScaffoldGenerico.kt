package com.personal.metricas.core.composables.scaffold

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.metricas.App
import com.personal.metricas.core.composables.componentes.Cabecera
import com.personal.metricas.core.composables.componentes.TituloScreen
import com.personal.metricas.core.composables.imagenes.MA_Icono
import com.personal.metricas.core.navegacion.EventosNavegacion
import com.personal.metricas.core.utils.Parametros
import com.personal.metricas.core.utils.Preferencias
import com.personal.metricas.core.utils._toJson
import com.personal.metricas.dashboards.domain.entidades.Dashboard
import com.personal.metricas.dashboards.domain.entidades.TipoDashboard
import com.personal.metricas.dashboards.domain.interactors.ObtenerDashboardsAccesoDirectoCU
import com.personal.metricas.dashboards.ui.entidades.DashboardUI
import com.personal.metricas.dashboards.ui.entidades.fromDashboard
import com.personal.metricas.menu.Features
import com.personal.metricas.transacciones.domain.entidades.ResultadoSQL
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MA_ScaffoldGenerico(
        tituloScreen: TituloScreen,
        navegacion: (EventosNavegacion) -> Unit,
        accionesSuperiores: @Composable () -> Unit,
        contenido: @Composable () -> Unit,
        mostrarBotonesSuperioresYBarraInferior: Boolean = true
) {

    val obtenerAccesoDirectoCU: ObtenerDashboardsAccesoDirectoCU = koinInject()
    var accesosDirectos by remember { mutableStateOf(emptyList<DashboardUI>()) }

    LaunchedEffect(Unit) {
        obtenerAccesoDirectoCU.execute().collect { listaDashboardsDomain ->
            val listaDashboardExpandida = mutableListOf<Dashboard>()

            listaDashboardsDomain.forEach { dsh ->
                if (dsh.tipo == TipoDashboard.Dinamico()) {
                    try {
                        val filas = ResultadoSQL.fromSqlToTabla(dsh.kpiOrigenDatos).filas
                        filas.forEach { f ->
                            val dsExpansion =
                                    dsh.copy(
                                            nombre =
                                                    Parametros.reemplazar(
                                                            dsh.nombre,
                                                            parametrosKpi = f.toParametros(),
                                                            parametrosDashboard = f.toParametros()
                                                    ),
                                            parametros = f.toParametros()
                                    )
                            listaDashboardExpandida.add(dsExpansion)
                        }
                    } catch (e: Exception) {
                        // Si hay error en el SQL, al menos añadimos el original o nada
                        listaDashboardExpandida.add(dsh)
                    }
                } else {
                    listaDashboardExpandida.add(dsh)
                }
            }

            accesosDirectos = listaDashboardExpandida.map { DashboardUI().fromDashboard(it) }
        }
    }

    Scaffold(
            containerColor = Color(red = 245, green = 245, blue = 245, alpha = 100),
            topBar = {
                Box(modifier = Modifier.padding(vertical = 6.dp)) {
                    Column {
                        if (mostrarBotonesSuperioresYBarraInferior) {
                            Cabecera(tituloScreen, navegacion, accionesSuperiores)
                        }
                    }
                }
            },
            bottomBar = {
                if (mostrarBotonesSuperioresYBarraInferior) {
                    BottomAppBar(
                            modifier = Modifier.height(120.dp),
                            containerColor = Color(174, 213, 129, 10),
                            tonalElevation = 50.dp
                    ) {
                        Row(
                                modifier =
                                        Modifier.fillMaxWidth()
                                                .height(100.dp)
                                                .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (App.sharedPrerfences.get<Boolean>(
                                            Preferencias.ACCESO_SINCRONIZACION,
                                            true
                                    )
                            ) {
                                // Blue Theme for Sync
                                ColoredNavItem(
                                        icon = Features.Sincronizar().icono,
                                        texto = Features.Sincronizar().texto,
                                        backgroundColor = Color(0xFFE3F2FD), // Light Blue
                                        iconColor = Color(0xFF1565C0), // Dark Blue
                                        onClick = { navegacion(EventosNavegacion.Sincronizacion) }
                                )
                            }

                            val haptic = LocalHapticFeedback.current

                            // Gold/Amber Theme for Dashboard
                            ColoredNavItem(
                                    icon = Features.Cuadriculas().icono,
                                    texto = Features.Cuadriculas().texto,
                                    backgroundColor = Color(0xFFFFF8E1), // Light Amber
                                    iconColor = Color(0xFFD84315), // Deep Orange/Brownish
                                    onClick = { navegacion(EventosNavegacion.CuadriculaDashboard) },
                                    onLongClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        navegacion(EventosNavegacion.HomeApp)
                                    }
                            )
                            if (App.sharedPrerfences.get<Boolean>(
                                    Preferencias.ACCESO_HERRAMIENTAS,
                                    false
                                )
                            ) {
                                // Red/Pink Theme for Tools
                                ColoredNavItem(
                                    icon = Features.Herramientas().icono,
                                    texto = Features.Herramientas().texto,
                                    backgroundColor = Color(0xFFFFEBEE), // Light Red
                                    iconColor = Color(0xFFC62828), // Dark Red
                                    onClick = { navegacion(EventosNavegacion.MenuHerramientas) }
                                )
                            }


                            // Dynamic Dashboard Shortcuts
                            accesosDirectos.forEach { ds ->
                                ColoredNavItem(
                                        icon = Features.Dashboard().icono,
                                        texto = ds.nombre,
                                        backgroundColor = Color(ds.color).copy(alpha = 0.1f),
                                        iconColor = Color(ds.color),
                                        onClick = {
                                            navegacion(
                                                    EventosNavegacion.VisualizadorDashboard(
                                                            ds.id,
                                                            _toJson(ds.parametros)
                                                    )
                                            )
                                        }
                                )
                            }


                        }
                    }
                }
            }
    ) { paddingValues -> Box(Modifier.padding(paddingValues)) { contenido() } }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ColoredNavItem(
        icon: ImageVector,
        texto: String,
        backgroundColor: Color,
        iconColor: Color,
        onClick: () -> Unit,
        onLongClick: (() -> Unit)? = null
) {
    Column(
            modifier =
                    Modifier.padding(1.dp)
                            .fillMaxHeight()
                            .size(80.dp)
                            //.clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
                            .background(backgroundColor)
                            .combinedClickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onClick,
                                    onLongClick = onLongClick
                            ),
            //	contentAlignment = Alignment.Center,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(2.dp))
        MA_Icono(icono = icon, color = iconColor, modifier = Modifier.size(26.dp))
        Spacer(modifier = Modifier.height(2.dp))
        Text(
                text = texto,
                color = iconColor,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 2.dp)
        )
    }
}
