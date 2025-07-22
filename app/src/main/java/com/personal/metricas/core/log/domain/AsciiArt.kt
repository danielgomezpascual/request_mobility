package com.personal.metricas.core.log.domain

object AsciiArt {
    val asciiFont = mapOf(
        'A' to listOf("  A  ", " A A ", "AAAAA", "A   A", "A   A"),
        'B' to listOf("BBBB ", "B   B", "BBBB ", "B   B", "BBBB "),
        'C' to listOf(" CCC ", "C   C", "C    ", "C   C", " CCC "),
        'D' to listOf("DDDD ", "D   D", "D   D", "D   D", "DDDD "),
        'E' to listOf("EEEEE", "E    ", "EEE  ", "E    ", "EEEEE"),
        'F' to listOf("FFFFF", "F    ", "FFF  ", "F    ", "F    "),
        'G' to listOf(" GGG ", "G    ", "G  GG", "G   G", " GGG "),
        'H' to listOf("H   H", "H   H", "HHHHH", "H   H", "H   H"),
        'I' to listOf(" III ", "  I  ", "  I  ", "  I  ", " III "),
        'J' to listOf("JJJJJ", "    J", "    J", "J   J", " JJJ "),
        'K' to listOf("K   K", "K  K ", "KKK  ", "K  K ", "K   K"),
        'L' to listOf("L    ", "L    ", "L    ", "L    ", "LLLLL"),
        'M' to listOf("M   M", "MM MM", "M M M", "M   M", "M   M"),
        'N' to listOf("N   N", "NN  N", "N N N", "N  NN", "N   N"),
        'O' to listOf(" OOO ", "O   O", "O   O", "O   O", " OOO "),
        'P' to listOf("PPPP ", "P   P", "PPPP ", "P    ", "P    "),
        'Q' to listOf(" QQQ ", "Q   Q", "Q   Q", "Q  QQ", " QQQQ"),
        'R' to listOf("RRRR ", "R   R", "RRRR ", "R  R ", "R   R"),
        'S' to listOf(" SSS ", "S    ", " SSS ", "    S", " SSS "),
        'T' to listOf("TTTTT", "  T  ", "  T  ", "  T  ", "  T  "),
        'U' to listOf("U   U", "U   U", "U   U", "U   U", " UUU "),
        'V' to listOf("V   V", "V   V", "V   V", " V V ", "  V  "),
        'W' to listOf("W   W", "W   W", "W W W", "WW WW", "W   W"),
        'X' to listOf("X   X", " X X ", "  X  ", " X X ", "X   X"),
        'Y' to listOf("Y   Y", " Y Y ", "  Y  ", "  Y  ", "  Y  "),
        'Z' to listOf("ZZZZZ", "   Z ", "  Z  ", " Z   ", "ZZZZZ"),
        '0' to listOf(" OOO ", "O   O", "O   O", "O   O", " OOO "),
        '1' to listOf("  1  ", " 11  ", "  1  ", "  1  ", "11111"),
        '2' to listOf(" 222 ", "2   2", "   2 ", "  2  ", "22222"),
        '3' to listOf(" 333 ", "3   3", "   33", "3   3", " 333 "),
        '4' to listOf("   4 ", "  44 ", " 4 4 ", "44444", "   4 "),
        '5' to listOf("55555", "5    ", "5555 ", "    5", "5555 "),
        '6' to listOf(" 666 ", "6    ", "6666 ", "6   6", " 666 "),
        '7' to listOf("77777", "   7 ", "  7  ", " 7   ", "7    "),
        '8' to listOf(" 888 ", "8   8", " 888 ", "8   8", " 888 "),
        '9' to listOf(" 999 ", "9   9", " 9999", "    9", " 999 "),
        ' ' to listOf("     ", "     ", "     ", "     ", "     "),
        '!' to listOf("  !  ", "  !  ", "  !  ", "     ", "  !  "),
        '?' to listOf(" ??? ", "?   ?", "   ? ", "     ", "  ?  "),
        '@' to listOf(" @@@ ", "@   @", "@ @@ ", "@    ", " @@@ "),
        '#' to listOf(" # # ", "#####", " # # ", "#####", " # # "),
        '$' to listOf(" $$$ ", "$    ", " $$$ ", "    $", " $$$ "),
        '%' to listOf("%   %", "   % ", "  %  ", " %   ", "%   %"),
        '&' to listOf("  &  ", " & & ", "  &  ", "&   &", " & & "),
        '*' to listOf("     ", " * * ", "  *  ", " * * ", "     "),
        '-' to listOf("     ", "     ", "-----", "     ", "     "),
        '_' to listOf("     ", "     ", "     ", "     ", "____ "),
        '+' to listOf("     ", "  +  ", " +++ ", "  +  ", "     "),
        '=' to listOf("     ", "=====", "     ", "=====", "     "),
        '/' to listOf("    /", "   / ", "  /  ", " /   ", "/    "),
        '.' to listOf("     ", "     ", "     ", "     ", "  .  "),
        ',' to listOf("     ", "     ", "     ", "  ,  ", " ,   ")
    )

    fun from(input: String): String {
        val output = mutableListOf<StringBuilder>()
        for (i in 0 until 5) output.add(StringBuilder()) // Crear 5 líneas para el ASCII art

        for (char in input.uppercase()) {
            val asciiLines = asciiFont[char] ?: listOf("?????", "?????", "?????", "?????", "?????")
            for (i in 0 until 5) {
                output[i].append(asciiLines[i]).append("    ")
            }
        }

        return output.joinToString("\n") { it.toString() }
    }
}
