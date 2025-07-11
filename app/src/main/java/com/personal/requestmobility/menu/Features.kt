package com.personal.requestmobility.menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DataThresholding
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.GolfCourse
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TableRows
import androidx.compose.material.icons.filled.TableView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.personal.requestmobility.menu.navegacion.Modulos

sealed class Features {
    data class Menu(val texto: String = "Menu", val icono: ImageVector = Icons.Filled.Menu) : Features()
    data class Previo(val texto: String = "Previo", val icono: ImageVector = Icons.Filled.Preview,  val color : Color = Color(216, 67, 21, 255)) : Features()
    data class Transacciones(val texto: String = "Transacciones", val icono: ImageVector = Icons.Filled.TableView) : Features()
    data class Kpi(val texto: String = "Kpi", val icono: ImageVector = Icons.Filled.DataThresholding) : Features()
    data class Cuadriculas(val texto: String = "Cuadriculas", val icono: ImageVector = Icons.Filled.DeveloperBoard) : Features()
    data class Dashboard(val texto: String = "Dashboard", val icono: ImageVector = Icons.Filled.Dashboard) : Features()
    data class Duplicar(val texto: String = "Duplocar", val icono: ImageVector = Icons.Filled.ContentCopy) : Features()
    data class Paneles(val texto: String = "Paneles", val icono: ImageVector = Icons.Filled.SelectAll) : Features()
    data class Sincronizar(val texto: String = "Sincronizar", val icono: ImageVector = Icons.Filled.Sync) : Features()
    data class Herramientas(val texto: String = "Herramientas", val icono: ImageVector = Icons.Filled.Settings) : Features()


    data class Nuevo(val texto: String = "Nuevo", val icono: ImageVector = Icons.Filled.Add, val color : Color = Color(210, 141, 1, 255)) : Features()
    data class Guardar(val texto: String = "Guardar", val icono: ImageVector = Icons.Filled.Check, val color : Color = Color(60, 160, 63, 255)) : Features()
    data class Eliminar(val texto: String = "Eliminar", val icono: ImageVector = Icons.Filled.Cancel, val color : Color = Color(170, 22, 68, 255)) : Features()
    data class EliminarDatosActuales(val texto: String = "Eliminar", val icono: ImageVector = Icons.Filled.Cancel, val color : Color = Color(170, 22, 68, 255)) : Features()
    data class Probar(val texto: String = "Probar", val icono: ImageVector = Icons.Filled.GolfCourse, val color : Color = Color(22, 54, 170, 255)) : Features()

}