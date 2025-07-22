package com.personal.metricas.kpi.data.repositorios

import com.personal.metricas.core.data.ds.TIPO_DS
import com.personal.metricas.core.data.repositorio.BaseRepositorio
import com.personal.metricas.kpi.data.ds.IDataSourceKpis
import com.personal.metricas.kpi.domain.entidades.Kpi
import com.personal.metricas.kpi.domain.repositorios.KpisRepositorio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class KpisRepositorioImp(fuentesDatos: List<IDataSourceKpis>) :
    BaseRepositorio<IDataSourceKpis>(fuentesDatos),
    KpisRepositorio {

    val origenFuente: TIPO_DS = TIPO_DS.ROOM

    override suspend fun getAll(): Flow<List<Kpi>> = flow { emit(dameDS(origenFuente).getAll()) }

    override suspend fun eliminar(kpi: Kpi) = dameDS(origenFuente).eliminar(kpi)

    override suspend fun eliminarTodos() = dameDS(origenFuente).eliminarTodas()

    override suspend fun guardar(kpi: Kpi): Long = dameDS(origenFuente).guardar(kpi)

    override suspend fun obtener(id: Int): Kpi = dameDS(origenFuente).obtener(id)

}