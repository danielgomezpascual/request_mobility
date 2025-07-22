package com.personal.metricas.core.composables.componentes

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

object MA_Colores {
    fun obtenerColorAleatorio(): Color {
        val indiceAleatorio = Random.nextInt(vibrantColorList.size)
        return vibrantColorList[indiceAleatorio]
    }


    val listaColoresDefecto = listOf(
        Color(0xFF6699CC), // Azul claro
        Color(0xFFFFA07A), // Salmón claro
        Color(0xFF8FBC8F), // Verde mar
        Color(0xFFDDA0DD), // Lavanda
        Color(0xFF4682B4), // Acero azul
        Color(0xFFE9967A), // Marrón claro
        Color(0xFF3CB371), // Verde medio mar
        Color(0xFFBA55D3), // Violeta medio
        Color(0xFF5F9EA0), // Azul verdoso
        Color(0xFFCD853F), // Sepia
        Color(0xFF2E8B57), // Verde mar oscuro
        Color(0xFF8A2BE2), // Azul violeta
        Color(0xFFF08080), // Coral claro
        Color(0xFF98FB98), // Verde pálido
        Color(0xFF87CEFA), // Azul cielo claro
        Color(0xFFE6E6FA), // Lavanda pálido
        Color(0xFF778899), // Gris claro pizarra
        Color(0xFFB0E0E6), // Azul polvo
        Color(0xFF90EE90), // Verde claro
        Color(0xFFD8BFD8), // Ciruela pálido
        Color(0xFFA9A9A9), // Gris oscuro
        Color(0xFFADD8E6), // Azul claro
        Color(0xFFADFF2F), // Verde amarillo
        Color(0xFFFFF0F5), // Lavanda rubor
        Color(0xFF696969), // Gris oscuro
        Color(0xFF87CEEB), // Azul cielo
        Color(0xFFFAFAD2), // Dorado claro
        Color(0xFFD3D3D3), // Gris claro
        Color(0xFFB0C4DE), // Azul acero claro
        Color(0xFFFFE4E1), // Niebla rosa
        Color(0xFFC0C0C0), // Plata
        Color(0xFFF5F5DC), // Beige
        Color(0xFFEEE8AA), // Dorado pálido
        Color(0xFFF0F8FF), // Azul Alicia
        Color(0xFFDCDCDC), // Gris claro
        Color(0xFFFAEBD7), // Blanco antiguo
        Color(0xFFFFDEAD), // Ante blanco
        Color(0xFFF5FFFA), // Menta crema
        Color(0xFF808080), // Gris
        Color(0xFFFFFACD), // Amarillo dorado claro
        Color(0xFF00FA9A), // Verde medio primavera
        Color(0xFFF0FFF0), // Miel rocío
        Color(0xFF708090), // Gris pizarra
        Color(0xFFE0FFFF), // Cian claro
        Color(0xFFF8F8FF), // Humo blanco
        Color(0xFF7B68EE), // Azul lavanda
        Color(0xFFF5F5F5), // Blanco humo
        Color(0xFF6495ED), // Azul celeste
        Color(0xFFFFF5EE), // Concha marina
        Color(0xFF483D8B), // Azul pizarra oscuro
        Color(0xFFFFF8DC), // Maíz seda
        Color(0xFF00BFFF), // Azul profundo cielo
        Color(0xFFFDF5E6), // Viejo encaje
        Color(0xFF1E90FF), // Azul Dodger
        Color(0xFFFFFFF0), // Marfil
        Color(0xFF4169E1), // Azul real
        Color(0xFFFFFFE0), // Amarillo claro
        Color(0xFF0000CD), // Azul medio
        Color(0xFFFFFFFA), // Salmón blanco
        Color(0xFF0000FF), // Azul
        Color(0xFFFFFACD), // Amarillo dorado claro
        Color(0xFF191970), // Azul medianoche
        Color(0xFFFFFF00), // Amarillo
        Color(0xFF008B8B), // Cian oscuro
        Color(0xFFFFFFE0), // Amarillo claro
        Color(0xFF008080), // Verde azulado
        Color(0xFFADFF2F), // Verde amarillo
        Color(0xFF000080), // Azul marino
        Color(0xFF98FB98), // Verde pálido
        Color(0xFF4B0082), // Índigo
        Color(0xFFF0E68C), // Caqui
        Color(0xFF800080), // Púrpura
        Color(0xFFFFFACD), // Amarillo dorado claro
        Color(0xFFEE82EE), // Violeta
        Color(0xFFFAF0E6), // Lino
        Color(0xFFDDA0DD), // Lavanda
        Color(0xFFE0FFFF), // Cian claro
        Color(0xFFDA70D6), // Orquídea
        Color(0xFFF0FFF0), // Miel rocío
        Color(0xFFC71585), // Rojo violeta medio
        Color(0xFFF5F5DC), // Beige
        Color(0xFFFF00FF), // Magenta
        Color(0xFFFFF8DC), // Maíz seda
        Color(0xFFFF69B4), // Rosa fuerte
        Color(0xFFFFFACD), // Amarillo dorado claro
        Color(0xFFFF1493), // Rosa profundo
        Color(0xFFF0FFF0), // Miel rocío
        Color(0xFFFFC0CB), // Rosa
        Color(0xFFDCDCDC), // Gris claro
        Color(0xFFDB7093), // Rojo pálido violeta
        Color(0xFFFAEBD7), // Blanco antiguo
        Color(0xFFFFE4E1), // Niebla rosa
        Color(0xFFF5FFFA), // Menta crema
        Color(0xFFFFF0F5), // Lavanda rubor
        Color(0xFFFFF5EE), // Concha marina
        Color(0xFFFFFFF0), // Marfil
        Color(0xFFFFFFE0), // Amarillo claro
        Color(0xFFFFFFFA), // Salmón blanco
        Color(0xFFF0F8FF), // Azul Alicia
        Color(0xFFE6E6FA), // Lavanda pálido
        Color(0xFFFFFACD)  // Amarillo dorado claro (repetido intencionalmente para llegar a 100)
    )


