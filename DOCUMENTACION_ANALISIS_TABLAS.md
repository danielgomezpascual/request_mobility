# Análisis de Generación de Tablas Dinámicas en Paneles

## Introducción
Este documento detalla el funcionamiento interno de la aplicación para la generación y visualización de tablas dinámicas dentro de los paneles (dashboards). El objetivo es explicar por qué y cómo diferentes paneles muestran tablas con diferentes configuraciones de columnas.

## Flujo de Datos y Componentes Clave

El sistema se basa en una arquitectura dirigida por datos SQL, donde la estructura visual de la tabla es un reflejo directo del resultado de la consulta SQL subyacente.

### 1. Origen de Datos (`ResultadoSQL.kt`)
La clase `ResultadoSQL` es el núcleo de la lógica de transformación de datos.
- **Ejecución Dinámica**: Método `fromSqlToTabla(sql, ...)`:
  - Ejecuta una consulta SQL cruda contra la base de datos local (`AppDatabase`).
  - El resultado (`ResultadoEjecucionSQL`) contiene dos listas vitales:
    1. `titulos`: Nombres de las columnas devueltos por la consulta.
    2. `filas`: Los datos en sí.
- **Construcción de Metadatos**: Método `toValoresTabla()`:
  - Transforma los resultados crudos en objetos de UI (`ValoresTabla`, `Fila`, `Columnas`, `Celda`).
  - **Detección de Columnas**: Itera sobre los `titulos` recuperados de la query y genera dinámicamente objetos `Columnas`.
  - No hay columnas predefinidas en el código Kotlin; las columnas son exactamente las que devuelve el `SELECT` de la SQL.

### 2. Renderizado de UI (`MA_Panel.kt` y `VisualizadorDashboardUI.kt`)
- **VisualizadorDashboardUI**:
  - Actúa como contenedor, iterando sobre la lista de paneles configurados para un dashboard.
  - Instancia un `MA_Panel` para cada elemento.
- **MA_Panel**:
  - Recibe un objeto `PanelData`, que ya contiene la estructura `ValoresTabla` procesada anteriormente.
  - En el caso de `TiposPanel.PANEL_KPI`, evalúa si debe mostrar la tabla.
  - Llama a `dameTipoTabla`, pasando las filas y la configuración dinámica.

## Conclusión: ¿Por qué varían las columnas?

La aplicación no impone un esquema fijo para las tablas en los paneles. 
1. **Definición por KPI**: Cada Panel está vinculado a un "KPI" (Key Performance Indicator).
2. **Consulta SQL Única**: Cada KPI tiene asociada una sentencia SQL específica (ej. `SELECT nombre, edad FROM usuarios` vs `SELECT fecha, total, id_transaccion FROM ventas`).
3. **Proyección Dinámica**: Dado que la UI construye las columnas basándose en los metadatos de la respuesta SQL (`titulos`), el panel renderizará exactamente las columnas solicitadas en la query del KPI.

En resumen, **la estructura de la tabla en el panel es una representación visual directa y dinámica del esquema de resultados de la consulta SQL configurada para ese panel específico.**
