package com.personal.metricas.excel

import kotlin.math.max
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFWorkbook

fun Cell.applyBackgroundColorFromHex(hexColor: String) {
    // Asegúrate de que el libro de trabajo sea un XSSFWorkbook
    val workbook = this.sheet.workbook
    if (workbook !is XSSFWorkbook) {
        println(
                "Advertencia: Los colores personalizados solo son compatibles con archivos .xlsx (XSSFWorkbook)."
        )
        return
    }

    try {
        // 1. Decodificar el string hexadecimal a un array de bytes (RGB)
        val rgb = ColorManager.hexToRgbBytes(hexColor) ?: return

        // 2. Crear un XSSFColor a partir de los bytes
        val xssfColor = XSSFColor(rgb, null)

        // 3. Crear el estilo de celda
        val cellStyle = workbook.createCellStyle() as XSSFCellStyle
        cellStyle.setFillForegroundColor(xssfColor)
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)

        // 4. Aplicar el estilo a la celda
        this.setCellStyle(cellStyle)
    } catch (e: Exception) {
        println("Error al aplicar el color '$hexColor': ${e.message}")
    }
}

fun Sheet.autoSizeColumnAndroid(
        columnIndex: Int,
        startRow: Int = 0,
        endRow: Int = Int.MAX_VALUE,
        maxContentLength: Int = 255
) {
    var maxWidth = 0

    // Itera sobre el rango de filas especificado
    for (i in startRow..minOf(endRow, lastRowNum)) {
        val row = getRow(i) ?: continue
        val cell = row.getCell(columnIndex) ?: continue

        // Omitimos celdas que son parte de un rango combinado (excepto si son la celda principal)
        // Aunque en POI getCell suele devolver la celda principal si está combinada,
        // si es una fila de título merged no queremos que determine el ancho de una única columna.
        var isPartOfLargeMerge = false
        for (range in mergedRegions) {
            if (range.isInRange(i, columnIndex)) {
                // Si el rango cubre más de una columna, no lo usamos para auto-size de una sola
                // columna
                if (range.firstColumn != range.lastColumn) {
                    isPartOfLargeMerge = true
                    break
                }
            }
        }
        if (isPartOfLargeMerge) continue

        // Obtenemos el contenido de la celda como String
        val cellContent = cell.toString()
        maxWidth = max(maxWidth, cellContent.length)
    }

    // Si se especificó un maxContentLength, lo usamos como tope superior
    if (maxContentLength > 0 && maxWidth > maxContentLength) {
        maxWidth = maxContentLength
    }

    // Establecemos el ancho. La unidad es 1/256 de un ancho de carácter.
    // Añadimos un pequeño margen extra (2 caracteres) para evitar cortes.
    if (maxWidth > 0) {
        val width = (maxWidth + 2) * 280
        setColumnWidth(
                columnIndex,
                minOf(width, 255 * 256)
        ) // Máximo permitido por Excel es 255 caracteres
    }
}