    val vibrantColorList = listOf(
        Color(0xFFFF0000),   // Rojo puro
        Color(0xFF00FF00),   // Verde puro
        Color(0xFF0000FF),   // Azul puro
        Color(0xFFFFFF00),   // Amarillo puro
        Color(0xFFFF00FF),   // Magenta puro
        Color(0xFF00FFFF),   // Cian puro
        Color(0xFFFF69B4),   // Rosa fuerte
        Color(0xFFFFA500),   // Naranja
        Color(0xFF800080),   // Púrpura
        Color(0xFF008000),   // Verde oscuro
        Color(0xFF000080),   // Azul marino
        Color(0xFF8B0000),   // Rojo oscuro
        Color(0xFFFFFFF0),   // Marfil (ligeramente menos vivo, pero útil para contrastes)
        Color(0xFFF0FFF0),   // Miel rocío (similar al anterior)
        Color(0xFF00FA9A),   // Verde medio primavera
        Color(0xFFFFD700),   // Oro
        Color(0xFF4682B4),   // Acero azul (un tono vivo de azul)
        Color(0xFFDC143C),   // Rojo carmesí
        Color(0xFF00CED1),   // Turquesa oscuro
        Color(0xFFDAA520),   // Dorado varilla
        Color(0xFFF4A460),   // Siena quemado
        Color(0xFF228B22),   // Verde bosque
        Color(0xFF4169E1),   // Azul real
        Color(0xFFB22222),   // Ladrillo fuego
        Color(0xFF9ACD32),   // Verde amarillo
        Color(0xFFFF4500),   // Naranja rojizo
        Color(0xFF7CFC00),   // Verde césped
        Color(0xFF6495ED),   // Azul celeste
        Color(0xFFD2691E),   // Chocolate
        Color(0xFF008080),   // Verde azulado
        Color(0xFF9370DB),   // Violeta medio
        Color(0xFFE9967A),   // Salmón oscuro
        Color(0xFFADFF2F),   // Verde lima
        Color(0xFFFF8C00),   // Naranja oscuro
        Color(0xFF00BFFF),   // Azul profundo cielo
        Color(0xFFFF7F50),   // Coral
        Color(0xFF32CD32),   // Verde lima brillante
        Color(0xFF1E90FF),   // Azul Dodger
        Color(0xFFB8860B),   // Dorado oscuro
        Color(0xFFFA8072),   // Salmón
        Color(0xFF00FF7F),   // Verde primavera
        Color(0xFF6A5ACD),   // Azul pizarra
        Color(0xFFD87093),   // Rojo pálido violeta
        Color(0xFF7FFF00),   // Chartreuse
        Color(0xFFFF6347),   // Tomate
        Color(0xFF40E0D0),   // Turquesa
        Color(0xFFBA55D3),   // Violeta medio
        Color(0xFFF08080),   // Coral claro
        Color(0xFF00FFFF),   // Cian
        Color(0xFF9932CC),   // Morado oscuro
        Color(0xFFE0FFFF),   // Cian claro
        Color(0xFFFF00FF),   // Magenta
        Color(0xFF8FBC8F),   // Verde mar oscuro
        Color(0xFFADD8E6),   // Azul claro
        Color(0xFFFFFAFA),   // Blanco nieve (contraste)
        Color(0xFFFAF0E6),   // Lino (contraste suave)
        Color(0xFF87CEFA),   // Azul cielo claro
        Color(0xFF7B68EE),   // Azul lavanda
        Color(0xFFF0E68C),   // Caqui
        Color(0xFFDDA0DD),   // Lavanda
        Color(0xFF98FB98),   // Verde pálido
        Color(0xFFB0E0E6),   // Azul polvo
        Color(0xFFEE82EE),   // Violeta
        Color(0xFF90EE90),   // Verde claro
        Color(0xFFFFA07A),   // Salmón claro
        Color(0xFF87CEEB),   // Azul cielo
        Color(0xFFD8BFD8),   // Ciruela pálido
        Color(0xFFE6E6FA),   // Lavanda pálido
        Color(0xFFF5F5DC),   // Beige (contraste neutro)
        Color(0xFFFFFACD),   // Amarillo dorado claro
        Color(0xFFF0FFF0),   // Miel rocío (contraste suave)
        Color(0xFFFFFFE0),   // Amarillo claro
        Color(0xFFF5FFFA),   // Menta crema (contraste suave)
        Color(0xFFF0F8FF),   // Azul Alicia (contraste suave)
        Color(0xFFFAEBD7),   // Blanco antiguo (contraste suave)
        Color(0xFFFFDEAD),   // Ante blanco (contraste suave)
        Color(0xFFFFF5EE),   // Concha marina (contraste suave)
        Color(0xFFFDF5E6),   // Viejo encaje (contraste suave)
        Color(0xFFFFFFF0),   // Marfil (contraste suave)
        Color(0xFFFFFFFA),   // Salmón blanco (contraste suave)
        Color(0xFFFFF8DC),   // Maíz seda (contraste suave)
        Color(0xFFFFFACD),   // Amarillo dorado claro (repetido para llegar a 100)
        Color(0xFFF0FFF0),   // Miel rocío (repetido)
        Color(0xFFFFFFE0),   // Amarillo claro (repetido)
        Color(0xFFF5FFFA),   // Menta crema (repetido)
        Color(0xFFF0F8FF),   // Azul Alicia (repetido)
        Color(0xFFFAEBD7),   // Blanco antiguo (repetido)
        Color(0xFFFFDEAD),   // Ante blanco (repetido)
        Color(0xFFFFF5EE),   // Concha marina (repetido)
        Color(0xFFFDF5E6),   // Viejo encaje (repetido)
        Color(0xFFFFFFF0),   // Marfil (repetido)
        Color(0xFFFFFFFA),   // Salmón blanco (repetido)
        Color(0xFFFFF8DC)    // Maíz seda (repetido)
    )

}