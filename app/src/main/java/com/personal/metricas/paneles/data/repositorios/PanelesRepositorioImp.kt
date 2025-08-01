package com.personal.metricas.paneles.data.repositorios

import com.personal.metricas.core.data.ds.TIPO_DS
import com.personal.metricas.core.data.repositorio.BaseRepositorio
import com.personal.metricas.paneles.data.ds.IDataSourcePaneles
import com.personal.metricas.paneles.domain.entidades.Panel
import com.personal.metricas.paneles.domain.repositorios.PanelesRepositorio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PanelesRepositorioImp(fuentesDatos: List<IDataSourcePaneles>) :
    BaseRepositorio<IDataSourcePaneles>(fuentesDatos),
    PanelesRepositorio {

    val origenFuente: TIPO_DS = TIPO_DS.ROOM

    override suspend fun getAll(): Flow<List<Panel>> = flow { emit(dameDS(origenFuente).getAll()) }

    override suspend fun eliminar(panel: Panel) = dameDS(origenFuente).eliminar(panel)

    override suspend fun eliminarTodos() = dameDS(origenFuente).eliminarTodas()

    override suspend fun guardar(panel: Panel): Long = dameDS(origenFuente).guardar(panel)

    override suspend fun obtener(id: Int): Panel = dameDS(origenFuente).obtener(id)

}