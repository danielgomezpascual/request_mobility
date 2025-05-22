package com.personal.requestmobility.dashboards.data.ds


import com.personal.requestmobility.core.data.ds.IDS
import com.personal.requestmobility.core.data.ds.TIPO_DS
import com.personal.requestmobility.dashboards.domain.entidades.Dashboard


interface IDataSourceDashboard: IDS {


    /**
     * Obtiene todos los dashboards.
     * @return Lista de [Dashboard].
     */
    suspend fun getAll(): List<Dashboard>

    /**
     * Elimina todos los dashboards de la fuente de datos.
     * Este método podría no estar permitido en todas las fuentes (ej. remotas).
     */
    suspend fun eliminarTodos()

    /**
     * Elimina un dashboard específico.
     * @param dashboard El [Dashboard] a eliminar.
     * Este método podría no estar permitido en todas las fuentes (ej. remotas).
     */
    suspend fun eliminar(dashboard: Dashboard)

    /**
     * Guarda un dashboard. Si el dashboard tiene id 0, se considera una nueva inserción.
     * @param dashboard El [Dashboard] a guardar.
     * @return El ID del dashboard guardado (especialmente relevante para inserciones locales).
     * Este método podría no estar permitido en todas las fuentes (ej. remotas).
     */
    suspend fun guardar(dashboard: Dashboard): Long

    /**
     * Obtiene un dashboard por su identificador.
     * @param identificador El ID del dashboard a obtener.
     * @return El [Dashboard] encontrado.
     * @throws DataExcepcion.DatosNoEncontrador si no se encuentra.
     * Este método podría no estar permitido en todas las fuentes (ej. remotas).
     */
    suspend fun getPorID(identificador: Int): Dashboard
}