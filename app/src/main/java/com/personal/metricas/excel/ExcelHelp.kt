package com.personal.metricas.excel

import androidx.compose.ui.graphics.Color
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import kotlin.math.max
import kotlin.math.pow

fun Cell.applyBackgroundColorFromHex(hexColor: String) {
	// Asegúrate de que el libro de trabajo sea un XSSFWorkbook
	val workbook = this.sheet.workbook
	if (workbook !is XSSFWorkbook) {
		println("Advertencia: Los colores personalizados solo son compatibles con archivos .xlsx (XSSFWorkbook).")
		return
	}

	try {
		// 1. Decodificar el string hexadecimal a un objeto java.awt.Color
		val color = ColorManager.hexToRgbBytes(hexColor) ?: return

		// 2. Crear un XSSFColor a partir del objeto Color
		val xssfColor = XSSFColor(color, null)

		// 3. Crear el estilo de celda
		val cellStyle = workbook.createCellStyle() as XSSFCellStyle
		cellStyle.setFillForegroundColor(xssfColor)
		cellStyle.fillPattern = FillPatternType.SOLID_FOREGROUND

		// 4. Aplicar el estilo a la celda
		this.cellStyle = cellStyle

	}
	catch (e: NumberFormatException) {
		// Manejo de error por si el string hexadecimal no es válido
		println("Error: El código hexadecimal '$hexColor' no es válido.")
	}
}


fun Sheet.autoSizeColumnAndroid(columnIndex: Int, maxContentLength: Int = 0) {
	var maxWidth = 0

	// Itera sobre las filas para encontrar el texto más largo en la columna
	for (row in this) {
		val cell = row.getCell(columnIndex)
		if (cell != null) {
			// Obtenemos el contenido de la celda como String
			val cellContent = cell.toString()
			maxWidth = max(maxWidth, cellContent.length)
		}
	}

	// Si se especificó un maxContentLength, lo usamos como tope superior
	if (maxContentLength > 0 && maxWidth > maxContentLength) {
		maxWidth = maxContentLength
	}

	// Establecemos el ancho. La unidad es 1/256 de un ancho de carácter.
	// Un valor entre 280 y 300 suele funcionar bien para fuentes estándar.
	if (maxWidth > 0) {
		setColumnWidth(columnIndex, maxWidth * 280)
	}
}